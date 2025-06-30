package me.udnek.fnafu.entity.remnanttrap

import me.udnek.coreu.custom.entitylike.entity.ConstructableCustomEntity
import me.udnek.coreu.custom.entitylike.entity.CustomTickingEntityType
import me.udnek.fnafu.entity.EntityTypes
import me.udnek.fnafu.game.FnafUGame
import me.udnek.fnafu.player.FnafUPlayer
import org.bukkit.entity.BlockDisplay
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class RemnantTrap : ConstructableCustomEntity<BlockDisplay>() {

    companion object {
        const val FIND_RADIUS: Double = 10.0
        const val FIND_TIME = 2
    }

    lateinit var game: FnafUGame

    override fun getTickDelay() = 100

    override fun delayedTick() {
        game.findNearbyPlayers(entity.location, FIND_RADIUS, FnafUPlayer.Type.SURVIVOR).forEach {
            it.player.addPotionEffect(PotionEffect(PotionEffectType.GLOWING, FIND_TIME, 0, false, false, false))
        }
    }

    override fun getType(): CustomTickingEntityType<*> = EntityTypes.REMNANT_TRAP
}