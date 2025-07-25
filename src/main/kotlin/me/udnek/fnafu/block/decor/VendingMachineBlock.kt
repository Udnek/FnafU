package me.udnek.fnafu.block.decor

import me.udnek.coreu.custom.entitylike.block.constructabletype.RotatableCustomBlockType
import me.udnek.coreu.custom.item.CustomItem
import me.udnek.coreu.util.Either
import me.udnek.fnafu.item.Items
import org.bukkit.Material
import org.bukkit.block.TileState
import org.bukkit.inventory.ItemStack
import org.bukkit.loot.LootTable

class VendingMachineBlock : RotatableCustomBlockType() {
    override fun getItem(): CustomItem = Items.VENDING_MACHINE

    override fun getLoot(): Either<LootTable?, List<ItemStack?>?>? = null

    override fun getBreakSpeedBaseBlock(): Material = Material.IRON_BLOCK

    override fun getRawId(): String = "vending_machine"

    override fun load(p0: TileState) {}

    override fun unload(p0: TileState) {}

}