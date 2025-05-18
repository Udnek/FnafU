package me.udnek.fnafu.mechanic.camera

import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.datacomponent.item.CustomModelData
import me.udnek.fnafu.item.CameraButton
import me.udnek.fnafu.util.getCameraId
import me.udnek.fnafu.util.getFnafU
import me.udnek.itemscoreu.custominventory.ConstructableCustomInventory
import net.kyori.adventure.text.Component
import org.bukkit.Color
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent

open class CameraMenu : ConstructableCustomInventory{
    private var title: Component

    constructor(cameras: List<Camera>, title: Component) : super() {
        this.title = title
        for ((index, camera) in cameras.withIndex()) {
            val item = CameraButton.getWithCamera(camera, index)
            item.setData(DataComponentTypes.CUSTOM_MODEL_DATA,
                CustomModelData.customModelData().addFloat(index.toFloat()).addColor(Color.BLACK))
            inventory.setItem(camera.tabletMenuPosition, item)
        }
    }
    override fun onPlayerClicksItem(event: InventoryClickEvent) {
        event.isCancelled = true
        event.currentItem?.let {
            val id = it.getCameraId() ?: return

            (event.whoClicked as Player).getFnafU()?.let {
                it.game.cameraSystem.spectateCamera(it, id, it.player.inventory.getItem(1)!!)
            }
        }
    }

    override fun onPlayerClosesInventory(event: InventoryCloseEvent) {
        (event.player as Player).getFnafU()?.let { it.game.cameraSystem.exitCamera(it) }
    }

    override fun getTitle(): Component? { return title }
    override fun getInventorySize(): Int { return 9 * 6 }
}
