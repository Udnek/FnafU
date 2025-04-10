package me.udnek.fnafu.item

import me.udnek.fnafu.util.getFnafU
import me.udnek.itemscoreu.customcomponent.instance.RightClickableItem
import me.udnek.itemscoreu.customitem.ConstructableCustomItem
import me.udnek.itemscoreu.customitem.CustomItem
import org.bukkit.event.player.PlayerInteractEvent

class CameraTablet : ConstructableCustomItem() {

    override fun initializeComponents() {
        super.initializeComponents()
        components.set(object : RightClickableItem {
            override fun onRightClick(customItem: CustomItem, event: PlayerInteractEvent) {
                event.player.getFnafU()?.let {
                    player ->
                    player.game.map.cameraSystem.let { system ->
                        system.spectateCamera(player, "main")
                        system.openMenu(player)
                    }
                }

            }
        })
    }

    override fun getRawId(): String = "camera_tablet"
}
