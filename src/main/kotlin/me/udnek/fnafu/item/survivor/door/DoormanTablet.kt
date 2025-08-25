package me.udnek.fnafu.item.survivor.door

import me.udnek.coreu.custom.item.ConstructableCustomItem
import me.udnek.coreu.custom.item.CustomItemProperties
import me.udnek.coreu.rpgu.component.RPGUComponents
import me.udnek.fnafu.component.survivor.tablet.DoormanTabletAbility
import net.kyori.adventure.key.Key
import org.bukkit.NamespacedKey

class DoormanTablet: ConstructableCustomItem() {
    override fun getRawId(): String = "doorman_tablet"

    override fun getItemModel(): CustomItemProperties.DataSupplier<Key?> {
        return CustomItemProperties.DataSupplier.of(NamespacedKey(this.key.namespace, "tablet/door"))
    }

    override fun initializeComponents() {
        super.initializeComponents()
        components.getOrCreateDefault(RPGUComponents.ACTIVE_ABILITY_ITEM).components.set(DoormanTabletAbility.DEFAULT)
    }
}