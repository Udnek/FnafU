package me.udnek.fnafu.game

import me.udnek.fnafu.map.FnafUMap
import me.udnek.fnafu.player.FnafUPlayer
import me.udnek.fnafu.util.Ticking
import me.udnek.itemscoreu.customminigame.game.MGUGameInstance
import org.bukkit.Location
import org.bukkit.event.entity.EntityDamageByEntityEvent

interface FnafUGame : MGUGameInstance, Ticking {
    val map: FnafUMap
    fun findNearbyPlayers(location: Location, radius: Float): List<FnafUPlayer>

    fun onPlayerDamagePlayer(event: EntityDamageByEntityEvent, damager: FnafUPlayer, victim: FnafUPlayer)
}