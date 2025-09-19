package me.udnek.fnafu.entity.remnanttrap

import me.udnek.coreu.custom.entitylike.entity.ConstructableCustomEntity
import me.udnek.coreu.custom.entitylike.entity.CustomTickingEntityType
import me.udnek.coreu.rpgu.component.RPGUComponents
import me.udnek.fnafu.component.animatronic.freddy.FreddyTrapData
import me.udnek.fnafu.entity.EntityTypes
import me.udnek.fnafu.misc.Utils
import me.udnek.fnafu.player.FnafUPlayer
import org.bukkit.Color
import org.bukkit.Particle
import org.bukkit.entity.Item

class RemnantTrap : ConstructableCustomEntity<Item>() {

    companion object {
        const val SCAN_DURATION = 20
    }

    lateinit var data: FreddyTrapData
    override fun getTickDelay() = 50

    override fun delayedTick() {
        if (!this::data.isInitialized) return

        val radius = data.ability!!.components.getOrDefault(RPGUComponents.ABILITY_AREA_OF_EFFECT).get(data.player!!.player)
        data.player!!.game.findNearbyPlayers(
            entity.location,
            radius,
            FnafUPlayer.Type.SURVIVOR
        ).forEach {
                it.showAuraTo(data.player!!.game.playerContainer.animatronics, SCAN_DURATION)
        }
        Utils.spawnDustCircle(Particle.DUST.builder().color(Color.AQUA).location(entity.location).receivers(data.player!!.player), radius, 0.1,
            {return@spawnDustCircle !it.block.isSolid})
    }

    override fun getType(): CustomTickingEntityType<*> = EntityTypes.REMNANT_TRAP
}