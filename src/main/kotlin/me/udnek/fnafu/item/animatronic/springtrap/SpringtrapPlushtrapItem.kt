package me.udnek.fnafu.item.animatronic.springtrap

import me.udnek.coreu.custom.item.ConstructableCustomItem
import me.udnek.coreu.custom.item.CustomItemProperties
import me.udnek.coreu.rpgu.component.RPGUComponents
import me.udnek.fnafu.FnafU
import me.udnek.fnafu.component.AbilityIconFilesComponent
import me.udnek.fnafu.component.FnafUComponents
import me.udnek.fnafu.component.kit.Kit
import net.kyori.adventure.key.Key
import org.bukkit.NamespacedKey

class SpringtrapPlushtrapItem : ConstructableCustomItem() {
    override fun initializeComponents() {
        super.initializeComponents()
        components.set(AbilityIconFilesComponent(Kit.SPRINGTRAP))
        components.getOrCreateDefault(RPGUComponents.ACTIVE_ABILITY_ITEM).components.set(FnafUComponents.SPRINGTRAP_PLUSHTRAP_ABILITY.createNewDefault())
    }

    override fun getItemModel(): CustomItemProperties.DataSupplier<Key> =
        CustomItemProperties.DataSupplier.of(NamespacedKey(FnafU.instance, "springtrap/ability/plushtrap"))

    override fun getRawId(): String = "springtrap_plushtrap"
}
