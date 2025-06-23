package me.udnek.fnafu.mechanic

import me.udnek.fnafu.game.EnergyGame
import me.udnek.fnafu.util.Resettable
import me.udnek.fnafu.util.Ticking

class Energy(private val game: EnergyGame) : Resettable, Ticking {

    companion object {
        var MAX_ENERGY: Float = 100f
        var MIN_ENERGY: Float = 0f
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
        energy -= consumption
        if (energy < MIN_ENERGY) energy = MIN_ENERGY
        else if (energy > MAX_ENERGY) energy = MAX_ENERGY
    }

    fun updateConsumption() {
        updateUsage()
        consumption = usage / 5f
    }

    private fun updateUsage() {
        var closedAmount = 0
        for (doorButtonPair in game.systems.door.doors) {
            if (doorButtonPair.door.isClosed) closedAmount++
        }
        usage = closedAmount + 1
    }

    override fun reset() {
        consumption = 0f
        energy = MAX_ENERGY
        usage = 0
    }
}
