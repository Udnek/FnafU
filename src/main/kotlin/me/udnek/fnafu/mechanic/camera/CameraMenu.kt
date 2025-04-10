package me.udnek.fnafu.mechanic.camera

import me.udnek.fnafu.item.CameraButton
import me.udnek.fnafu.util.getCameraId
import me.udnek.fnafu.util.getFnafU
import me.udnek.itemscoreu.custominventory.ConstructableCustomInventory
import net.kyori.adventure.text.Component
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent

open class CameraMenu : ConstructableCustomInventory{
    private var title: Component

    constructor(cameras: List<Camera>, title: Component) : super() {
        this.title = title
        for (camera in cameras) {
            inventory.setItem(camera.tabletMenuPosition, CameraButton.getWithCamera(camera))
        }
    }

    override fun onPlayerClicksItem(event: InventoryClickEvent) {
        event.isCancelled = true
        event.currentItem?.let {
            val id = event.currentItem?.getCameraId() ?: return
            (event.whoClicked as Player).getFnafU()?.also {
                it.game.map.cameraSystem.spectateCamera(it, id)
            }
        }
    }

    override fun onPlayerClosesInventory(event: InventoryCloseEvent) {
        (event.player as Player).getFnafU()?.also { it.game.map.cameraSystem.exitCamera(it) }
    }

    override fun getTitle(): Component? { return title }
    override fun getInventorySize(): Int { return 9 * 6 }
}
