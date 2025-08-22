package me.udnek.fnafu.item.survivor.camera

import io.papermc.paper.datacomponent.item.CustomModelData
import me.udnek.coreu.custom.item.ConstructableCustomItem
import me.udnek.coreu.custom.item.CustomItemProperties
import me.udnek.coreu.rpgu.component.RPGUComponents
import me.udnek.fnafu.FnafU
import me.udnek.fnafu.component.survivor.tablet.CameraTabletAbility
import net.kyori.adventure.key.Key
import org.bukkit.Color
import org.bukkit.NamespacedKey

class FullCameraTablet : ConstructableCustomItem() {

    override fun getCustomModelData(): CustomItemProperties.DataSupplier<CustomModelData> {
        return CustomItemProperties.DataSupplier.of(CustomModelData.customModelData().addColor(Color.GREEN).build())
    }

    override fun initializeComponents() {
        super.initializeComponents()
        components.getOrCreateDefault(RPGUComponents.ACTIVE_ABILITY_ITEM).components.set(CameraTabletAbility.FULL)
    }

    override fun getRawId(): String = "full_camera_tablet"

    override fun getItemModel(): CustomItemProperties.DataSupplier<Key> {
        return CustomItemProperties.DataSupplier.of(NamespacedKey(FnafU.instance, "tablet/camera"))
    }
}

