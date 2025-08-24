package me.udnek.fnafu.mechanic

import me.udnek.coreu.mgu.Resettable
import me.udnek.fnafu.misc.Ticking

class Time(val maxTime: Int) : Resettable, Ticking {
    var ticks: Int = 0
        set(value) {
            field = Math.clamp(value.toLong(), 0, maxTime)
        }

    val isEnded: Boolean
        get() = ticks >= maxTime

    override fun tick() {
        ticks++
    }
    override fun reset() { ticks = 0 }
}
