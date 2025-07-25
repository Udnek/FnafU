package me.udnek.fnafu.mechanic.system.ventilation

import me.udnek.fnafu.game.FnafUGame
import me.udnek.fnafu.mechanic.system.AbstractSystem
import net.kyori.adventure.text.Component
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class VentilationSystem(game: FnafUGame) : AbstractSystem(game) {

    override val sidebarPosition: Int = 1
    private var timeBroken: Int = 0
    override var guiSlot: Int = 34
    override var sidebarLine: Component = Component.translatable("system.fnafu.ventilation")

    companion object {
        const val TIME_BETWEEN_TICK: Int = 10
        const val FIRST_STAGE_TIME: Int = 20 * 20
        const val FIRST_STAGE_EFFECT_LEVEL: Int = 0
        const val SECOND_STAGE_TIME: Int = 40 * 20
        const val SECOND_STAGE_EFFECT_LEVEL: Int = 1

        fun getEffect(level: Int) : PotionEffect =
            PotionEffect(PotionEffectType.SLOWNESS, TIME_BETWEEN_TICK + 1, level, false, true, true)
    }

    override fun tick() {
        if (!isBroken) {
            timeBroken = 0
            return
        }
        if (timeBroken >= SECOND_STAGE_TIME) {
            game.playerContainer.aliveSurvivors.forEach { it.player.addPotionEffect(getEffect(SECOND_STAGE_EFFECT_LEVEL)) }
        } else if (timeBroken >= FIRST_STAGE_TIME) {
            game.playerContainer.aliveSurvivors.forEach { it.player.addPotionEffect(getEffect(FIRST_STAGE_EFFECT_LEVEL)) }
        }
        timeBroken += TIME_BETWEEN_TICK
    }
}