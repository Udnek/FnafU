package me.udnek.fnafu.item.animatronic.freddy

import me.udnek.coreu.custom.item.ConstructableCustomItem
import me.udnek.coreu.custom.item.CustomItemProperties
import me.udnek.fnafu.FnafU
import net.kyori.adventure.key.Key
import org.bukkit.NamespacedKey

class FreddyTrap: ConstructableCustomItem() {
    override fun getRawId() = "freddy_trap"
    override fun getItemModel(): CustomItemProperties.DataSupplier<Key> =
        CustomItemProperties.DataSupplier.of(NamespacedKey(FnafU.instance, "animatronic/freddy/trap"))
}