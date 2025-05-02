package me.udnek.fnafu.item

import me.udnek.fnafu.util.getFnafU
import me.udnek.itemscoreu.customcomponent.instance.RightClickableItem
import me.udnek.itemscoreu.customitem.ConstructableCustomItem
import me.udnek.itemscoreu.customitem.CustomItem
import org.bukkit.event.player.PlayerInteractEvent

class SystemTablet: ConstructableCustomItem() {
    override fun getRawId(): String = "system_tablet"

    override fun initializeComponents() {
        super.initializeComponents()
        components.set(object : RightClickableItem{
            override fun onRightClick(customItem: CustomItem, event: PlayerInteractEvent) {
                event.item?.let {
                    event.player.getFnafU()?.let { player ->
                        player.game.map.system.openMenu(player)
                    }
                }
            }
        })

    }
}
