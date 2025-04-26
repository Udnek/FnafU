package me.udnek.fnafu.component

import me.udnek.fnafu.player.FnafUPlayer
import me.udnek.itemscoreu.customitem.CustomItem
import org.bukkit.inventory.ItemStack
import java.util.function.Supplier

class ConstructableKit : Kit {

    private val items: List<Supplier<ItemStack>>
    private val customItems: List<CustomItem>

    constructor(items: List<Supplier<ItemStack>>, customItems: List<CustomItem>) {
        this.items = items
        this.customItems = customItems
    }

    override fun setUp(player: FnafUPlayer) {
        player.player.inventory.clear()
        for (item in items) {
            player.player.inventory.addItem(item.get())
        }
        for (customItem in customItems) {
            player.player.inventory.addItem(customItem.item)
        }
    }

    override fun regive(player: FnafUPlayer) {
        setUp(player)
    }
}
