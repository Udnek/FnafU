package me.udnek.fnafu.item

import me.udnek.coreu.custom.item.ConstructableCustomItem
import me.udnek.coreu.custom.item.CustomItemProperties
import net.kyori.adventure.key.Key

class EmptyItem(private var name: String): ConstructableCustomItem() {

    private var model: CustomItemProperties.DataSupplier<Key?>? = null

    fun hiddenModel(): EmptyItem {
        model = CustomItemProperties.DataSupplier.of(null)
        return this
    }

    override fun getRawId(): String = name
    override fun getHideTooltip(): Boolean? = true
    override fun getItemModel(): CustomItemProperties.DataSupplier<Key?>? {
        return if (model == null) super.getItemModel() else model
    }
}