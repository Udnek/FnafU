package me.udnek.fnafu.game

import me.udnek.fnafu.FnafU
import me.udnek.fnafu.util.getFnafU
import me.udnek.itemscoreu.customminigame.game.MGUGameType
import me.udnek.itemscoreu.customregistry.AbstractRegistrable
import me.udnek.itemscoreu.customregistry.CustomRegistries
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.player.PlayerDropItemEvent


object GameTypes {

    val ENERGY: MGUGameType = register(object : AbstractRegistrable(), MGUGameType, Listener{
        override fun getRawId(): String = "energy"

        @EventHandler
        fun onItemDrop(event: PlayerDropItemEvent){
            if (!isPlayerInThisGame(event.player)) return
            event.isCancelled = true
        }
        @EventHandler
        fun onPlayerAttacksEntity(event: EntityDamageByEntityEvent) {
            val victim = (event.entity as? Player)?.getFnafU() ?: return
            if (victim.game.type != this) return
            val damager = (event.damager as? Player)?.getFnafU() ?: return
            victim.game.onPlayerDamagePlayer(event, damager, victim)
        }
    })

    private fun register(type: MGUGameType): MGUGameType {
        return CustomRegistries.MGU_GAME_TYPE.register(FnafU.instance, type)
    }

}















