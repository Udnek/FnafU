package me.udnek.fnafu.item

import io.papermc.paper.datacomponent.item.CustomModelData
import me.udnek.fnafu.FnafU
import me.udnek.fnafu.component.survivor.TabletComponent
import me.udnek.itemscoreu.customitem.ConstructableCustomItem
import me.udnek.itemscoreu.customitem.CustomItemProperties
import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Color
import org.bukkit.NamespacedKey

class FullCameraTablet : ConstructableCustomItem() {

    override fun getCustomModelData(): CustomItemProperties.DataSupplier<CustomModelData> {
        return CustomItemProperties.DataSupplier.of(CustomModelData.customModelData().addColor(Color.GREEN).build())
    }

    override fun initializeComponents() {
        super.initializeComponents()
        components.set(TabletComponent("main_entrance", Color.GREEN, NamedTextColor.GREEN, false))
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

    override fun getCustomModelData(): CustomItemProperties.DataSupplier<CustomModelData> {
        return CustomItemProperties.DataSupplier.of(CustomModelData.customModelData().addColor(Color.WHITE).build())
    }

    override fun initializeComponents() {
        super.initializeComponents()
        components.set(TabletComponent("main_entrance", Color.WHITE, NamedTextColor.WHITE, true))
    }

    override fun getRawId(): String = "cut_camera_tablet"

    override fun getItemName(): CustomItemProperties.DataSupplier<Component> {
        return CustomItemProperties.DataSupplier.of(Component.translatable("item.fnafu.camera_tablet"))
    }

    override fun getItemModel(): CustomItemProperties.DataSupplier<Key> {
        return CustomItemProperties.DataSupplier.of(NamespacedKey(FnafU.instance, "camera_tablet"))
    }
}
