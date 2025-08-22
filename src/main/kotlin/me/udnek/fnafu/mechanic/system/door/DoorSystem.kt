package me.udnek.fnafu.mechanic.system.door

import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.datacomponent.item.BundleContents
import io.papermc.paper.datacomponent.item.TooltipDisplay
import me.udnek.fnafu.FnafU
import me.udnek.fnafu.component.FnafUComponents
import me.udnek.fnafu.component.survivor.tablet.DoormanTabletAbility
import me.udnek.fnafu.game.FnafUGame
import me.udnek.fnafu.item.Items
import me.udnek.fnafu.mechanic.system.AbstractSystem
import me.udnek.fnafu.mechanic.system.door.door.Door
import me.udnek.fnafu.mechanic.system.doorlike.DoorLike
import me.udnek.fnafu.player.FnafUPlayer
import net.kyori.adventure.text.Component
import org.bukkit.scheduler.BukkitRunnable

class DoorSystem : AbstractSystem {

    override val sidebarPosition: Int = 3
    val doors: MutableList<ButtonDoorPair>
    override var guiSlot: Int = 16
    override var sidebarLine: Component = Component.translatable("system.fnafu.door")
    private val menu: DoorMenu

    constructor(game: FnafUGame) : super(game){
        doors = ArrayList(game.map.doors)
        menu = DoorMenu(game.map.mapImage, doors)
    }

    override fun tick() {}

    fun openMenu(player: FnafUPlayer) {
        menu.open(player.player)
        object : BukkitRunnable() {
            override fun run() {
                val doorTablet = Items.DOOR_TABLET.item
                doorTablet.setData(DataComponentTypes.BUNDLE_CONTENTS, BundleContents.bundleContents().build())
                doorTablet.setData(DataComponentTypes.TOOLTIP_DISPLAY, TooltipDisplay.tooltipDisplay().hideTooltip(true))
                doorTablet.setData(DataComponentTypes.ITEM_NAME, Component.empty())
                for (i in 0..8) player.player.inventory.setItem(i, doorTablet)
            }
        }.runTaskLater(FnafU.instance, 1)
    }

    fun onMenuExit(player: FnafUPlayer) {
        player.regiveInventory()
    }

    fun updateDoorMenu() {
        menu.updateDoorIcons()
    }

    fun onPlayerClickButtonInMenu(door: Door, player: FnafUPlayer) {
        if (game.energy.isEndedUp) {
            player.player.closeInventory()
            return
        }

        if (door.isLocked) return
        door.toggle()
        game.updateEnergy()
        player.player.closeInventory()
        durability -= DoormanTabletAbility.DAMAGE_PER_USAGE
        game.applyForEveryAbility { component, player, item ->
            component.components.get(FnafUComponents.DOORMAN_TABLET_ABILITY)?.onPlayerClickButton(item, player)
        }
    }

    override fun reset() {
        super.reset()
        doors.forEach { it.reset() }
    }
}