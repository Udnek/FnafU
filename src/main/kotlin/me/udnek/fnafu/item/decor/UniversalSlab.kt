package me.udnek.fnafu.item.decor

import me.udnek.coreu.custom.item.CustomItemProperties
import me.udnek.fnafu.block.Blocks
import me.udnek.fnafu.item.FnafUBlockItem
import net.kyori.adventure.key.Key
import org.bukkit.Material
import org.bukkit.NamespacedKey

class UniversalSlab : FnafUBlockItem("universal_slab", Blocks.UNIVERSAL_SLAB) {
    override fun getMaterial(): Material = Material.CAMPFIRE

    override fun getItemModel(): CustomItemProperties.DataSupplier<Key?> {
        return CustomItemProperties.DataSupplier.of(NamespacedKey(this.key.namespace, "decor/universal_slab"))
    }
}