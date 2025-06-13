package me.udnek.fnafu.component.survivor

import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.datacomponent.item.CustomModelData
import me.udnek.coreu.custom.component.CustomComponent
import me.udnek.coreu.custom.component.CustomComponentType
import me.udnek.coreu.custom.item.CustomItem
import me.udnek.coreu.custom.item.CustomItemComponent
import me.udnek.fnafu.component.Components
import me.udnek.fnafu.item.Items
import me.udnek.fnafu.util.getFnafU
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor
import org.bukkit.Color
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack

open class TabletComponent : CustomItemComponent {
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
        val EMPTY: TabletComponent = object : TabletComponent("", Color.WHITE, NamedTextColor.WHITE, false) {
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
                player.game.systems.camera.let { system ->
                    if (system.isBroken) {
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