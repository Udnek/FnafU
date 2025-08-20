package me.udnek.fnafu.map

import me.udnek.coreu.custom.registry.CustomRegistries
import me.udnek.coreu.custom.registry.CustomRegistry
import me.udnek.coreu.custom.registry.MappedCustomRegistry
import me.udnek.fnafu.FnafU
import me.udnek.fnafu.map.instance.Fnaf1Map
import me.udnek.fnafu.map.instance.Fnaf3Map
import me.udnek.fnafu.map.instance.Fnaf4Map
import org.bukkit.Bukkit
import org.bukkit.Location

object Maps {

    val REGISTRY: CustomRegistry<FnafUMap> = CustomRegistries.addRegistry(FnafU.instance, MappedCustomRegistry("map"))

    val FNAF1 = register(Fnaf1Map(Location(Bukkit.getWorld("fnaf")!!, -159.0, 65.0, -34.0)))
    val FNAF3 = register(Fnaf3Map(Location(Bukkit.getWorld("fnaf")!!, -202.0, 65.0, -139.0)))
    val FNAF4 = register(Fnaf4Map(Location(Bukkit.getWorld("fnaf")!!, -97.0, 65.0, -109.0)))

    private fun register(map: FnafUMap): FnafUMap{
        return REGISTRY.register(FnafU.instance, map);
    }
}