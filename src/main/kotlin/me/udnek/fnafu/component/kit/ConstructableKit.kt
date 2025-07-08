package me.udnek.fnafu.component.kit

import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.datacomponent.item.ItemLore
import me.udnek.coreu.custom.item.CustomItem
import me.udnek.coreu.custom.registry.AbstractRegistrable
import me.udnek.coreu.custom.sound.CustomSound
import me.udnek.coreu.rpgu.component.RPGUComponents
import me.udnek.fnafu.player.FnafUPlayer
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.inventory.ItemStack

open class ConstructableKit(
    private val id: String,
    final override val playerType: FnafUPlayer.Type,
    protected val displayCustomItem: CustomItem,
    protected val permanentCustomItems: List<CustomItem>,
    protected val inventoryCustomItems: List<CustomItem> = listOf()
) : Kit, AbstractRegistrable() {

    override var jumpScareSound: CustomSound? = null

    override val displayItem: ItemStack
        get() {
            val item = displayCustomItem.item
            item.setData(DataComponentTypes.ITEM_NAME, Component.translatable("kit.${key.namespace}.${key.key}"))
            val lore = ArrayList<Component>()
            val items = ArrayList(permanentCustomItems)
            items.addAll(inventoryCustomItems)
            for (customItem in items) {
                lore.add(customItem.item.getData(DataComponentTypes.ITEM_NAME)
                    ?.decoration(TextDecoration.ITALIC, false)
                    ?.color(NamedTextColor.GREEN) ?: Component.text("null"))
            }
            item.setData(DataComponentTypes.LORE, ItemLore.lore(lore))
            return item
        }

    override val permanentItems: List<ItemStack>
        get() = permanentCustomItems.map { it.item }

    override val inventoryItems: List<ItemStack>
        get() = inventoryCustomItems.map { it.item }

    override fun setUp(player: FnafUPlayer) {
        inventoryCustomItems.forEach { player.currentInventory.add(it.item) }
        giveItems(player)
        player.currentInventory.give(player)
        player.abilityItems.forEach { item ->
            item.components.get(RPGUComponents.ACTIVE_ABILITY_ITEM)?.components?.forEach { ability ->
                ability.cooldown(item, player.player)
            }
        }
    }

    protected fun giveItems(player: FnafUPlayer){
        for (customItem in permanentCustomItems) {
            if (customItem.item.hasData(DataComponentTypes.EQUIPPABLE)){
                player.player.inventory.setItem(customItem.item.getData(DataComponentTypes.EQUIPPABLE)!!.slot(), customItem.item)
            } else player.player.inventory.addItem(customItem.item)
        }
    }

    override fun regiveCurrentInventory(player: FnafUPlayer) {
        player.currentInventory.reset()
        inventoryCustomItems.forEach { player.currentInventory.add(it.item) }
    }

    override fun regive(player: FnafUPlayer) {
        giveItems(player)
    }

    override fun getRawId(): String = id
}
