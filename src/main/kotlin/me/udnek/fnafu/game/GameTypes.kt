package me.udnek.fnafu.game

import me.udnek.coreu.custom.registry.AbstractRegistrable
import me.udnek.coreu.custom.registry.CustomRegistries
import me.udnek.coreu.mgu.game.MGUGameType
import me.udnek.fnafu.FnafU
import me.udnek.fnafu.block.Blocks
import me.udnek.fnafu.event.EnergyEndedUpEvent
import me.udnek.fnafu.misc.getCustom
import me.udnek.fnafu.misc.getFnafU
import me.udnek.fnafu.player.FnafUPlayer
import org.bukkit.Tag
import org.bukkit.block.data.Powerable
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.*
import org.bukkit.inventory.EquipmentSlot


object GameTypes {

    val ENERGY: MGUGameType = register(object : AbstractRegistrable(), MGUGameType, Listener{
        override fun getRawId(): String = "energy"
        @EventHandler
        fun onItemDrop(event: PlayerDropItemEvent){
            if (!(getIfPlayerInThisGame<FnafUPlayer>(event.player)?.game?.isRunning ?: return)) return
            event.isCancelled = true
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
            if (event.hand != EquipmentSlot.HAND) return
            if (!event.action.isRightClick) return
            val player = getIfPlayerInThisGame<FnafUPlayer>(event.player) ?: return
            if (!player.game.isRunning) return
            val block = event.clickedBlock ?: return
            if (!Tag.BUTTONS.isTagged(block.type)) event.isCancelled = true

            proceedButton(player, event)
            if (block.getCustom() == Blocks.SYSTEM_STATION){
                if (player.type == FnafUPlayer.Type.SURVIVOR && block.location.toCenterLocation().distance(player.player.eyeLocation) < 1.5){
                    player.game.systems.openStation(player)
                }
            }
        }

        @EventHandler
        fun onPlayerInteractEntity(event: PlayerInteractEntityEvent) {
            if (getIfPlayerInThisGame<FnafUPlayer>(event.player)?.game?.isRunning ?: false) event.isCancelled = true
        }

        @EventHandler
        fun onPlayerInteractAtEntity(event: PlayerInteractAtEntityEvent) {
            if (getIfPlayerInThisGame<FnafUPlayer>(event.player)?.game?.isRunning ?: false) event.isCancelled = true
        }


        private fun proceedButton(player: FnafUPlayer, event: PlayerInteractEvent) {
            val block = event.clickedBlock ?: return
            if ((block.blockData as? Powerable)?.isPowered == true) return
            for (pair in player.game.systems.door.doors) {
                if (pair.button.hasAt(block.location)) {
                    player.game.onPlayerClicksDoorButton(event, player, pair)
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
        @EventHandler
        fun onInventoryMoveItem(event: InventoryClickEvent) {
            if (!(getIfPlayerInThisGame<FnafUPlayer>(event.whoClicked as Player)?.game?.isRunning ?: return)) return
            event.isCancelled = true
        }

        @EventHandler
        fun onEnergyEndedUp(event: EnergyEndedUpEvent){

        }
    })

    private fun register(type: MGUGameType): MGUGameType {
        return CustomRegistries.MGU_GAME_TYPE.register(FnafU.instance, type)
    }
}















