package me.udnek.fnafu.component

import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.datacomponent.item.CustomModelData
import me.udnek.fnafu.item.Items
import me.udnek.fnafu.mechanic.KitMenu
import me.udnek.fnafu.util.getFnafU
import me.udnek.itemscoreu.customcomponent.CustomComponent
import me.udnek.itemscoreu.customcomponent.CustomComponentType
import me.udnek.itemscoreu.customitem.CustomItem
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor
import org.bukkit.Color
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack

open class CameraComponent : CustomComponent<CustomItem> {
    val startCameraID: String
    val guiColor: Color
    val noiseColor: TextColor

    constructor(startCameraID: String, guiColor: Color, noiseColor: TextColor){
        this.startCameraID = startCameraID
        this.guiColor = guiColor
        this.noiseColor = noiseColor
    }

    companion object {
        val EMPTY: CameraComponent = object : CameraComponent("",Color.WHITE, NamedTextColor.WHITE) {
            override fun onRightClick(customItem: CustomItem, event: PlayerInteractEvent) {}
            override fun createGui(): ItemStack {
                throw RuntimeException("CameraComponent is default! Gui")
                return super.createGui()
            }
        }
    }

    open fun createGui() : ItemStack {
        val item = Items.CAMERA_OVERLAY.item
        item.setData(DataComponentTypes.CUSTOM_MODEL_DATA, CustomModelData.customModelData().addColor(guiColor))
        return item
    }

    open fun onRightClick(customItem: CustomItem, event: PlayerInteractEvent) {
        event.item?.let {
            itemStack ->
            event.player.getFnafU()?.let { player ->KitMenu().open(player.player)
                player.game.cameraSystem.let { system ->
                    if (system.isBroken()) {
                        player.showNoise(noiseColor)
                        return
                    }
                    system.spectateCamera(player, startCameraID, itemStack)
                    system.openMenu(player)
                }
            }
        }
    }

    override fun getType(): CustomComponentType<out CustomItem, out CustomComponent<CustomItem>> {
        return Components.CAMERA_COMPONENT
    }
}