package me.udnek.fnafu.component

import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.datacomponent.item.CustomModelData
import me.udnek.fnafu.item.Items
import me.udnek.fnafu.util.getFnafU
import me.udnek.itemscoreu.customcomponent.CustomComponent
import me.udnek.itemscoreu.customcomponent.CustomComponentType
import me.udnek.itemscoreu.customitem.CustomItem
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor
import org.bukkit.Color
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack

open class TabletComponent : CustomComponent<CustomItem> {
    val startCameraID: String
    val guiColor: Color
    val noiseColor: TextColor
    val isCut: Boolean

    constructor(startCameraID: String, guiColor: Color, noiseColor: TextColor, isCut: Boolean){
        this.startCameraID = startCameraID
        this.guiColor = guiColor
        this.noiseColor = noiseColor
        this.isCut = isCut
    }

    companion object {
        val EMPTY: TabletComponent = object : TabletComponent("",Color.WHITE, NamedTextColor.WHITE, false) {
            override fun onRightClick(customItem: CustomItem, event: PlayerInteractEvent) {  }
            override fun createGui(): ItemStack { throw RuntimeException("TabletComponent is default! Gui") }
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
            event.player.getFnafU()?.let { player ->
                player.game.cameraSystem.let { system ->
                    if (system.isBroken()) {
                        player.showNoise(noiseColor)
                        return
                    }
                    system.openMenu(player, itemStack)
                    system.spectateCamera(player, startCameraID, itemStack)
                }
            }
        }
    }

    override fun getType(): CustomComponentType<out CustomItem, out CustomComponent<CustomItem>> {
        return Components.TABLET_COMPONENT
    }
}