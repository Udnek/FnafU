package me.udnek.fnafu.component

import me.udnek.coreu.custom.item.CustomItem
import me.udnek.coreu.custom.registry.AbstractRegistrable
import me.udnek.fnafu.player.FnafUPlayer
import org.bukkit.inventory.ItemStack

open class ConstructableKit : Kit, AbstractRegistrable {

    /*private val items: List<Supplier<ItemStack>>*/
    private val customItems: List<CustomItem>
    val type: FnafUPlayer.Type
    val displayItem: ItemStack
    private val id: String

    constructor(id: String, type: FnafUPlayer.Type, displayItem: ItemStack, /*items: List<Supplier<ItemStack>>,*/ customItems: List<CustomItem>) {
        /*this.items = items*/
        this.displayItem = displayItem
        this.customItems = customItems
        this.type = type
        this.id = id
    }

    override fun setUp(player: FnafUPlayer) {
        player.player.inventory.clear()
        /*for (item in items) {
            player.player.inventory.addItem(item.get())
        }*/
        for (customItem in customItems) {
            player.player.inventory.addItem(customItem.item)
        }
    }

    override fun regive(player: FnafUPlayer) {
        setUp(player)
    }

    override fun getRawId(): String {
        return id
    }
}
