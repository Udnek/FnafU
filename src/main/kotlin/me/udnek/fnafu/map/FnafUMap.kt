package me.udnek.fnafu.map

import com.google.common.base.Preconditions
import me.udnek.coreu.custom.sound.CustomSound
import me.udnek.coreu.mgu.map.MGUMap
import me.udnek.fnafu.map.location.LocationData
import me.udnek.fnafu.mechanic.camera.Camera
import me.udnek.fnafu.mechanic.door.ButtonDoorPair
import me.udnek.fnafu.mechanic.door.Door
import me.udnek.fnafu.util.Resettable
import net.kyori.adventure.text.Component
import org.bukkit.Location
import java.util.*

abstract class FnafUMap : MGUMap, Resettable {

    private val origin: Location

    private val locations: EnumMap<LocationType, LocationData> = EnumMap<LocationType, LocationData>(LocationType::class.java)
    val doors: MutableList<ButtonDoorPair> = ArrayList()
    lateinit var cameras: List<Camera>
    lateinit var cameraImage: Component
    lateinit var ambientSound: CustomSound

    constructor(origin: Location) {
        origin.set(origin.blockX.toDouble(), origin.blockY.toDouble(), origin.blockZ.toDouble())
        origin.pitch = 0f
        origin.yaw = 0f
        this.origin = origin

        this.build()
    }

    abstract fun build()

    ///////////////////////////////////////////////////////////////////////////
    // DOORS
    ///////////////////////////////////////////////////////////////////////////
    protected fun addDoor(doorButtonPair: ButtonDoorPair) {
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

    override fun getOrigin(): Location = origin.clone()
    ///////////////////////////////////////////////////////////////////////////
    // MISC
    ///////////////////////////////////////////////////////////////////////////
    override fun reset() {
        for (door in doors) door.door.open()
    }
}
