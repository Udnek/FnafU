package me.udnek.fnafu.misc

import io.papermc.paper.datacomponent.DataComponentTypes
import me.udnek.fnafu.FnafU
import me.udnek.jeiu.item.Items
import me.udnek.jeiu.menu.AllItemsMenu
import me.udnek.jeiu.menu.Category
import net.kyori.adventure.text.Component
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack

object Categories {
    val BUILD_ITEMS = register(object : Category("build_items") {
        override fun getIcon(context: AllItemsMenu): ItemStack {
            val item = Items.SWITCH.getItem()
            item.setData(DataComponentTypes.ITEM_NAME, Component.translatable("item.fnafu.build_items"))
            item.setData(DataComponentTypes.ITEM_MODEL, NamespacedKey(FnafU.instance, "category/build_items"))
            return item
        }

        override fun getAll(p0: AllItemsMenu): List<ItemStack> {
            TODO("Not yet implemented")
        }
    })


    private fun register(category: Category): Category {
        return Category.REGISTRY.register<Category>(FnafU.instance, category)
    }
}