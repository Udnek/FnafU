package me.udnek.fnafu.mechanic.system

import me.udnek.fnafu.item.Items
import me.udnek.fnafu.util.getFnafU
import me.udnek.itemscoreu.custominventory.ConstructableCustomInventory
import me.udnek.itemscoreu.customitem.CustomItem
import me.udnek.itemscoreu.util.ComponentU
import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent

class SystemMenu : ConstructableCustomInventory {

    constructor() : super() {
        inventory.setItem(9, Items.CURSOR_ICON.item)
    }

    override fun onPlayerClicksItem(event: InventoryClickEvent) {
        event.isCancelled = true
        event.currentItem?.let {
            when (CustomItem.get(it)) {
                Items.DOWN_BUTTON -> (event.whoClicked as Player).getFnafU()?.also { it.game.systems.cursorDown(inventory) }
                Items.UP_BUTTON ->  (event.whoClicked as Player).getFnafU()?.also { it.game.systems.cursorUp(inventory) }
                Items.ENTER_BUTTON -> (event.whoClicked as Player).getFnafU()?.also { it.game.systems.enter(it, inventory) }
                Items.SYSTEM_TABLET -> (event.whoClicked as Player).getFnafU()?.also {
                    it.player.closeInventory()
                    it.game.systems.exitSystem(inventory, it)
                }
            }
        }
    }

    override fun onPlayerClosesInventory(event: InventoryCloseEvent) {
        (event.player as Player).getFnafU()?.also { it.game.systems.exitSystem(inventory, it) }
    }

    override fun getTitle(): Component? {
        return ComponentU.textWithNoSpaceSpaceFont(-8, Component.text("1").font(Key.key("fnafu:system")).color(TextColor.color(1f, 1f, 1f)), 165)
    }
    override fun getInventorySize(): Int { return 9 * 6 }
}