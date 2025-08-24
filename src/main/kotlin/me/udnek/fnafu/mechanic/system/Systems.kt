package me.udnek.fnafu.mechanic.system

import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.datacomponent.item.BundleContents
import io.papermc.paper.datacomponent.item.TooltipDisplay
import me.udnek.coreu.mgu.Resettable
import me.udnek.fnafu.FnafU
import me.udnek.fnafu.game.FnafUGame
import me.udnek.fnafu.item.Items
import me.udnek.fnafu.mechanic.system.camera.CameraSystem
import me.udnek.fnafu.mechanic.system.door.DoorSystem
import me.udnek.fnafu.mechanic.system.ventilation.VentilationSystem
import me.udnek.fnafu.misc.Ticking
import me.udnek.fnafu.player.FnafUPlayer
import net.kyori.adventure.text.Component
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable

open class Systems : Resettable, Ticking {

    companion object {
        const val TICKRATE = 10
        const val REBOOT_ALL_REPAIR_ICON_POSITION = 51
        const val SINGLE_REPAIR_DURATION = 4*20
        const val ALL_REPAIR_DURATION = 7*20
    }

    private val cursorPosToAction: Map<Int, (player: FnafUPlayer) -> Unit> = mapOf(
        18 to {player ->  door.startRepairing(player, menu, SINGLE_REPAIR_DURATION)},
        27 to {player ->  camera.startRepairing(player, menu, SINGLE_REPAIR_DURATION)},
        36 to {player ->  ventilation.startRepairing(player, menu, SINGLE_REPAIR_DURATION)},
        45 to { player ->  repairAll(player)})
    private val cursorItem = Items.CURSOR_ICON.item

    private val upButtons = listOf(9, 10, 18, 19, 27, 28)
    private val downButtons = listOf(11, 12, 20, 21, 29, 30)
    private val enterButtons = listOf(15, 16, 17, 23, 24, 25, 26, 32, 33, 34, 35)

    private val playersInsideSystem = ArrayList<FnafUPlayer>()
    var menu: SystemsMenu
        protected set
    val all: List<System>
    val door: DoorSystem
    val camera: CameraSystem
    val ventilation: VentilationSystem

    constructor(game: FnafUGame) : this(DoorSystem(game), CameraSystem(game), VentilationSystem(game))

    private constructor(doorSystem: DoorSystem, cameraSystem: CameraSystem, ventilationSystem: VentilationSystem){
        this.door = doorSystem
        this.camera = cameraSystem
        this.ventilation = ventilationSystem
        all = listOf(doorSystem, cameraSystem, ventilationSystem)
        menu = SystemsMenu()
        menu.setItem(cursorPosToAction.keys.first(), cursorItem)
    }

    override fun tick() {
        all.forEach { it.tick() }
    }

    fun cursorUp(menu: SystemsMenu) {
        if (isAnySystemBeingRepaired()) return
        val cursorIndex = getCursorItemIndex(menu)

        if (cursorIndex == 0){
            menu.setItem(cursorPosToAction.keys.toList()[cursorPosToAction.size-1], cursorItem)
        }else {
            menu.setItem(cursorPosToAction.keys.toList()[cursorIndex - 1], cursorItem)
        }
        menu.setItem(cursorPosToAction.keys.toList()[cursorIndex], null)
    }

    fun cursorDown(menu: SystemsMenu) {
        if (isAnySystemBeingRepaired()) return
        val cursorIndex = getCursorItemIndex(menu)

        if (cursorIndex == cursorPosToAction.size-1){
            menu.setItem(cursorPosToAction.keys.toList()[0], cursorItem)
        }else {
            menu.setItem(cursorPosToAction.keys.toList()[cursorIndex + 1], cursorItem)
        }
        menu.setItem(cursorPosToAction.keys.toList()[cursorIndex], null)
    }

    fun enter(player: FnafUPlayer, menu: SystemsMenu) {
        if (isAnySystemBeingRepaired()) return
        cursorPosToAction.toList()[getCursorItemIndex(menu)].second.invoke(player)
    }

    private fun getCursorItemIndex(menu: SystemsMenu): Int {
        return cursorPosToAction.keys.indexOf(menu.inventory.first(cursorItem))
    }

    fun repairAll(player: FnafUPlayer) {
        if (isAnySystemBeingRepaired()) return
        menu.setItem(REBOOT_ALL_REPAIR_ICON_POSITION, Items.REBOOT_ICON)
        all.forEachIndexed { index, system ->
            system.startRepairing(player, menu, ALL_REPAIR_DURATION, false,index == 0)
        }
    }

    fun isAnySystemBeingRepaired(): Boolean {
        return all.count { it.isRepairing } > 0
    }

    fun exitMenu(player: FnafUPlayer) {
        playersInsideSystem.remove(player)
        player.regiveInventory()
    }

    fun openStation(player: FnafUPlayer) = openMenu(player, Items.EXIT_BUTTON.item)

    fun openTablet(player: FnafUPlayer) = openMenu(player, Items.SYSTEM_TABLET.item)

    private fun openMenu(player: FnafUPlayer, exitItem: ItemStack) {
        val inventory = player.player.inventory
        for (i in upButtons) inventory.setItem(i, Items.UP_BUTTON.item)
        for (i in downButtons) inventory.setItem(i, Items.DOWN_BUTTON.item)
        for (i in enterButtons) inventory.setItem(i, Items.ENTER_BUTTON.item)

        exitItem.setData(DataComponentTypes.BUNDLE_CONTENTS, BundleContents.bundleContents())
        exitItem.setData(DataComponentTypes.TOOLTIP_DISPLAY, TooltipDisplay.tooltipDisplay().hideTooltip(true))
        exitItem.setData(DataComponentTypes.ITEM_NAME, Component.empty())
        object : BukkitRunnable() {
            override fun run() {
                for (i in 0..8) inventory.setItem(i, exitItem)
            }
        }.runTaskLater(FnafU.instance, 1)

        playersInsideSystem.add(player)
        menu.open(player.player)
    }

    override fun reset() {
        all.forEach { it.reset() }
        for (player in ArrayList<FnafUPlayer>(playersInsideSystem)) {
            exitMenu(player)
            player.player.closeInventory()
        }
    }
}