package me.udnek.fnafu.block

import me.udnek.coreu.custom.entitylike.block.constructabletype.DisplayBasedConstructableBlockType
import me.udnek.coreu.custom.item.CustomItem
import me.udnek.coreu.custom.registry.CustomRegistries
import me.udnek.coreu.util.Either
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.loot.LootTable

open class FnafUBlock(val rawID: String) : DisplayBasedConstructableBlockType(){
    override fun getLoot(): Either<LootTable?, List<ItemStack?>?>? = null
    override fun getItem(): CustomItem? = CustomRegistries.ITEM.getOrException(id)
    override fun getRawId(): String = rawID
    override fun getBreakSpeedBaseBlock(): Material = Material.IRON_BLOCK
}
