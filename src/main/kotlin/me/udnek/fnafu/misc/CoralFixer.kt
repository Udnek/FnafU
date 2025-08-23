package me.udnek.fnafu.misc

import io.papermc.paper.dialog.Dialog
import io.papermc.paper.registry.data.dialog.DialogBase
import io.papermc.paper.registry.data.dialog.body.DialogBody
import io.papermc.paper.registry.data.dialog.type.DialogType
import me.udnek.coreu.util.SelfRegisteringListener
import me.udnek.fnafu.item.Items
import net.kyori.adventure.text.Component
import org.bukkit.GameRule
import org.bukkit.Tag
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockFadeEvent
import org.bukkit.event.player.PlayerJoinEvent
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