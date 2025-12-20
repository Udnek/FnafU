package me.udnek.fnafu.map

import me.udnek.coreu.custom.item.CustomItem
import me.udnek.coreu.custom.registry.AbstractRegistrable
import me.udnek.coreu.custom.registry.CustomRegistries
import me.udnek.coreu.custom.registry.CustomRegistry
import me.udnek.coreu.custom.registry.MappedCustomRegistry
import me.udnek.fnafu.FnafU
import me.udnek.fnafu.item.Items
import me.udnek.fnafu.map.instance.Faf30Map
import me.udnek.fnafu.map.instance.Fnaf1Map
import me.udnek.fnafu.map.instance.Fnaf3Map
import me.udnek.fnafu.map.instance.Fnaf4Map
import org.bukkit.Bukkit
import org.bukkit.Location

class MapBuilder(private val rawId: String, val icon: CustomItem, private val builder: () -> FnafUMap) : AbstractRegistrable() {

    companion object {
        val REGISTRY: CustomRegistry<MapBuilder> = CustomRegistries.addRegistry(FnafU.instance, MappedCustomRegistry("map_builder"))

        val FNAF1 = register(MapBuilder("fnaf1", Items.MAP_ICON_FNAF1){Fnaf1Map(Location(Bukkit.getWorld("fnaf")!!, -159.0, 65.0, -34.0))})
        val FNAF3 = register(MapBuilder("fnaf3", Items.MAP_ICON_FNAF3){Fnaf3Map(Location(Bukkit.getWorld("fnaf")!!, -202.0, 65.0, -139.0))})
        //val FNAF4 = register(MapBuilder("fnaf4", Items.MAP_ICON_FNAF4){Fnaf4Map(Location(Bukkit.getWorld("fnaf")!!, -97.0, 65.0, -109.0))})
        val FAF30 = register(MapBuilder("faf30", Items.VENDING_MACHINE){Faf30Map(Location(Bukkit.getWorld("fnaf")!!, -139.0, 65.0, -106.0))})

        private fun register(map: MapBuilder): MapBuilder{
            return REGISTRY.register(FnafU.instance, map);
        }

    }
    fun build(): FnafUMap = builder()
    override fun getRawId(): String = rawId
}
