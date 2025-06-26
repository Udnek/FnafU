package me.udnek.fnafu.mechanic

import me.udnek.coreu.custom.inventory.ConstructableCustomInventory
import me.udnek.fnafu.FnafU
import me.udnek.fnafu.component.FnafUComponents
import me.udnek.fnafu.component.kit.Kit
import me.udnek.fnafu.game.FnafUGame
import me.udnek.fnafu.player.FnafUPlayer
import me.udnek.fnafu.util.getFnafU
import net.kyori.adventure.text.Component
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable

class KitMenu : ConstructableCustomInventory() {

    private var chosenKit: Kit? = null

    override fun onPlayerOpensInventory(event: InventoryOpenEvent) {
        var i = 0
        (event.player as Player).getFnafU()?.also { player ->
            Kit.REGISTRY.getAll().filter { it.playerType == player.type }.forEach {
                inventory.setItem(i++, it.displayItem)
            }
        }
    }

    override fun onPlayerClicksItem(event: InventoryClickEvent) {
        event.isCancelled = true
        chosenKit = getCurrentKit(event.currentItem?: return)
    }

    private fun getCurrentKit(itemStack: ItemStack) : Kit {
        return Kit.REGISTRY.getAll().firstOrNull { it.displayItem == itemStack } ?: FnafUComponents.KIT.default
    }

    private fun getFirstAllowedKit(player: FnafUPlayer) : Kit {
        return Kit.REGISTRY.getAll().firstOrNull { it.playerType == player.type} ?: player.kit
    }

    override fun onPlayerClosesInventory(event: InventoryCloseEvent) {
        (event.player as Player).getFnafU()?.also {
            it.kit = chosenKit ?: getFirstAllowedKit(it)
            if (it.game.stage == FnafUGame.Stage.KIT) {
                object : BukkitRunnable() { override fun run() {open(event.player as Player)} }.runTaskLater(FnafU.instance, 1)
            }
        }
    }

    override fun getTitle(): Component = Component.translatable("container.fnafu.kit")
    override fun getInventorySize(): Int = 9 * 6
    override fun shouldAutoUpdateItems(): Boolean = false
}