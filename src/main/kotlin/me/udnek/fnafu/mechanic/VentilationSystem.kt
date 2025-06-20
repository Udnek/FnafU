package me.udnek.fnafu.mechanic

import me.udnek.fnafu.game.EnergyGame
import me.udnek.fnafu.mechanic.system.System
import me.udnek.fnafu.util.Resettable
import me.udnek.fnafu.util.Ticking
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class VentilationSystem : Resettable, System, Ticking {

    override val game: EnergyGame
    override val sidebarPosition: Int = 1
    private var timeBroken: Int = 0

    companion object {
        const val TIME_BETWEEN_TICK: Int = 10
        const val FIRST_STAGE_TIME: Int = 20 * 20
        const val FIRST_STAGE_EFFECT_LEVEL: Int = 0
        const val SECOND_STAGE_TIME: Int = 40 * 20
        const val SECOND_STAGE_EFFECT_LEVEL: Int = 1

        fun getEffect(level: Int) : PotionEffect = PotionEffect(PotionEffectType.SLOWNESS, TIME_BETWEEN_TICK + 1, level, false, true, true)
    }

    constructor(game: EnergyGame) : super(34, "sidebar.fnafu.ventilation_system") {
        this.game = game
    }

    override fun tick() {
        if (!isBroken) {
            timeBroken = 0
            return
        }
        if (timeBroken >= SECOND_STAGE_TIME) {
            game.playerContainer.getSurvivors(false).forEach { it.player.addPotionEffect(getEffect(SECOND_STAGE_EFFECT_LEVEL)) }
        } else if (timeBroken >= FIRST_STAGE_TIME) {
            game.playerContainer.getSurvivors(false).forEach { it.player.addPotionEffect(getEffect(FIRST_STAGE_EFFECT_LEVEL)) }
        }
        timeBroken += TIME_BETWEEN_TICK
    }

    override fun reset() {}
}