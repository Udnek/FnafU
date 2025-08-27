package me.udnek.fnafu.misc

import me.udnek.fnafu.FnafU
import me.udnek.fnafu.player.FnafUPlayer
import org.bukkit.Location
import org.bukkit.block.data.BlockData
import org.bukkit.scheduler.BukkitRunnable

class FakeBlock {
    constructor(players: List<FnafUPlayer>, location: Location, blockData: BlockData, duration: Long) {
        val location = location.clone()
        players.forEach { it.player.sendBlockChange(location, blockData) }
        object : BukkitRunnable() {
            override fun run() { players.forEach { it.player.sendBlockChange(location, location.block.blockData) } }
        }.runTaskLater(FnafU.instance, duration)
    }
}