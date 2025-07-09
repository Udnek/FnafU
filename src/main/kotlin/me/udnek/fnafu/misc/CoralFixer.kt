package me.udnek.fnafu.misc

import me.udnek.coreu.util.SelfRegisteringListener
import org.bukkit.GameRule
import org.bukkit.Tag
import org.bukkit.event.EventHandler
import org.bukkit.event.block.BlockFadeEvent
import org.bukkit.plugin.Plugin


class CoralFixer(plugin: Plugin) : SelfRegisteringListener(plugin) {
    @EventHandler
    fun onBlockFade(event: BlockFadeEvent) {
        val type = event.block.type
        if (event.block.world.getGameRuleValue(GameRule.RANDOM_TICK_SPEED) != 0) return
        if (!(Tag.CORALS.isTagged(type) || Tag.CORAL_BLOCKS.isTagged(type))) return
        event.isCancelled = true
    }
}