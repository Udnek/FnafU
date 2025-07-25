package me.udnek.fnafu.mechanic.system.door

import io.papermc.paper.datacomponent.DataComponentTypes
import me.udnek.coreu.custom.inventory.ConstructableCustomInventory
import me.udnek.coreu.util.ComponentU
import me.udnek.fnafu.item.survivor.doorman.DoormanTabletButton
import me.udnek.fnafu.misc.getFnafU
import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent

class DoorMenu : ConstructableCustomInventory {
    private val title: Component
    private val doors: List<ButtonDoorPair>
    private val tabletBackground =
        ComponentU.textWithNoSpaceSpaceFont(-8, Component.text("1").font(Key.key("fnafu:doorman")).color(TextColor.color(1f, 1f, 1f)), 175)

    constructor(title: Component, doors: List<ButtonDoorPair>) : super() {
        this.title = tabletBackground.append(title.color(NamedTextColor.BLACK))
        this.doors = doors
        updateDoorIcons()
    }

    fun updateDoorIcons() {
        doors.forEachIndexed { index, door ->
            inventory.setItem(door.door.tabletMenuPosition, DoormanTabletButton.getWithCamera(door.door, index))
        }
    }

    override fun onPlayerClicksItem(event: InventoryClickEvent) {
        event.isCancelled = true
        val door = doors[event.currentItem?.getData(DataComponentTypes.CUSTOM_MODEL_DATA)?.floats()[0]?.toInt() ?: return].door
        (event.whoClicked as Player).getFnafU()?.let {
            it.game.systems.door.onPlayerClickButtonInMenu(door, it)
        }
        updateDoorIcons()
    }

    override fun onPlayerClosesInventory(event: InventoryCloseEvent) {
        (event.player as Player).getFnafU()?.let { it.game.systems.door.onMenuExit(it) }
    }

    override fun getTitle(): Component = title
    override fun getInventorySize(): Int = 9 * 6
}