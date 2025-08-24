package me.udnek.fnafu.misc

import me.udnek.coreu.util.SelfRegisteringListener
import org.bukkit.GameRule
import org.bukkit.Tag
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockFadeEvent
import org.bukkit.plugin.Plugin


class CoralFixer(plugin: Plugin) : SelfRegisteringListener(plugin), Listener {
    @EventHandler
    fun onBlockFade(event: BlockFadeEvent) {
        if (event.block.world.getGameRuleValue(GameRule.RANDOM_TICK_SPEED) != 0) return
        val type = event.block.type
        if (!(Tag.CORALS.isTagged(type) || Tag.CORAL_BLOCKS.isTagged(type))) return
        event.isCancelled = true
    }

/*
    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val dialog: Dialog = Dialog.create{ builder ->
            builder.empty()
                .base(DialogBase.builder(
                    Component.text("Title"))
                    .body(mutableListOf(
                        DialogBody.item(Items.CURSOR_ICON.item, null, true, true, 1, 1)
                    ))
                    .build()
                )
                .type(DialogType.notice())
        }
        event.player.showDialog(dialog)
    }
*/

}