package me.udnek.fnafu.item

import io.papermc.paper.datacomponent.item.TooltipDisplay
import me.udnek.coreu.custom.item.ConstructableCustomItem
import me.udnek.coreu.custom.item.CustomItemProperties
import me.udnek.jeiu.component.Components
import net.kyori.adventure.key.Key

class EmptyItem(private var name: String): ConstructableCustomItem() {

    private var model: CustomItemProperties.DataSupplier<Key?>? = null

    fun hiddenModel(): EmptyItem {
        model = CustomItemProperties.DataSupplier.of(null)
        return this
    }

    override fun initializeComponents() {
        super.initializeComponents()
        components.set(Components.TECHNICAL_ITEM.default)
    }

    override fun getRawId(): String = name
    override fun getTooltipDisplay() = CustomItemProperties.DataSupplier.of(TooltipDisplay.tooltipDisplay().hideTooltip(true).build())
    override fun getItemModel(): CustomItemProperties.DataSupplier<Key?>? {
        return if (model == null) super.getItemModel() else model
    }
}