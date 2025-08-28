package me.udnek.fnafu.block

import me.udnek.coreu.custom.entitylike.block.constructabletype.DisplayBasedConstructableBlockType
import me.udnek.coreu.custom.item.CustomItem
import me.udnek.coreu.custom.registry.CustomRegistries
import org.bukkit.Material

open class FnafUBlock(val rawID: String) : DisplayBasedConstructableBlockType(){
    override fun getItem(): CustomItem? = CustomRegistries.ITEM.getOrException(id)
    override fun getRawId(): String = rawID
    override fun getBreakSpeedBaseBlock(): Material = Material.IRON_BLOCK
}
