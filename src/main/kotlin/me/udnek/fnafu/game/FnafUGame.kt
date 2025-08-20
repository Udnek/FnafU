package me.udnek.fnafu.game

import me.udnek.coreu.custom.component.ComponentHolder
import me.udnek.coreu.custom.component.CustomComponent
import me.udnek.coreu.custom.item.CustomItem
import me.udnek.coreu.custom.sidebar.CustomSidebar
import me.udnek.coreu.mgu.game.MGUGameInstance
import me.udnek.coreu.rpgu.component.RPGUActiveAbilityItem
import me.udnek.fnafu.map.FnafUMap
import me.udnek.fnafu.mechanic.Energy
import me.udnek.fnafu.mechanic.system.Systems
import me.udnek.fnafu.mechanic.system.door.ButtonDoorPair
import me.udnek.fnafu.misc.Ticking
import me.udnek.fnafu.player.FnafUPlayer
import me.udnek.fnafu.player.PlayerContainer
import org.bukkit.Location
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.scoreboard.Team

interface FnafUGame : MGUGameInstance, Ticking, ComponentHolder<FnafUGame, CustomComponent<FnafUGame>> {

    val stage: Stage
    val systems: Systems
    val map: FnafUMap
    var survivorLives: Int
    val sidebar: CustomSidebar
    val playerContainer: PlayerContainer
    val energy: Energy

    fun applyForEveryAbility(function: (component: RPGUActiveAbilityItem, player: FnafUPlayer, item: CustomItem) -> Unit)
    fun getTeam(fnafUPlayer: FnafUPlayer): Team?
    fun findNearbyPlayers(location: Location, radius: Double, playerType: FnafUPlayer.Type? = null): List<FnafUPlayer>
    fun checkForEndConditions()
    fun updateSidebar()
    fun onPlayerDamagePlayer(event: EntityDamageByEntityEvent, damager: FnafUPlayer, victim: FnafUPlayer)
    fun onPlayerClicksDoorButton(event: PlayerInteractEvent, player: FnafUPlayer, button: ButtonDoorPair)
    fun onPlayerLeave(event: PlayerQuitEvent, player: FnafUPlayer)
    override fun isRunning(): Boolean = stage != Stage.WAITING

    enum class Stage {
        WAITING,
        KIT,
        RUNNING
    }
}