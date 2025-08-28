package me.udnek.fnafu.misc

import me.udnek.fnafu.FnafU
import me.udnek.fnafu.player.FnafUPlayer
import org.bukkit.Location
import org.bukkit.block.Block
import org.bukkit.block.data.BlockData
import org.bukkit.scheduler.BukkitRunnable

class FakeBlock(players: List<FnafUPlayer>, location: Location, blockData: BlockData, duration: Long) {

    companion object{
        val fakes: MutableMap<Block, FakeBlock> = HashMap()
    }

    val task: BukkitRunnable

    init {
        val block = location.block
        players.forEach { it.player.sendBlockChange(block.location, blockData) }
        task = object : BukkitRunnable() {
            override fun run() { players.forEach { it.player.sendBlockChange(block.location, block.blockData) } }
        }.also { it.runTaskLater(FnafU.instance, duration) }

        fakes[block]?.task?.cancel()
        fakes[block] = this
    }
}