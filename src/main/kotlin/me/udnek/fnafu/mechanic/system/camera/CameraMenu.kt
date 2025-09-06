package me.udnek.fnafu.mechanic.system.camera

import me.udnek.coreu.custom.equipmentslot.universal.BaseUniversalSlot
import me.udnek.coreu.custom.inventory.ConstructableCustomInventory
import me.udnek.coreu.rpgu.component.RPGUComponents
import me.udnek.coreu.util.Either
import me.udnek.fnafu.component.FnafUComponents
import me.udnek.fnafu.item.Items
import me.udnek.fnafu.item.survivor.camera.CameraButton
import me.udnek.fnafu.misc.getCameraId
import me.udnek.fnafu.misc.getCustom
import me.udnek.fnafu.misc.getFnafU
import me.udnek.fnafu.misc.play
import me.udnek.fnafu.sound.Sounds
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.ItemStack

open class CameraMenu : ConstructableCustomInventory{
    private var title: Component

    companion object{
        const val FLASH_POSITION = 17
    }

    constructor(cameras: List<Camera>, title: Component, cameraTablet: ItemStack) : super() {
        this.title = title.color(TextColor.color(1F, 1F, 1F))
        updateCameras(cameras, null, cameraTablet)
        setItem(FLASH_POSITION, Items.FLASH_BUTTON)
    }

    fun updateCameras(cameras: List<Camera>, useCamera: Camera?, cameraTablet: ItemStack) {
        val isCutTublet = cameraTablet.getCustom()?.components?.get(RPGUComponents.ACTIVE_ABILITY_ITEM)?.components?.get(FnafUComponents.CAMERA_TABLET_ABILITY)?.isCut ?: return
        for (camera in cameras) {
            if (isCutTublet && !camera.isInCutMenu){
                inventory.setItem(camera.tabletMenuPosition, ItemStack(Material.AIR))
            }
            else if (camera == useCamera) {
                inventory.setItem(camera.tabletMenuPosition, CameraButton.getWithCamera(useCamera, useCamera.number, Color.GREEN))
            }
            else {
                inventory.setItem(camera.tabletMenuPosition, CameraButton.getWithCamera(camera, camera.number, Color.BLACK))
            }
        }
    }

    override fun onPlayerClicksItem(event: InventoryClickEvent) {
        event.isCancelled = true
        val currentItem = event.currentItem ?: return
        val player = (event.whoClicked as Player).getFnafU() ?: return
        if (currentItem.getCustom() == Items.FLASH_BUTTON) {
            Items.FLASH_BUTTON.components.getOrDefault(RPGUComponents.ACTIVE_ABILITY_ITEM).components.getOrDefault(
                FnafUComponents.CAMERA_FLASH_ABILITY).activate(
                    Items.FLASH_BUTTON,
                    player.player,
                    Either(BaseUniversalSlot(FLASH_POSITION), null),
                    event
                )
            return
        }
        val id = currentItem.getCameraId() ?: return
        Sounds.CAMERA_SWITCH.play(player)
        player.game.systems.camera.spectateCamera(player, id, player.data.getOrDefault(FnafUComponents.SPECTATE_CAMERA_DATA).tablet!!)
    }

    override fun onPlayerClosesInventory(event: InventoryCloseEvent) {
        (event.player as Player).getFnafU()?.let { it.game.systems.camera.exitCamera(it) }
    }

    override fun getTitle(): Component? { return title }
    override fun getInventorySize(): Int { return 9 * 6 }
}







