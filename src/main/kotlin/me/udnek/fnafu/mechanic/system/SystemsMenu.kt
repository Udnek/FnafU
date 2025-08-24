package me.udnek.fnafu.mechanic.system

import me.udnek.coreu.custom.inventory.ConstructableCustomInventory
import me.udnek.coreu.util.ComponentU
import me.udnek.fnafu.item.Items
import me.udnek.fnafu.misc.getCustom
import me.udnek.fnafu.misc.getFnafU
import me.udnek.fnafu.misc.play
import me.udnek.fnafu.sound.Sounds
import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent

class SystemsMenu : ConstructableCustomInventory() {

    override fun onPlayerClicksItem(event: InventoryClickEvent) {
        event.isCancelled = true
        val player = (event.whoClicked as Player).getFnafU() ?: return
        var playSound = true
        when (event.currentItem?.getCustom()?: return) {
            Items.DOWN_BUTTON -> player.game.systems.cursorDown(this)
            Items.UP_BUTTON ->  player.game.systems.cursorUp(this)
            Items.ENTER_BUTTON -> player.game.systems.enter(player, this)
            Items.SYSTEM_TABLET, Items.EXIT_BUTTON -> {
                player.player.closeInventory()
            }
            else -> playSound = false
        }
        if (playSound) Sounds.BUTTON_CLICK.play(player)
    }

    override fun onPlayerClosesInventory(event: InventoryCloseEvent) {
        (event.player as Player).getFnafU()?.let { it.game.systems.exitMenu(it) }
    }

    override fun getTitle(): Component {
        return ComponentU.textWithNoSpaceSpaceFont(-8, Component.text("1").font(Key.key("fnafu:system")).color(TextColor.color(1f, 1f, 1f)), 165)
    }
    override fun getInventorySize(): Int { return 9 * 6 }
}