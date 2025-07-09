package me.udnek.fnafu.mechanic

import me.udnek.coreu.mgu.Resettable
import me.udnek.fnafu.game.FnafUGame
import me.udnek.fnafu.misc.Ticking

class Energy(private val game: FnafUGame) : Resettable, Ticking {

    companion object {
        const val MAX_ENERGY: Float = 100f
        const val MIN_ENERGY: Float = 0f
        const val TICKRATE = 15
    }

    var endedUpAlreadyChecked = false

    var energy: Float = MAX_ENERGY
        set(value){ field = Math.clamp(value, 0f, MAX_ENERGY)}

    var consumption: Float = 0f
        private set
    var usage = 0
        private set

    val isEndedUp: Boolean
        get() = energy <= MIN_ENERGY

    override fun tick() {
        energy -= consumption * TICKRATE
        energy = Math.clamp(energy, MIN_ENERGY, MAX_ENERGY)
        if (energy > 0) endedUpAlreadyChecked = false
    }

    fun updateConsumption() {
        updateUsage()
        consumption = (usage / 20f) * 0.35f
    }

    private fun updateUsage() {
        usage = game.systems.door.doors.count{it.door.isClosed} + 1
    }

    override fun reset() {
        consumption = 0f
        energy = MAX_ENERGY
        usage = 0
        endedUpAlreadyChecked = false
    }
}
