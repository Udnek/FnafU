package me.udnek.fnafu.mechanic

import me.udnek.fnafu.FnafU
import me.udnek.fnafu.component.Components
import me.udnek.fnafu.component.Kit
import me.udnek.fnafu.game.FnafUGame
import me.udnek.fnafu.player.FnafUPlayer
import me.udnek.fnafu.util.getFnafU
import me.udnek.itemscoreu.custominventory.ConstructableCustomInventory
import net.kyori.adventure.text.Component
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable

class KitMenu : ConstructableCustomInventory() {

    private var kit: Kit? = null

    override fun onPlayerOpensInventory(event: InventoryOpenEvent) {
        var i = 0
        (event.player as Player).getFnafU()?.also { player ->
            Kit.REGISTRY.getAll {
                if (it.type == player.type) {
                    inventory.setItem(i, it.displayItem)
                    i++
                }
            }
        }
    }

    override fun onPlayerClicksItem(event: InventoryClickEvent) {
        event.isCancelled = true
        event.currentItem?.let { itemStack -> (event.whoClicked as Player).getFnafU()?.also { kit = getCurrentKit(itemStack) } }
    }

    private fun getCurrentKit(itemStack: ItemStack) : Kit {
        var playerKit: Kit = Components.KIT.default
        Kit.REGISTRY.getAll { if (it.displayItem == itemStack) playerKit = it }
        return playerKit
    }

    private fun getFirstAllowedKit(player: FnafUPlayer) : Kit {
        var playerKit: Kit = player.kit
        Kit.REGISTRY.getAll { if (it.type == player.type) playerKit = it; return@getAll }
        return playerKit
    }

    override fun onPlayerClosesInventory(event: InventoryCloseEvent) {
        (event.player as Player).getFnafU()?.also {
            it.kit = kit ?: getFirstAllowedKit(it)
            if (it.game.stage == FnafUGame.Stage.KIT) {
                object : BukkitRunnable() { override fun run() {open(event.player as Player)} }.runTaskLater(FnafU.instance, 1)
            }
        }
    }

    override fun getTitle(): Component? { return Component.text("Kit") }
    override fun getInventorySize(): Int { return 9 * 6 }
}