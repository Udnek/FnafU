package me.udnek.fnafu.mechanic.door

import io.papermc.paper.datacomponent.DataComponentTypes
import me.udnek.coreu.custom.inventory.ConstructableCustomInventory
import me.udnek.fnafu.item.survivor.door.DoorTabletButton
import me.udnek.fnafu.util.getFnafU
import net.kyori.adventure.text.Component
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent

class DoorMenu : ConstructableCustomInventory {
    private val title: Component
    private val doors: List<ButtonDoorPair>

    constructor(title: Component, doors: List<ButtonDoorPair>) : super() {
        this.title = title
        this.doors = doors
        updateDoors()
    }

    fun updateDoors() {
        var i = 0
        for (door in doors) {
            inventory.setItem(door.door.tabletMenuPosition, DoorTabletButton.getWithCamera(door.door, i))
            i++
        }
    }

    override fun onPlayerClicksItem(event: InventoryClickEvent) {
        event.isCancelled = true
        event.currentItem?.let { doors[it.getData(DataComponentTypes.CUSTOM_MODEL_DATA)!!.floats()[0].toInt()].door.toggle() }
        updateDoors()
        (event.whoClicked as Player).getFnafU()?.also { it.game.systems.door.updateEnergy() }
    }

    override fun onPlayerClosesInventory(event: InventoryCloseEvent) {
        (event.player as Player).getFnafU()?.also { it.game.systems.door.exitSystem(it) }
    }

    override fun getTitle(): Component = title
    override fun getInventorySize(): Int = 9 * 6
}