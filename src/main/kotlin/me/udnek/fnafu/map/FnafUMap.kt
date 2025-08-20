package me.udnek.fnafu.map

import com.google.common.base.Preconditions
import me.udnek.coreu.custom.registry.AbstractRegistrable
import me.udnek.coreu.mgu.map.MGUMap
import me.udnek.fnafu.map.location.LocationData
import me.udnek.fnafu.map.location.LocationSingle
import me.udnek.fnafu.mechanic.system.camera.Camera
import me.udnek.fnafu.mechanic.system.door.ButtonDoorPair
import me.udnek.fnafu.sound.LoopedSound
import net.kyori.adventure.text.Component
import org.bukkit.Location
import org.bukkit.block.BlockFace
import org.bukkit.inventory.ItemStack
import java.util.*

abstract class FnafUMap : MGUMap, AbstractRegistrable {

    private val origin: Location
    private val locations: EnumMap<LocationType, LocationData> = EnumMap<LocationType, LocationData>(LocationType::class.java)
    var doors: MutableList<ButtonDoorPair> = ArrayList()
    abstract var systemStationsAmount: Int
    lateinit var cameras: List<Camera>
    lateinit var systemStations: List<Pair<LocationSingle, BlockFace>>
    abstract var mapImage: Component
    abstract var ambientSound: LoopedSound
    abstract val icon: ItemStack

    constructor(origin: Location) {
        origin.set(origin.blockX.toDouble(), origin.blockY.toDouble(), origin.blockZ.toDouble())
        origin.pitch = 0f
        origin.yaw = 0f
        this.origin = origin

        this.build()
        this.doors = this.doors.sortedBy { it.door.tabletMenuPosition }.toMutableList()
        this.systemStations.forEach { it.first.setOrigin(origin) }
    }

    abstract fun build()

    ///////////////////////////////////////////////////////////////////////////
    // DOORS
    ///////////////////////////////////////////////////////////////////////////
    protected fun addDoor(doorButtonPair: ButtonDoorPair) {
        doorButtonPair.setOrigin(origin)
        doors.add(doorButtonPair)
    }


    ///////////////////////////////////////////////////////////////////////////
    // LOCATIONS
    ///////////////////////////////////////////////////////////////////////////
    protected fun addLocation(locationType: LocationType, locationData: LocationData) {
        Preconditions.checkArgument(
            !locations.containsKey(locationType),
            "LocationType $locationType already added!"
        )
        locationData.setOrigin(origin)
        locations[locationType] = locationData
    }

    fun getLocation(locationType: LocationType): LocationData? {
        return locations[locationType]
    }

    override fun getOrigin(): Location = origin.clone()
    ///////////////////////////////////////////////////////////////////////////
    // MISC
    ///////////////////////////////////////////////////////////////////////////

    abstract fun createFresh(): FnafUMap
}
