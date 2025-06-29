package me.udnek.fnafu.component.kit

import io.papermc.paper.datacomponent.DataComponentTypes
import me.udnek.coreu.custom.item.CustomItem
import me.udnek.coreu.custom.registry.AbstractRegistrable
import me.udnek.coreu.custom.sound.CustomSound
import me.udnek.fnafu.player.FnafUPlayer
import net.kyori.adventure.text.Component
import org.bukkit.inventory.ItemStack

open class ConstructableKit : Kit, AbstractRegistrable {


    private val customItems: List<CustomItem>
    private val id: String

    final override val playerType: FnafUPlayer.Type
    override var jumpScareSound: CustomSound? = null

    private val displayCustomItem: CustomItem

    override val displayItem: ItemStack
        get() = displayCustomItem.item.also{ it.setData(DataComponentTypes.ITEM_NAME, Component.translatable("kit.fnafu.$id")) }

    override val items: List<ItemStack>
        get() = customItems.map { it.item }

    constructor(id: String, type: FnafUPlayer.Type, displayItem: CustomItem, customItems: List<CustomItem>) {
        this.displayCustomItem = displayItem
        this.customItems = customItems
        this.playerType = type
        this.id = id
    }

    override fun setUp(player: FnafUPlayer) {
        player.player.inventory.clear()
        for (customItem in customItems) {
            if (customItem.item.hasData(DataComponentTypes.EQUIPPABLE)){
                player.player.inventory.setItem(customItem.item.getData(DataComponentTypes.EQUIPPABLE)!!.slot(), customItem.item)
            } else player.player.inventory.addItem(customItem.item)
        }
    }

    override fun regive(player: FnafUPlayer) {
        setUp(player)
    }

    override fun getRawId(): String {
        return id
    }
}
