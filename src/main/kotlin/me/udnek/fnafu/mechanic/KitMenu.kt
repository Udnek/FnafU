package me.udnek.fnafu.mechanic;

import me.udnek.fnafu.component.Kit
import me.udnek.fnafu.player.FnafUPlayer
import me.udnek.fnafu.util.getFnafU
import me.udnek.itemscoreu.custominventory.ConstructableCustomInventory
import net.kyori.adventure.text.Component
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.inventory.ItemStack

class KitMenu : ConstructableCustomInventory() {

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

    /*constructor(playerType: FnafUPlayer.Type) : super() {

    }*/

    override fun onPlayerClicksItem(event: InventoryClickEvent) {
        event.isCancelled = true
        event.currentItem?.let { itemStack ->
            (event.whoClicked as Player).getFnafU()?.also {
                it.kit = getCurrentKit(itemStack) ?: return
                it.player.closeInventory()
            }
        }
    }

    private fun getCurrentKit(itemStack: ItemStack) : Kit? {
        var playerKit: Kit? = null
        Kit.REGISTRY.getAll { if (it.displayItem == itemStack) playerKit = it }
        return playerKit
    }

    private fun getFirstAllowKit(playerType: FnafUPlayer.Type) : Kit? {
        var playerKit: Kit? = null
        Kit.REGISTRY.getAll { if (it.type == playerType) playerKit = it; return@getAll }
        return playerKit
    }

    override fun onPlayerClosesInventory(event: InventoryCloseEvent) {
        (event.player as Player).getFnafU()?.also { it.kit = getFirstAllowKit(it.type) ?: return }
    }

    override fun getTitle(): Component? { return Component.text("глеб пидор") }
    override fun getInventorySize(): Int { return 9 * 6 }
}