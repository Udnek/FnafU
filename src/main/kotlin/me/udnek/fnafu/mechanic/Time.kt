package me.udnek.fnafu.mechanic

import me.udnek.coreu.mgu.Resettable
import me.udnek.fnafu.misc.Ticking

class Time(private val maxTime: Int) : Resettable, Ticking {
    var ticks = 0
        private set

    val isEnded: Boolean
        get() = ticks >= maxTime

    override fun tick() {
        ticks++
    }
    override fun reset() { ticks = 0 }
}
