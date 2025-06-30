package me.udnek.fnafu.mechanic

import me.udnek.coreu.mgu.Resettable
import me.udnek.fnafu.game.FnafUGame
import me.udnek.fnafu.util.Ticking

class Energy(private val game: FnafUGame) : Resettable, Ticking {

    companion object {
        const val MAX_ENERGY: Float = 100f
        const val MIN_ENERGY: Float = 0f
        const val TICKRATE = 15
    }

    var energy: Float = MAX_ENERGY
        private set
    var consumption: Float = 0f
        private set
    var usage = 0
        private set

    val isEndedUp: Boolean
        get() = energy <= MIN_ENERGY

    override fun tick() {
        energy -= consumption * TICKRATE
        energy = Math.clamp(energy, MIN_ENERGY, MAX_ENERGY)
    }

    fun updateConsumption() {
        updateUsage()
        consumption = (usage / 20f) * 0.25f
    }

    private fun updateUsage() {
        usage = game.systems.door.doors.count{it.door.isClosed} + 1
    }

    override fun reset() {
        consumption = 0f
        energy = MAX_ENERGY
        usage = 0
    }
}
