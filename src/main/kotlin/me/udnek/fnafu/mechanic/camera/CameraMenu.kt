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
            inventory.setItem(camera.tabletMenuPosition, CameraButton.getWithCamera(camera, index))
        }
    }
    override fun onPlayerClicksItem(event: InventoryClickEvent) {
        event.isCancelled = true
        (event.whoClicked as Player).getFnafU()?.let {
            val camera = it.game.map.cameraSystem.getSpectatingCamera(it) ?:return
            inventory.setItem(camera.tabletMenuPosition, CameraButton.getWithCamera(camera, camera.number))
        }
        event.currentItem?.let {
            val id = it.getCameraId() ?: return

            val data = it.getData(DataComponentTypes.CUSTOM_MODEL_DATA) ?: return
            it.setData(DataComponentTypes.CUSTOM_MODEL_DATA, CustomModelData.customModelData().addFloats(data.floats()).addColor(Color.GREEN))

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
