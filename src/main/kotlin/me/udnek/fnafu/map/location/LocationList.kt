package me.udnek.fnafu.map.location

import com.google.common.base.Preconditions
import me.udnek.fnafu.util.setOrigin
import org.bukkit.*
import java.util.*

class LocationList : LocationData {
    private val locations: MutableList<Location> = ArrayList()
    private var frozen = false

    override val size: Int
        get() = locations.size
    override val all: List<Location>
        get() = ArrayList(locations)

    override val first: Location
        get() = locations[0].clone()

    override val random: Location
        get() {
            if (size == 1) return first
            return locations[Random().nextInt(size)].clone()
        }


    fun add(location: Location): LocationList {
        Preconditions.checkArgument(!frozen, "LocationList is frozen")
        locations.add(location)
        return this
    }

    fun add(x: Double, y: Double, z: Double): LocationList {
        add(Location(null, x, y, z, 0f, 0f))
        return this
    }

    fun add(x: Double, y: Double, z: Double, yaw: Float, pitch: Float): LocationList {
        add(Location(null, x, y, z, yaw, pitch))
        return this
    }

    override fun setOrigin(origin: Location) {
        Preconditions.checkArgument(!frozen, "LocationList is frozen")
        for (location in locations) location.setOrigin(origin)
        frozen = true
    }

    override fun get(n: Int): Location {
        return locations[n]
    }

    override val center: LocationList
        get() {
            Preconditions.checkArgument(!frozen, "LocationList is frozen")
            for (location in locations) {
                val center = location.toCenterLocation()
                location[center.x, center.y] = center.z
            }
            return this
        }

    override val centerFloor: LocationList
        get() {
            Preconditions.checkArgument(!frozen, "LocationList is frozen")
            for (location in locations) {
                val center = location.toCenterLocation()
                location[center.x, center.blockY.toDouble()] = center.z
            }
            return this
        }
}
