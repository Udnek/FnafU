package me.udnek.fnafu.game

import me.udnek.coreu.custom.registry.AbstractRegistrable
import me.udnek.coreu.custom.registry.CustomRegistries
import me.udnek.coreu.mgu.game.MGUGameType
import me.udnek.fnafu.FnafU
import me.udnek.fnafu.mechanic.system.Systems
import me.udnek.fnafu.player.FnafUPlayer
import me.udnek.fnafu.util.getFnafU
import org.bukkit.Tag
import org.bukkit.block.data.Powerable
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.inventory.EquipmentSlot


object GameTypes {

    val ENERGY: MGUGameType = register(object : AbstractRegistrable(), MGUGameType, Listener{
        override fun getRawId(): String = "energy"
        @EventHandler
        fun onItemDrop(event: PlayerDropItemEvent){
            getIfPlayerInThisGame<FnafUPlayer>(event.player)?.let {
                event.isCancelled = it.game.isRunning
            }
        }
        @EventHandler
        fun onAttack(event: EntityDamageByEntityEvent) {
            val victim = (event.entity as? Player)?.getFnafU() ?: return
            if (victim.game.type != this) return
            if (!victim.game.isRunning) return
            val damager = (event.damager as? Player)?.getFnafU() ?: return
            victim.game.onPlayerDamagePlayer(event, damager, victim)
        }
        @EventHandler
        fun onInteract(event: PlayerInteractEvent){
            if ((getIfPlayerInThisGame<FnafUPlayer>(event.player)?.game?.stage ?: return) == FnafUGame.Stage.WAITING) return
            if (event.hand == EquipmentSlot.OFF_HAND) return
            val block = event.clickedBlock ?: return
            if (Tag.BUTTONS.isTagged(block.type)) {
                if ((block.blockData as Powerable).isPowered) return
                getIfPlayerInThisGame<FnafUPlayer>(event.player)?.let {
                    if (it.type != FnafUPlayer.Type.SURVIVOR) return
                    for (pair in it.game.systems.door.doors) {
                        if (pair.hasButtonAt(block.location)) {
                            it.game.onPlayerClicksDoorButton(event, it, pair)
                        }
                    }
                }
            } else if (block.type == Systems.STATION_BLOCK_MATERIAL){
                getIfPlayerInThisGame<FnafUPlayer>(event.player)?.let {
                    if (it.type == FnafUPlayer.Type.SURVIVOR && block.location.distance(it.player.location) < 1.5){
                        it.game.systems.openMenu(it)
                    }
                }
            }
        }
        @EventHandler
        fun onLeave(event: PlayerQuitEvent) {
            getIfPlayerInThisGame<FnafUPlayer>(event.player)?.let {
                //it.status = FnafUPlayer.Status.INACTIVE
                it.game.onPlayerLeave(event, it)
            }
        }
        /*@EventHandler
        fun onJoin(event: PlayerJoinEvent) {
            getIfPlayerInThisGame<FnafUPlayer>(event.player)?.let {
                it.status = FnafUPlayer.Status.INACTIVE
                it.f
            }
        }*/
    })

    private fun register(type: MGUGameType): MGUGameType {
        return CustomRegistries.MGU_GAME_TYPE.register(FnafU.instance, type)
    }
}















