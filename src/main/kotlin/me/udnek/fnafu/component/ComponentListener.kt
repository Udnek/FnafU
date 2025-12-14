package me.udnek.fnafu.component

import me.udnek.coreu.custom.equipmentslot.universal.BaseUniversalSlot
import me.udnek.coreu.rpgu.component.RPGUComponents
import me.udnek.coreu.util.ClickRateLimit
import me.udnek.coreu.util.SelfRegisteringListener
import me.udnek.fnafu.event.SystemRepairedEvent
import me.udnek.fnafu.misc.getCustom
import me.udnek.fnafu.misc.getFnafU
import me.udnek.fnafu.player.FnafUPlayer
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerSwapHandItemsEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.plugin.Plugin

class ComponentListener(plugin: Plugin) : SelfRegisteringListener(plugin), Listener {

    @EventHandler
    fun onRightClick(event: PlayerInteractEvent){
        if (!event.action.isRightClick) return
        if (!ClickRateLimit.triggerAndCanUse(event, 5, false)) return

        event.item?.getCustom()?.let {
            it.components.get(RPGUComponents.ACTIVE_ABILITY_ITEM)?.components?.forEach {
                    activeAbility -> (activeAbility as? FnafUActiveAbility)?.onRightClick(event)
            }
//            it.components.get(RPGUComponents.TOGGLE_ABILITY_ITEM)?.components?.get(FnafUComponents.FLASHLIGHT_ABILITY)
//                ?.toggle(it, event.player, BaseUniversalSlot(event.hand!!))
        }
    }

    @EventHandler(ignoreCancelled = true)
    fun onPlayerSwapHandItems(event: PlayerSwapHandItemsEvent) {
        val custom = event.mainHandItem.getCustom() ?: return
        val ability =
            custom.components.get(RPGUComponents.TOGGLE_ABILITY_ITEM)?.components?.get(FnafUComponents.FLASHLIGHT_ABILITY) ?: return
        event.isCancelled = true
        ability.toggle(custom, event.player, BaseUniversalSlot(EquipmentSlot.OFF_HAND))
    }

    @EventHandler
    fun onSystemRepaired(event: SystemRepairedEvent){
        event.system.game.applyForEveryAbility { component, player, item ->
            component.components.get(FnafUComponents.SPRINGTRAP_BREAK_CAMERAS_ABILITY)?.onSystemRepaired(item, player, event)
        }
    }

    @EventHandler
    fun onPlayerInteractEntity(event: PlayerInteractEntityEvent) {
        event.player.getFnafU()?.let {
            val item = it.player.inventory.getItem(event.hand).getCustom() ?: return
            item.components.get(RPGUComponents.ACTIVE_ABILITY_ITEM)?.components?.get(FnafUComponents.SPRINGTRAP_PLUSHTRAP_ABILITY)?.pickPlushtrap(item, it)
        }
    }
}
