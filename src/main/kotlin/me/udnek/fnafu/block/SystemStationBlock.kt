package me.udnek.fnafu.block

import me.udnek.coreu.custom.entitylike.block.CustomBlockPlaceContext
import me.udnek.coreu.custom.entitylike.block.constructabletype.RotatableCustomBlockType
import me.udnek.coreu.custom.item.CustomItem
import me.udnek.coreu.nms.Nms
import me.udnek.coreu.util.Either
import me.udnek.fnafu.FnafU
import me.udnek.fnafu.item.Items
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.TileState
import org.bukkit.inventory.ItemStack
import org.bukkit.loot.LootTable
import org.bukkit.scheduler.BukkitRunnable

class SystemStationBlock : RotatableCustomBlockType() {
    override fun getItem(): CustomItem = Items.SYSTEM_STATION

    override fun getLoot(): Either<LootTable?, List<ItemStack?>?>? = null

    override fun getBreakSpeedBaseBlock(): Material = Material.IRON_BLOCK

    override fun getRawId(): String = "system_station"

    override fun load(p0: TileState) {}

    override fun unload(p0: TileState) {}

    override fun place(location: Location, context: CustomBlockPlaceContext) {
        super.place(location, context)
        object : BukkitRunnable(){
            override fun run() {
                val blockState = location.block.state
                location.getNearbyPlayers(16*10.0).forEach {
                    Nms.get().sendBlockUpdatePacket(it, blockState)
                }
            }
        }.runTaskLater(FnafU.instance, 20*3)
    }
}