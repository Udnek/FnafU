package me.udnek.fnafu.item.survivor.ventilation

import me.udnek.coreu.custom.item.ConstructableCustomItem
import me.udnek.coreu.custom.item.CustomItemProperties
import me.udnek.coreu.rpgu.component.RPGUComponents
import me.udnek.fnafu.component.survivor.DoormanTabletAbility
import net.kyori.adventure.key.Key
import org.bukkit.NamespacedKey

class VentilationTablet: ConstructableCustomItem() {
    override fun getRawId(): String = "ventilation_tablet"

    override fun getItemModel(): CustomItemProperties.DataSupplier<Key?> {
        return CustomItemProperties.DataSupplier.of(NamespacedKey(this.key.namespace, "tablet/ventilation"))
    }

    override fun initializeComponents() {
        super.initializeComponents()
        components.getOrCreateDefault(RPGUComponents.ACTIVE_ABILITY_ITEM).components.set(DoormanTabletAbility.DEFAULT)
    }
}