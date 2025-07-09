package me.udnek.fnafu.item

import me.udnek.coreu.custom.item.ConstructableCustomItem
import me.udnek.coreu.custom.item.CustomItemProperties
import net.kyori.adventure.key.Key
import org.bukkit.Material

class FnafUVanillaBasedItem(val rawID: String, val base: Material) : ConstructableCustomItem(){
    override fun getRawId(): String = rawID
    override fun getMaterial(): Material = base
    override fun getItemModel(): CustomItemProperties.DataSupplier<Key?>? = null
}
