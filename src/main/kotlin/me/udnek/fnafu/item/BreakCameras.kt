package me.udnek.fnafu.item

import me.udnek.fnafu.FnafU
import me.udnek.fnafu.component.Abilities
import me.udnek.fnafu.component.AbilityIconFilesComponent
import me.udnek.fnafu.component.Kit
import me.udnek.fnafu.util.getFnafU
import me.udnek.itemscoreu.customcomponent.instance.RightClickableItem
import me.udnek.itemscoreu.customitem.ConstructableCustomItem
import me.udnek.itemscoreu.customitem.CustomItem
import me.udnek.itemscoreu.customitem.CustomItemProperties
import net.kyori.adventure.key.Key
import org.bukkit.NamespacedKey
import org.bukkit.event.player.PlayerInteractEvent

class BreakCameras: ConstructableCustomItem() {
    override fun getItemModel(): CustomItemProperties.DataSupplier<Key> =
        CustomItemProperties.DataSupplier.of(NamespacedKey(FnafU.instance, "springtrap/ability/$rawId"))
    override fun getRawId(): String = "break_cameras"

    override fun initializeComponents() {
        super.initializeComponents()
        components.set(AbilityIconFilesComponent(Kit.SPRINGTRAP))
        components.set(object : RightClickableItem{
            override fun onRightClick(customItem: CustomItem, event: PlayerInteractEvent) {
                event.item?.let {
                    event.player.getFnafU()?.let {
                        it.abilities.getOrCreateDefault(Abilities.SPRINGTRAP_CAMERA).activate(it, item)
                    }
                }
            }
        })

    }
}
