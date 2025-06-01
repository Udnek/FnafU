package me.udnek.fnafu.item

import me.udnek.coreu.custom.component.instance.RightClickableItem
import me.udnek.coreu.custom.item.ConstructableCustomItem
import me.udnek.coreu.custom.item.CustomItem
import me.udnek.coreu.custom.item.CustomItemProperties
import me.udnek.fnafu.FnafU
import me.udnek.fnafu.component.Abilities
import me.udnek.fnafu.component.AbilityIconFilesComponent
import me.udnek.fnafu.component.Kit
import me.udnek.fnafu.util.getFnafU
import net.kyori.adventure.key.Key
import org.bukkit.NamespacedKey
import org.bukkit.event.player.PlayerInteractEvent

class Plushtrap : ConstructableCustomItem() {
    override fun initializeComponents() {
        super.initializeComponents()
        components.set(AbilityIconFilesComponent(Kit.SPRINGTRAP))
        components.set(object : RightClickableItem {
            override fun onRightClick(customItem: CustomItem, event: PlayerInteractEvent) {
                event.player.getFnafU()?.also {
                    it.abilities.getOrCreateDefault(Abilities.SPRINGTRAP_PLUSHTRAP).activate(it, event.item!!)
                }
            }
        })
    }

    override fun getItemModel(): CustomItemProperties.DataSupplier<Key> =
        CustomItemProperties.DataSupplier.of(NamespacedKey(FnafU.instance, "springtrap/ability/$rawId"))
    override fun getRawId(): String = "plushtrap"
}
