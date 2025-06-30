package me.udnek.fnafu.sound

import me.udnek.coreu.custom.sound.CustomSound
import me.udnek.fnafu.FnafU
import me.udnek.fnafu.player.FnafUPlayer
import org.bukkit.scheduler.BukkitRunnable
import java.time.Duration

class LoopedSound(val sound: CustomSound, val loopTime: Duration) {
    var task: BukkitRunnable? = null

    fun loop(action: (CustomSound) -> Unit) {
        task = object : BukkitRunnable() {
            override fun run() = action.invoke(sound)
        }
        task!!.runTaskTimer(FnafU.instance, 0, loopTime.seconds*20)
    }

    fun stop(players: List<FnafUPlayer>) {
        task?.cancel()
        players.forEach { sound.stop(it.player) }
    }
}