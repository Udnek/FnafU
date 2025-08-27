package me.udnek.fnafu.block

import me.udnek.coreu.custom.entitylike.block.CustomBlockPlaceContext
import me.udnek.coreu.custom.entitylike.block.constructabletype.RotatableCustomBlockType
import me.udnek.coreu.custom.item.CustomItem
import me.udnek.coreu.nms.Nms
import me.udnek.fnafu.FnafU
import me.udnek.fnafu.item.Items
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.scheduler.BukkitRunnable

class SystemStationBlock : RotatableCustomBlockType() {
    override fun getItem(): CustomItem = Items.SYSTEM_STATION

    override fun getBreakSpeedBaseBlock(): Material = Material.IRON_BLOCK

    override fun getRawId(): String = "system_station"

    override fun internalPlace(location: Location, context: CustomBlockPlaceContext) {
        super.internalPlace(location, context)
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