package me.udnek.fnafu.block

import me.udnek.coreu.custom.entitylike.block.constructabletype.RotatableCustomBlockType
import me.udnek.coreu.custom.item.CustomItem
import me.udnek.fnafu.item.Items
import org.bukkit.Material

class GeneratorBlock : RotatableCustomBlockType() {

    override fun getItem(): CustomItem = Items.GENERATOR

    override fun getBreakSpeedBaseBlock(): Material = Material.IRON_BLOCK

    override fun getRawId(): String = "generator"
}
