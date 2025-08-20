package me.udnek.fnafu.item

import it.unimi.dsi.fastutil.ints.IntList
import me.udnek.coreu.custom.item.ConstructableCustomItem
import me.udnek.coreu.custom.item.CustomItemProperties
import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component
import org.bukkit.NamespacedKey

class MapIcon(val mapName: String) : ConstructableCustomItem(){
    override fun getRawId(): String = "map_icon_$mapName"
    override fun getItemModel(): CustomItemProperties.DataSupplier<Key> {
        return CustomItemProperties.DataSupplier.of(NamespacedKey(key.namespace,  "map_icon/$mapName"))
    }

    override fun getItemName(): CustomItemProperties.DataSupplier<Component> {
        return CustomItemProperties.DataSupplier.of(Component.translatable("map.${key.namespace}.${mapName}"))
    }
}
