package me.udnek.fnafu.block.decor

import me.udnek.coreu.custom.entitylike.block.constructabletype.RotatableCustomBlockType
import me.udnek.coreu.custom.item.CustomItem
import me.udnek.fnafu.item.Items
import org.bukkit.Material

class ArcadeMachineBlock : RotatableCustomBlockType() {

    override fun getItem(): CustomItem = Items.ARCADE_MACHINE

    override fun getBreakSpeedBaseBlock(): Material = Material.IRON_BLOCK

    override fun getRawId(): String = "arcade_machine"
}