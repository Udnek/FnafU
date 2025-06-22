package me.udnek.fnafu.component.kit

import me.udnek.coreu.custom.item.CustomItem
import me.udnek.coreu.custom.registry.AbstractRegistrable
import me.udnek.fnafu.player.FnafUPlayer
import org.bukkit.inventory.ItemStack

open class ConstructableKit : Kit, AbstractRegistrable {


    private val customItems: List<CustomItem>
    private val id: String

    final override val playerType: FnafUPlayer.Type
    final override val displayItem: ItemStack

    override val items: List<ItemStack>
        get() = customItems.map { it.item }

    constructor(id: String, type: FnafUPlayer.Type, displayItem: ItemStack, customItems: List<CustomItem>) {
        this.displayItem = displayItem
        this.customItems = customItems
        this.playerType = type
        this.id = id
    }

    override fun setUp(player: FnafUPlayer) {
        player.player.inventory.clear()
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
