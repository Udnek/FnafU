package me.udnek.fnafu.item

import me.udnek.fnafu.FnafU
import me.udnek.fnafu.component.CameraComponent
import me.udnek.fnafu.component.Components
import me.udnek.itemscoreu.customitem.ConstructableCustomItem
import me.udnek.itemscoreu.customitem.CustomItemProperties
import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Color
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack

class FullCameraTablet : ConstructableCustomItem() {

    companion object {
        fun getWithColor(): ItemStack {
            return Items.FULL_CAMERA_TABLET.components.getOrDefault(Components.CAMERA_COMPONENT).changeTabletColor(Items.FULL_CAMERA_TABLET.item)
        }
    }

    override fun initializeComponents() {
        super.initializeComponents()
        components.set(CameraComponent("main", Color.GREEN, Color.GREEN, NamedTextColor.GREEN))
    }

    override fun getRawId(): String = "full_camera_tablet"

    override fun getItemName(): CustomItemProperties.DataSupplier<Component> {
        return CustomItemProperties.DataSupplier.of(Component.translatable("item.fnafu.camera_tablet"))
    }

    override fun getItemModel(): CustomItemProperties.DataSupplier<Key> {
        return CustomItemProperties.DataSupplier.of(NamespacedKey(FnafU.instance, "camera_tablet"))
    }
}

class CutCameraTablet : ConstructableCustomItem() {
    companion object {
        fun getWithColor(): ItemStack{
            return Items.CUT_CAMERA_TABLET.components.getOrDefault(Components.CAMERA_COMPONENT).changeTabletColor(Items.CUT_CAMERA_TABLET.item)
        }
    }

    override fun initializeComponents() {
        super.initializeComponents()
        components.set(CameraComponent("main", Color.WHITE, Color.WHITE, NamedTextColor.WHITE))
    }

    override fun getRawId(): String = "cut_camera_tablet"

    override fun getItemName(): CustomItemProperties.DataSupplier<Component> {
        return CustomItemProperties.DataSupplier.of(Component.translatable("item.fnafu.camera_tablet"))
    }

    override fun getItemModel(): CustomItemProperties.DataSupplier<Key> {
        return CustomItemProperties.DataSupplier.of(NamespacedKey(FnafU.instance, "camera_tablet"))
    }
}
