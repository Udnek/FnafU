package me.udnek.fnafu

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import me.udnek.coreu.custom.event.CustomEvent
import me.udnek.coreu.custom.event.CustomItemGeneratedEvent
import me.udnek.coreu.custom.event.ResourcepackInitializationEvent
import me.udnek.coreu.custom.registry.CustomRegistries
import me.udnek.coreu.mgu.MGUManager
import me.udnek.coreu.resourcepack.ResourcePackablePlugin
import me.udnek.coreu.resourcepack.path.VirtualRpJsonFile
import me.udnek.fnafu.block.Blocks
import me.udnek.fnafu.component.ComponentListener
import me.udnek.fnafu.component.FnafUComponents
import me.udnek.fnafu.game.EnergyGame
import me.udnek.fnafu.item.Items
import me.udnek.fnafu.item.decor.Poster
import me.udnek.fnafu.map.MapBuilder
import me.udnek.fnafu.misc.CoralFixer
import me.udnek.fnafu.sound.Sounds
import org.bukkit.Bukkit
import org.bukkit.GameRule
import org.bukkit.Location
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable

class FnafU : JavaPlugin(), ResourcePackablePlugin {

    companion object {
        lateinit var instance: FnafU
            private set
    }


    override fun onEnable() {
        instance = this

        FnafUComponents.SPRINGTRAP_PLUSHTRAP_DATA
        Items.FULL_CAMERA_TABLET
        Items.POSTER.RULES
        Sounds.AMBIENCE_FNAF1
        Blocks.ARCADE_MACHINE
        MapBuilder.REGISTRY


        ComponentListener(this)
        CoralFixer(this)
        Bukkit.getPluginManager().registerEvents(object: Listener{

            @EventHandler
            fun onResourcepack(event: ResourcepackInitializationEvent) {
                val body = JsonArray()

                CustomRegistries.ITEM.all.forEach { item ->
                    if (item !is Poster) return@forEach
                    body.add(JsonParser.parseString(
                        """
                        {"type": "bitmap", "ascent": 128, "height": 288,
                            "file": "fnafu:font/poster/${item.path}.png",
                            "chars": ["${item.fontChar}"]
                        }
                        """.trimIndent()
                    ))
                }

                val file = JsonObject()
                file.add("providers", body)
                event.addFile(VirtualRpJsonFile(file, "assets/fnafu/font/poster.json"))
            }
        }, this)

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
}
