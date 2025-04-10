package me.udnek.fnafu.component

import me.udnek.fnafu.player.FnafUPlayer
import me.udnek.itemscoreu.customitem.CustomItem

class ConstructableKit : Kit {

    private val items: List<CustomItem>

    constructor(vararg items: CustomItem) {
        this.items = items.toList()
    }

    override fun setUp(player: FnafUPlayer) {
        player.player.inventory.clear()
        for (item in items) {
            player.player.inventory.addItem(item.item)
        }
    }

    override fun regive(player: FnafUPlayer) {
        setUp(player)
    }
}
