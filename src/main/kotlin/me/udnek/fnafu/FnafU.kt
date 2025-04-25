package me.udnek.fnafu

import me.udnek.fnafu.component.Components
import me.udnek.fnafu.game.EnergyGame
import me.udnek.fnafu.item.Items
import me.udnek.fnafu.map.instance.Fnaf1PizzeriaMap
import me.udnek.fnafu.util.ItemListener
import me.udnek.fnafu.util.Sounds
import me.udnek.itemscoreu.custom.minigame.MGUManager
import me.udnek.itemscoreu.resourcepack.ResourcePackablePlugin
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable

class FnafU : JavaPlugin(), ResourcePackablePlugin {
    override fun onEnable() {
        instance = this

        Components.CAMERA_COMPONENT
        Items.FULL_CAMERA_TABLET
        Sounds.AMBIENCE_FNAF1

        ItemListener(this)

        object : BukkitRunnable(){
            override fun run() {
                val origin = Location(Bukkit.getWorld("fnaf")!!, -224.0, 65.0, -3.0)
                MGUManager.get().registerGame(
                    EnergyGame(Fnaf1PizzeriaMap(origin)))
            }
        }.runTask(instance)
    }

    companion object {
        lateinit var instance: FnafU
            private set
    }
}
