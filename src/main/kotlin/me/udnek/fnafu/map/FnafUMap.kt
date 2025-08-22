package me.udnek.fnafu.map

import com.google.common.base.Preconditions
import me.udnek.coreu.mgu.Resettable
import me.udnek.coreu.mgu.map.MGUMap
import me.udnek.fnafu.map.location.LocationData
import me.udnek.fnafu.map.location.LocationSingle
import me.udnek.fnafu.mechanic.system.camera.Camera
import me.udnek.fnafu.mechanic.system.door.ButtonDoorPair
import me.udnek.fnafu.mechanic.system.ventilation.TrapdoorVent
import me.udnek.fnafu.mechanic.system.ventilation.Vent
import me.udnek.fnafu.sound.LoopedSound
import net.kyori.adventure.text.Component
import org.bukkit.Location
import org.bukkit.block.BlockFace
import org.bukkit.util.BoundingBox
import java.util.*

abstract class FnafUMap : MGUMap, Resettable{

    private val origin: Location
    private val locations: EnumMap<LocationType, LocationData> = EnumMap<LocationType, LocationData>(LocationType::class.java)
    var doors: MutableList<ButtonDoorPair> = ArrayList()
    var vents: MutableList<Vent> = ArrayList()
    abstract var systemStationsAmount: Int
    lateinit var cameras: List<Camera>
    lateinit var systemStations: List<Pair<LocationSingle, BlockFace>>
    abstract var mapImage: Component
    abstract var ambientSound: LoopedSound
    val mapLight: MapLight
    val minBound: LocationSingle
    val maxBound: LocationSingle

    constructor(origin: Location) {
        origin.set(origin.blockX.toDouble(), origin.blockY.toDouble(), origin.blockZ.toDouble())
        origin.pitch = 0f
        origin.yaw = 0f
        this.origin = origin

        minBound = LocationSingle(relativeBounds().min)
        maxBound = LocationSingle(relativeBounds().max)

        this.build()
        minBound.setOrigin(origin)
        maxBound.setOrigin(origin)
        mapLight = compileLight()

        this.doors = this.doors.sortedBy { it.door.tabletMenuPosition }.toMutableList()
        this.systemStations.forEach { it.first.setOrigin(origin) }
    }

    protected abstract fun relativeBounds(): BoundingBox

    abstract fun build()


    protected fun compileLight(): MapLight{
        val mapLight = MapLight()
        val location = origin.clone()
        for (x in minBound.first.blockX..maxBound.first.blockX){
            for (y in minBound.first.blockY..maxBound.first.blockY){
                for (z in minBound.first.blockZ..maxBound.first.blockZ){
                    location.set(x.toDouble(), y.toDouble(), z.toDouble())
                    mapLight.tryAddLightPos(location)
                }
            }
        }
        return mapLight
    }

    ///////////////////////////////////////////////////////////////////////////
    // DOORS_LIKE
    ///////////////////////////////////////////////////////////////////////////
    protected fun addDoor(doorButtonPair: ButtonDoorPair) {
        doorButtonPair.setOrigin(origin)
        doors.add(doorButtonPair)
    }

    protected fun addVent(vent: TrapdoorVent) {
        vent.setOrigin(origin)
        vents.add(vent)
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

    override fun reset() {
        mapLight.reset()
    }
}
