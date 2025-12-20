package me.udnek.fnafu.component.kit

import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.datacomponent.item.ItemLore
import me.udnek.coreu.custom.component.instance.TranslatableThing
import me.udnek.coreu.custom.item.CustomItem
import me.udnek.coreu.custom.registry.AbstractRegistrableComponentable
import me.udnek.coreu.custom.sound.CustomSound
import me.udnek.fnafu.player.FnafUPlayer
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.inventory.ItemStack

open class ConstructableKit(
    private val id: String,
    final override val playerType: FnafUPlayer.Type,
    protected val translation: TranslatableThing,
    protected val displayCustomItem: CustomItem,
    protected val customItems: List<CustomItem>
) : Kit, AbstractRegistrableComponentable<Kit>() {

    override var jumpScareSound: CustomSound? = null

    override val displayItem: ItemStack
        get() {
            val item = displayCustomItem.item
            item.setData(DataComponentTypes.ITEM_NAME, Component.translatable(translationKey()))
            val lore = ArrayList<Component>()
            for (customItem in customItems) {
                lore.add(customItem.item.getData(DataComponentTypes.ITEM_NAME)
                    ?.decoration(TextDecoration.ITALIC, false)
                    ?.color(NamedTextColor.GREEN) ?: Component.text("null"))
            }
            item.setData(DataComponentTypes.LORE, ItemLore.lore(lore))
            return item
        }

    override val items: List<ItemStack>
        get() = customItems.map { it.item }

    override fun afterInitialization() {
        super<AbstractRegistrableComponentable>.afterInitialization()
        components.set(translation)
    }

    override fun setUp(player: FnafUPlayer) {
        giveToCurrentInventory(player)
    }

    override fun giveToCurrentInventory(player: FnafUPlayer) {
        items.forEach {
            player.currentInventory.add(it)
        }
    }

    override fun getRawId(): String = id
}
