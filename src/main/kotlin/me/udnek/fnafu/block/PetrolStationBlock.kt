package me.udnek.fnafu.block

import me.udnek.coreu.custom.item.CustomItem
import me.udnek.fnafu.item.Items
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class PetrolStationBlock  : FnafUBlock("petrol_station") {
    override fun getItem(): CustomItem = Items.PETROL_STATION
    override fun getFakeDisplay(): ItemStack = ItemStack(Material.REDSTONE_BLOCK)
}