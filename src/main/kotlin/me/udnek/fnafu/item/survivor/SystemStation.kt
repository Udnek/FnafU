package me.udnek.fnafu.item.survivor

import me.udnek.coreu.custom.component.instance.BlockPlacingItem
import me.udnek.coreu.custom.item.ConstructableCustomItem
import me.udnek.coreu.custom.item.CustomItemProperties
import me.udnek.coreu.rpgu.component.RPGUComponents
import me.udnek.fnafu.block.Blocks
import me.udnek.fnafu.component.survivor.SystemTabletAbility
import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.NamespacedKey

class SystemStation: ConstructableCustomItem() {

    override fun getRawId(): String = "system_station"

    override fun getMaterial(): Material = DEFAULT_MATERIAL_FOR_BLOCK_PLACEABLE

    override fun getItemModel(): CustomItemProperties.DataSupplier<Key?> {
        return CustomItemProperties.DataSupplier.of(NamespacedKey(this.key.namespace, "system/station"))
    }

    override fun initializeComponents() {
        super.initializeComponents()
        components.set(BlockPlacingItem(Blocks.SYSTEM_STATION))
    }
}