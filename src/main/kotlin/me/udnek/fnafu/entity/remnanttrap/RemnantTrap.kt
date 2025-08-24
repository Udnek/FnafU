package me.udnek.fnafu.entity.remnanttrap

import me.udnek.coreu.custom.entitylike.entity.ConstructableCustomEntity
import me.udnek.coreu.custom.entitylike.entity.CustomTickingEntityType
import me.udnek.fnafu.entity.EntityTypes
import me.udnek.fnafu.game.FnafUGame
import me.udnek.fnafu.player.FnafUPlayer
import org.bukkit.Color
import org.bukkit.Location
import org.bukkit.entity.Item

class RemnantTrap : ConstructableCustomEntity<Item>() {

    companion object {
        const val SCAN_RADIUS: Double = 10.0
        const val SCAN_TIME = 2
        const val SCAN_DELAY = 100
    }

    lateinit var game: FnafUGame
    lateinit var teleportLocation: Location

    override fun getTickDelay() = SCAN_DELAY

    override fun delayedTick() {
        if (!this::game.isInitialized) return
        game.findNearbyPlayers(entity.location, SCAN_RADIUS, FnafUPlayer.Type.SURVIVOR).forEach {
            it.showAuraTo(game.playerContainer.animatronics, SCAN_TIME, Color.GREEN)
        }
    }

    override fun getType(): CustomTickingEntityType<*> = EntityTypes.REMNANT_TRAP
}