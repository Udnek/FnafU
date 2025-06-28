package me.udnek.fnafu.sound

import me.udnek.coreu.custom.sound.CustomSound
import me.udnek.fnafu.FnafU
import me.udnek.fnafu.player.FnafUPlayer
import org.bukkit.scheduler.BukkitRunnable

class LoopedSound(val sound: CustomSound, val loopTime: Long) {
    var task: BukkitRunnable? = null

    fun activate(action: (CustomSound) -> Unit) {
        task = object : BukkitRunnable() { override fun run() = action.invoke(sound) }
        task!!.runTaskTimer(FnafU.instance, 0, loopTime * 20)
    }

    fun stop(players: List<FnafUPlayer>) {
        players.forEach { sound.stop(it.player) }
        task?.cancel()
    }
}