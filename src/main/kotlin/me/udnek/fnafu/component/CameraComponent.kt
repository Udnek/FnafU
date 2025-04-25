package me.udnek.fnafu.component

import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.datacomponent.item.CustomModelData
import me.udnek.fnafu.item.Items
import me.udnek.fnafu.util.getFnafU
import me.udnek.itemscoreu.customcomponent.CustomComponent
import me.udnek.itemscoreu.customcomponent.CustomComponentType
import me.udnek.itemscoreu.customitem.CustomItem
import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.title.Title
import org.bukkit.Color
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import java.time.Duration

open class CameraComponent : CustomComponent<CustomItem> {
    val startCameraID: String
    val tabletColor: Color
    val guiColor: Color
    val titleColor: TextColor

    constructor(startCameraID: String, tabletColor: Color, guiColor: Color, titleColor: TextColor){
        this.startCameraID = startCameraID
        this.tabletColor = tabletColor
        this.guiColor = guiColor
        this.titleColor = titleColor
    }

    companion object {
        val EMPTY: CameraComponent = object : CameraComponent("",Color.WHITE, Color.WHITE, NamedTextColor.WHITE) {
            override fun onRightClick(customItem: CustomItem, event: PlayerInteractEvent) {}
            override fun createGui(): ItemStack {
                throw RuntimeException("CameraComponent is default! Gui")
                return super.createGui()
            }
            override fun changeTabletColor(itemStack: ItemStack): ItemStack {
                throw RuntimeException("CameraComponent is default!" + itemStack.getData(DataComponentTypes.ITEM_NAME))
                return super.changeTabletColor(itemStack)
            }
            override fun showTitle(player: Player) {
                throw RuntimeException("CameraComponent is default! Title")
            }
        }
    }

    open fun  changeTabletColor(itemStack: ItemStack) : ItemStack {
        itemStack.setData(DataComponentTypes.CUSTOM_MODEL_DATA, CustomModelData.customModelData().addColor(tabletColor))
        return itemStack
    }

    open fun createGui() : ItemStack {
        val item = Items.CAMERA_OVERLAY.item
        item.setData(DataComponentTypes.CUSTOM_MODEL_DATA, CustomModelData.customModelData().addColor(guiColor))
        return item
    }

    open fun showTitle(player: Player) {
        player.showTitle(Title.title(Component.text("3").font(Key.key("fnafu:camera")).color(titleColor), Component.empty(),
            Title.Times.times(Duration.ZERO, Duration.ofMillis(250), Duration.ZERO)))
    }

    open fun onRightClick(customItem: CustomItem, event: PlayerInteractEvent) {
        val player = event.player
        event.item?.let {
            player.getFnafU()?.let { player ->
                player.game.map.cameraSystem.let { system ->
                    system.spectateCamera(player, startCameraID)
                    system.openMenu(player)
                }
            }
        }
    }

    override fun getType(): CustomComponentType<out CustomItem, out CustomComponent<CustomItem>> {
        return Components.CAMERA_COMPONENT
    }
}