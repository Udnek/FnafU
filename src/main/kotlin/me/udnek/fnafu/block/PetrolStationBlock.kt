package me.udnek.fnafu.block

import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class PetrolStationBlock  : FnafUBlock("petrol_station") {
    override fun getFakeDisplay(): ItemStack = ItemStack(Material.REDSTONE_BLOCK)
}