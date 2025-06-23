package me.udnek.fnafu.component

import me.udnek.coreu.rpgu.component.RPGUComponents
import me.udnek.coreu.util.SelfRegisteringListener
import me.udnek.fnafu.event.SystemRepairedEvent
import me.udnek.fnafu.util.getCustom
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.plugin.Plugin

class ComponentListener(plugin: Plugin) : SelfRegisteringListener(plugin){


    @EventHandler
    fun onRightClick(event: PlayerInteractEvent){
        if (!event.action.isRightClick) return
        event.item?.getCustom()?.components?.get(RPGUComponents.ACTIVE_ABILITY_ITEM)?.components?.forEach {
            activeAbility -> (activeAbility as? FnafUActiveAbility)?.onRightClick(event)
        }
    }

    @EventHandler
    fun onSystemRepaired(event: SystemRepairedEvent){
        event.system.game.applyForEveryAbility { component, player, item ->
            component.components.get(FnafUComponents.SPRINGTRAP_BREAK_CAMERAS_ABILITY)?.onSystemRepaired(item, player, event)
        }
    }
}
