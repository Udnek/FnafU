package me.udnek.fnafu.game

import me.udnek.fnafu.map.FnafUMap
import me.udnek.fnafu.mechanic.door.ButtonDoorPair
import me.udnek.fnafu.player.FnafUPlayer
import me.udnek.fnafu.util.Ticking
import me.udnek.itemscoreu.custom.minigame.game.MGUGameInstance
import me.udnek.itemscoreu.custom.minigame.player.MGUPlayer
import org.bukkit.Location
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.player.PlayerInteractEvent

interface FnafUGame : MGUGameInstance, Ticking {
    val map: FnafUMap
    fun findNearbyPlayers(location: Location, radius: Float): List<FnafUPlayer>

    fun onPlayerDamagePlayer(event: EntityDamageByEntityEvent, damager: FnafUPlayer, victim: FnafUPlayer)
    fun onPlayerClicksDoorButton(event: PlayerInteractEvent, player: MGUPlayer, button: ButtonDoorPair)
}