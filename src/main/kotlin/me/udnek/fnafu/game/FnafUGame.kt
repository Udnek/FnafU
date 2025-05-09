package me.udnek.fnafu.game

import me.udnek.fnafu.map.FnafUMap
import me.udnek.fnafu.mechanic.AudioSystem
import me.udnek.fnafu.mechanic.VentilationSystem
import me.udnek.fnafu.mechanic.camera.CameraSystem
import me.udnek.fnafu.mechanic.door.ButtonDoorPair
import me.udnek.fnafu.mechanic.system.Systems
import me.udnek.fnafu.player.FnafUPlayer
import me.udnek.fnafu.util.Ticking
import me.udnek.itemscoreu.custom.minigame.game.MGUGameInstance
import me.udnek.itemscoreu.custom.minigame.player.MGUPlayer
import me.udnek.itemscoreu.customcomponent.ComponentHolder
import me.udnek.itemscoreu.customcomponent.CustomComponent
import org.bukkit.Location
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.player.PlayerInteractEvent

interface FnafUGame : MGUGameInstance, Ticking, ComponentHolder<FnafUGame, CustomComponent<FnafUGame>> {
    val audioSystem: AudioSystem
    val cameraSystem: CameraSystem
    val ventilationSystem: VentilationSystem
    val systems: Systems
    val map: FnafUMap
    fun findNearbyPlayers(location: Location, radius: Float): List<FnafUPlayer>

    fun onPlayerDamagePlayer(event: EntityDamageByEntityEvent, damager: FnafUPlayer, victim: FnafUPlayer)
    fun onPlayerClicksDoorButton(event: PlayerInteractEvent, player: MGUPlayer, button: ButtonDoorPair)
}