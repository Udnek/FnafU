package me.udnek.fnafu.item.survivor.door

import me.udnek.coreu.custom.component.instance.RightClickableItem
import me.udnek.coreu.custom.item.ConstructableCustomItem
import me.udnek.coreu.custom.item.CustomItem
import me.udnek.coreu.custom.item.CustomItemProperties
import me.udnek.fnafu.util.getFnafU
import net.kyori.adventure.text.Component
import org.bukkit.event.player.PlayerInteractEvent

class DoorTablet: ConstructableCustomItem() {
    override fun getRawId(): String = "tablet/door"
    override fun getItemName(): CustomItemProperties.DataSupplier<Component> {
        return CustomItemProperties.DataSupplier.of(Component.translatable("item.fnafu.door_tablet"))
    }

    override fun initializeComponents() {
        super.initializeComponents()
        components.set(object : RightClickableItem {
            override fun onRightClick(customItem: CustomItem, event: PlayerInteractEvent) {
                event.item?.let {
                    event.player.getFnafU()?.let { player ->
                        player.game.systems.door.openMenu(player)
                    }
                }
            }
        })

    }
}