package me.udnek.fnafu.item.decor

import me.udnek.coreu.custom.entitylike.block.CustomBlockType
import me.udnek.coreu.custom.item.CustomItemProperties
import me.udnek.fnafu.item.FnafUBlockItem
import net.kyori.adventure.key.Key
import org.bukkit.NamespacedKey

open class FnafUBlockDecorItem(rawID: String, block: CustomBlockType) : FnafUBlockItem(rawID, block) {

    override fun getItemModel(): CustomItemProperties.DataSupplier<Key?> {
        return CustomItemProperties.DataSupplier.of(NamespacedKey(this.key.namespace, "decor/$rawId"))
    }

}