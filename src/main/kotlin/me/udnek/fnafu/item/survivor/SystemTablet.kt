package me.udnek.fnafu.item.survivor

import me.udnek.coreu.custom.component.instance.RightClickableItem
import me.udnek.coreu.custom.item.ConstructableCustomItem
import me.udnek.coreu.custom.item.CustomItem
import me.udnek.coreu.custom.item.CustomItemProperties
import me.udnek.coreu.rpgu.component.RPGUComponents
import me.udnek.fnafu.component.survivor.SystemTabletAbility
import me.udnek.fnafu.util.getFnafU
import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component
import org.bukkit.NamespacedKey
import org.bukkit.event.player.PlayerInteractEvent

class SystemTablet: ConstructableCustomItem() {
    override fun getRawId(): String = "system_tablet"

    override fun getItemModel(): CustomItemProperties.DataSupplier<Key?> {
        return CustomItemProperties.DataSupplier.of(NamespacedKey(this.key.namespace, "tablet/system"))
    }

    override fun initializeComponents() {
        super.initializeComponents()
        components.getOrCreateDefault(RPGUComponents.ACTIVE_ABILITY_ITEM).components.set(SystemTabletAbility.DEFAULT)
    }
}