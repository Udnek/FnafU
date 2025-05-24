package me.udnek.fnafu.util

import me.udnek.fnafu.component.Components
import me.udnek.itemscoreu.customitem.CustomItem
import me.udnek.itemscoreu.util.SelfRegisteringListener
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.plugin.Plugin

class ItemListener(plugin: Plugin) : SelfRegisteringListener(plugin) {
    @EventHandler
    fun onPlayerInteract(event: PlayerInteractEvent) {
        if (!event.action.isRightClick) return
        val customItem = CustomItem.get(event.item) ?: return
        customItem.components.getOrDefault(Components.TABLET_COMPONENT).onRightClick(customItem, event)
    }
}