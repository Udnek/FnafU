package me.udnek.fnafu.map.location

import com.google.common.base.Preconditions
import me.udnek.fnafu.util.setOrigin
import org.bukkit.*

class LocationSingle : LocationData {
    private var frozen = false

    private val location: Location

    override val all: List<Location>
        get() = ArrayList(setOf(location))

    override val first: Location
        get() = location.clone()

    override val random: Location
        get() = first

    override val size: Int = 1

    constructor(x: Double, y: Double, z: Double, yaw: Float, pitch: Float){
        location = Location(null, x, y, z, yaw, pitch)
    }

    constructor(x: Double, y: Double, z: Double): this(x, y, z, 0f, 0f)

    override fun get(n: Int): Location {
        return first
    }

    override fun setOrigin(origin: Location) {
        Preconditions.checkArgument(!frozen, "Location is frozen")
        location.setOrigin(origin)
        frozen = true
    }

    override val center: LocationSingle
        get() {
            Preconditions.checkArgument(!frozen, "Location is frozen")
            val center = location.toCenterLocation()
            location.set(center.x, center.y, center.z)
            return this
        }

    override val centerFloor: LocationSingle
        get() {
            Preconditions.checkArgument(!frozen, "Location is frozen")
            val center = location.toCenterLocation()
            location.set(center.x, center.blockY.toDouble(), center.z)
            return this
        }
}
