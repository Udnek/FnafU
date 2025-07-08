package me.udnek.fnafu.item

import me.udnek.coreu.custom.item.ConstructableCustomItem
import me.udnek.coreu.custom.item.CustomItemProperties
import net.kyori.adventure.key.Key
import org.bukkit.Material

class ReadyButton : ConstructableCustomItem() {
    override fun getRawId(): String = "ready_button"
    override fun getMaterial(): Material = Material.LIME_STAINED_GLASS_PANE
    override fun getItemModel(): CustomItemProperties.DataSupplier<Key?>? = null
}