package me.udnek.fnafu.item

import me.udnek.coreu.custom.component.instance.RightClickableItem
import me.udnek.coreu.custom.item.ConstructableCustomItem
import me.udnek.coreu.custom.item.CustomItem
import me.udnek.fnafu.util.getFnafU
import org.bukkit.event.player.PlayerInteractEvent

class SystemTablet: ConstructableCustomItem() {
    override fun getRawId(): String = "system_tablet"

    override fun initializeComponents() {
        super.initializeComponents()
        components.set(object : RightClickableItem{
            override fun onRightClick(customItem: CustomItem, event: PlayerInteractEvent) {
                event.item?.let {
                    event.player.getFnafU()?.let { player ->
                        player.game.systems.openMenu(player)
                    }
                }
            }
        })

    }
}
