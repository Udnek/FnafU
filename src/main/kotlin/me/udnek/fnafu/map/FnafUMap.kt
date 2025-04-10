package me.udnek.fnafu.map

import com.google.common.base.Preconditions
import me.udnek.fnafu.map.location.LocationData
import me.udnek.fnafu.mechanic.camera.CameraSystem
import me.udnek.fnafu.mechanic.door.Door
import me.udnek.fnafu.mechanic.door.DoorButtonPair
import me.udnek.fnafu.util.Resettable
import org.bukkit.Location
import java.util.*

abstract class FnafUMap : Resettable {

    val origin: Location
        get() = field.clone()

    private val locations: EnumMap<LocationType, LocationData> = EnumMap<LocationType, LocationData>(LocationType::class.java)
    val doors: MutableList<DoorButtonPair> = ArrayList()
    val cameraSystem: CameraSystem

    constructor(origin: Location) {
        origin.set(origin.blockX.toDouble(), origin.blockY.toDouble(), origin.blockZ.toDouble())
        origin.pitch = 0f
        origin.yaw = 0f
        this.origin = origin
        cameraSystem = CameraSystem()
        this.build()
        cameraSystem.setOrigin(origin)
    }

    abstract fun build()

    ///////////////////////////////////////////////////////////////////////////
    // DOORS
    ///////////////////////////////////////////////////////////////////////////
    protected fun addDoor(doorButtonPair: DoorButtonPair) {
        doorButtonPair.setOrigin(origin)
        doors.add(doorButtonPair)
    }


    fun getDoorByButtonLocation(location: Location): Door? {
        return doors.firstOrNull { it.hasButtonAt(location) }?.door
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
        Preconditions.checkArgument(
            locations.containsKey(locationType),
            "LocationType $locationType does not exists!"
        )
        return locations[locationType]
    }

    ///////////////////////////////////////////////////////////////////////////
    // MISC
    ///////////////////////////////////////////////////////////////////////////
    override fun reset() {
        cameraSystem.reset()
        for (door in doors) door.door.open()
    }
}
