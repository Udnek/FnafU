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
        for ((index, camera) in cameras.withIndex()) {
            inventory.setItem(camera.tabletMenuPosition, CameraButton.getWithCamera(camera, index))
        }
    }
    override fun onPlayerClicksItem(event: InventoryClickEvent) {
        event.isCancelled = true
        event.currentItem?.let {
            val id = it.getCameraId() ?: return

            (event.whoClicked as Player).getFnafU()?.also {
                it.game.map.cameraSystem.spectateCamera(it, id, event.inventory.getItem(1)!!)
            }
        }
    }

    override fun onPlayerClosesInventory(event: InventoryCloseEvent) {
        (event.player as Player).getFnafU()?.also { it.game.map.cameraSystem.exitCamera(it) }
    }

    override fun getTitle(): Component? { return title }
    override fun getInventorySize(): Int { return 9 * 6 }
}
