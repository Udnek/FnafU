package me.udnek.fnafu

import me.udnek.coreu.mgu.MGUManager
import me.udnek.coreu.resourcepack.ResourcePackablePlugin
import me.udnek.fnafu.block.Blocks
import me.udnek.fnafu.component.ComponentListener
import me.udnek.fnafu.component.FnafUComponents
import me.udnek.fnafu.game.EnergyGame
import me.udnek.fnafu.item.Items
import me.udnek.fnafu.map.Maps
import me.udnek.fnafu.sound.Sounds
import me.udnek.fnafu.misc.CoralFixer
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
        Blocks.ARCADE_MACHINE
        Maps.FNAF1


        ComponentListener(this)
        CoralFixer(this)

        object : BukkitRunnable(){
            override fun run() {
                val world = Bukkit.getWorld("fnaf")!!
                world.setGameRule(GameRule.RANDOM_TICK_SPEED, 0)
                MGUManager.get().registerGame(EnergyGame(
                    Location(world, -162.5, 71.0, -38.5, 0f, 0f),
                    Location(world, -162.5, 77.0, -38.5, 0f, 0f)
                ))
            }
        }.runTask(instance)
    }

    override fun getPriority(): ResourcePackablePlugin.Priority = ResourcePackablePlugin.Priority.BASE

    companion object {
        lateinit var instance: FnafU
            private set
    }
}
