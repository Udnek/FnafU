package me.udnek.fnafu.game

import me.udnek.fnafu.FnafU
import me.udnek.fnafu.player.FnafUPlayer
import me.udnek.fnafu.util.getFnafU
import me.udnek.itemscoreu.custom.minigame.game.MGUGameType
import me.udnek.itemscoreu.customregistry.AbstractRegistrable
import me.udnek.itemscoreu.customregistry.CustomRegistries
import org.bukkit.Tag
import org.bukkit.block.data.Powerable
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.player.PlayerInteractEvent


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
            val block = event.clickedBlock ?: return
            if (!Tag.BUTTONS.isTagged(block.type)) return
            if ((block.blockData as Powerable).isPowered) return
            getIfPlayerInThisGame<FnafUPlayer>(event.player)?.let {
                player ->
                if (player.type != FnafUPlayer.PlayerType.SURVIVOR) return
                for (pair in player.game.map.doors) {
                    if (pair.hasButtonAt(block.location)) {
                        player.game.onPlayerClicksDoorButton(event, player, pair)
                    }
                }
            }
        }
    })

    private fun register(type: MGUGameType): MGUGameType {
        return CustomRegistries.MGU_GAME_TYPE.register(FnafU.instance, type)
    }
}















