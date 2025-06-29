package me.udnek.fnafu

import me.udnek.coreu.mgu.MGUManager
import me.udnek.coreu.resourcepack.ResourcePackablePlugin
import me.udnek.fnafu.component.ComponentListener
import me.udnek.fnafu.component.FnafUComponents
import me.udnek.fnafu.game.EnergyGame
import me.udnek.fnafu.item.Items
import me.udnek.fnafu.map.instance.Fnaf1PizzeriaMap
import me.udnek.fnafu.sound.Sounds
import me.udnek.fnafu.util.CoralFixer
import me.udnek.fnafu.map.instance.Fnaf4PizzeriaMap
import me.udnek.fnafu.util.Sounds
import org.bukkit.Bukkit
import org.bukkit.GameRule
import org.bukkit.Location
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable

class FnafU : JavaPlugin(), ResourcePackablePlugin {
    override fun onEnable() {
        instance = this

        FnafUComponents.SPRINGTRAP_PLUSHTRAP_DATA
        Items.FULL_CAMERA_TABLET
        Sounds.AMBIENCE_FNAF1


        ComponentListener(this)
        CoralFixer(this)

        object : BukkitRunnable(){
            override fun run() {
                val world = Bukkit.getWorld("fnaf")!!
                MGUManager.get().registerGame(EnergyGame(Fnaf1PizzeriaMap(Location(world, -159.0, 65.0, -34.0))))
                MGUManager.get().registerGame(EnergyGame(Fnaf4PizzeriaMap(Location(world, -97.0, 65.0, -109.0))))
                world.setGameRule(GameRule.RANDOM_TICK_SPEED, 0)
                val origin = Location(world, -159.0, 65.0, -34.0)
                MGUManager.get().registerGame(
                    EnergyGame(Fnaf1PizzeriaMap(origin)))
            }
        }.runTask(instance)
    }

    override fun getPriority(): ResourcePackablePlugin.Priority = ResourcePackablePlugin.Priority.BASE

    companion object {
        lateinit var instance: FnafU
            private set
    }
}
