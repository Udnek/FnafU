package me.udnek.fnafu.mechanic.system

import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.datacomponent.item.BundleContents
import io.papermc.paper.datacomponent.item.TooltipDisplay
import me.udnek.coreu.mgu.Resettable
import me.udnek.fnafu.FnafU
import me.udnek.fnafu.item.Items
import me.udnek.fnafu.mechanic.system.camera.CameraSystem
import me.udnek.fnafu.mechanic.system.door.DoorSystem
import me.udnek.fnafu.mechanic.system.ventilation.VentilationSystem
import me.udnek.fnafu.player.FnafUPlayer
import me.udnek.fnafu.misc.Ticking
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable

open class Systems : Resettable, Ticking {

    companion object {
        const val REBOOT_ALL_ICON_POSITION = 43
        const val SINGLE_REPAIR_DURATION = 8*10
        const val ALL_REPAIR_DURATION = 15*10
    }

    private val cursorPosition: HashMap<Int, (player: FnafUPlayer) -> Unit> = hashMapOf(
        9 to {player ->  door.startRepairing(player, menu, SINGLE_REPAIR_DURATION)},
        18 to {player ->  camera.startRepairing(player, menu, SINGLE_REPAIR_DURATION)},
        27 to {player ->  ventilation.startRepairing(player, menu, SINGLE_REPAIR_DURATION)},
        36 to {player ->  repairAll(player)})
    private val cursorItem = Items.CURSOR_ICON.item

    private val upButtons = listOf(9, 10, 18, 19, 27, 28)
    private val downButtons = listOf(11, 12, 20, 21, 29, 30)
    private val enterButtons = listOf(15, 16, 17, 23, 24, 25, 26, 32, 33, 34, 35)

    private val playerInsideSystem = ArrayList<FnafUPlayer>()
    var menu: SystemMenu
        protected set
    val all: List<System>
    val door: DoorSystem
    val camera: CameraSystem
    val ventilation: VentilationSystem

    constructor(doorSystem: DoorSystem, cameraSystem: CameraSystem, ventilationSystem: VentilationSystem){
        this.door = doorSystem
        this.camera = cameraSystem
        this.ventilation = ventilationSystem
        all = listOf(doorSystem, cameraSystem, ventilationSystem)
        menu = SystemMenu()
    }

    override fun tick() {
        all.forEach { it.tick() }
    }

    fun cursorUp(inventory: Inventory) {
        if (isAnyOfSystemsBeingRepaired()) return
        val indexCursorItem = getCursorItemIndex(inventory)

        if (indexCursorItem == 0){
            inventory.setItem(cursorPosition.keys.toList()[3], cursorItem)
        }else {
            inventory.setItem(cursorPosition.keys.toList()[indexCursorItem - 1], cursorItem)
        }
        inventory.setItem(cursorPosition.keys.toList()[indexCursorItem], ItemStack(Material.AIR))
    }

    fun cursorDown(inventory: Inventory) {
        if (isAnyOfSystemsBeingRepaired()) return
        val indexCursorItem = getCursorItemIndex(inventory)

        if (indexCursorItem == 3){
            inventory.setItem(cursorPosition.keys.toList()[0], cursorItem)
        }else {
            inventory.setItem(cursorPosition.keys.toList()[indexCursorItem + 1], cursorItem)
        }
        inventory.setItem(cursorPosition.keys.toList()[indexCursorItem], ItemStack(Material.AIR))
    }

    fun enter(player: FnafUPlayer, inventory: Inventory) {
        if (isAnyOfSystemsBeingRepaired()) return
        cursorPosition[cursorPosition.keys.toList()[getCursorItemIndex(inventory)]]?.invoke(player)
    }

    private fun getCursorItemIndex(inventory: Inventory): Int {
        return cursorPosition.keys.toList().indexOf(inventory.first(cursorItem))
    }

    fun repairAll(player: FnafUPlayer) {
        if (isAnyOfSystemsBeingRepaired()) return
        menu.inventory.setItem(REBOOT_ALL_ICON_POSITION, Items.REBOOT_ICON.item)
        all.forEach { it.startRepairing(player, menu, ALL_REPAIR_DURATION, false) }
    }

    fun isAnyOfSystemsBeingRepaired(): Boolean {
        all.forEach { if (it.isRepairing) return true }
        return false
    }

    fun exitMenu(player: FnafUPlayer) {
        if (playerInsideSystem.isEmpty()) return
        playerInsideSystem.remove(player)
        player.regiveInventory()
    }

    fun openMenu(player: FnafUPlayer) {
        val inventory = player.player.inventory
        for (i in upButtons) inventory.setItem(i, Items.UP_BUTTON.item)
        for (i in downButtons) inventory.setItem(i, Items.DOWN_BUTTON.item)
        for (i in enterButtons) inventory.setItem(i, Items.ENTER_BUTTON.item)

        val item = Items.SYSTEM_TABLET.item
        item.setData(DataComponentTypes.BUNDLE_CONTENTS, BundleContents.bundleContents())
        item.setData(DataComponentTypes.TOOLTIP_DISPLAY, TooltipDisplay.tooltipDisplay().hideTooltip(true))
        item.setData(DataComponentTypes.ITEM_NAME, Component.empty())
        object : BukkitRunnable() {
            override fun run() {
                for (i in 0..8) inventory.setItem(i, item)
            }
        }.runTaskLater(FnafU.instance, 1)

        playerInsideSystem.add(player)
        menu.open(player.player)
    }

    override fun reset() {
        all.forEach {
            it.reset()
        }

        for (player in ArrayList<FnafUPlayer>(playerInsideSystem)) {
            exitMenu(player)
            player.player.closeInventory()
        }
    }


}