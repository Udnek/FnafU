package me.udnek.fnafu.mechanic.camera

import me.udnek.coreu.custom.inventory.ConstructableCustomInventory
import me.udnek.coreu.rpgu.component.RPGUComponents
import me.udnek.fnafu.component.FnafUComponents
import me.udnek.fnafu.item.survivor.CameraButton
import me.udnek.fnafu.util.getCameraId
import me.udnek.fnafu.util.getCustom
import me.udnek.fnafu.util.getFnafU
import net.kyori.adventure.text.Component
import org.bukkit.Color
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.ItemStack

open class CameraMenu : ConstructableCustomInventory{
    private var title: Component

    constructor(cameras: List<Camera>, title: Component, cameraTablet: ItemStack) : super() {
        this.title = title
        updateCameras(cameras, null, cameraTablet)
    }

    fun updateCameras(cameras: List<Camera>, useCamera: Camera?, cameraTablet: ItemStack) {
        val isCutTublet = cameraTablet.getCustom()?.components?.get(RPGUComponents.ACTIVE_ABILITY_ITEM)?.components?.get(FnafUComponents.CAMERA_TABLET_ABILITY)?.isCut ?: return
        for (camera in cameras) {
            if (isCutTublet && !camera.isCut) continue
            if (camera == useCamera) {
                inventory.setItem(useCamera.tabletMenuPosition, CameraButton.getWithCamera(useCamera, useCamera.number, Color.GREEN)) }
            else { inventory.setItem(camera.tabletMenuPosition, CameraButton.getWithCamera(camera, camera.number, Color.BLACK)) }
        }
    }

    override fun onPlayerClicksItem(event: InventoryClickEvent) {
        event.isCancelled = true
        event.currentItem?.let {
            val id = it.getCameraId() ?: return

            (event.whoClicked as Player).getFnafU()?.let {
                it.game.systems.camera.spectateCamera(it, id, it.player.inventory.getItem(1)!!)
            }
        }
    }

    override fun onPlayerClosesInventory(event: InventoryCloseEvent) {
        (event.player as Player).getFnafU()?.let { it.game.systems.camera.exitCamera(it) }
    }

    override fun getTitle(): Component? { return title }
    override fun getInventorySize(): Int { return 9 * 6 }
}
