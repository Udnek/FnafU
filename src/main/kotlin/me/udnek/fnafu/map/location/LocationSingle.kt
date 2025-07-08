package me.udnek.fnafu.map.location

import com.google.common.base.Preconditions
import me.udnek.fnafu.misc.setOrigin
import me.udnek.fnafu.misc.toCenterFloor
import org.bukkit.Location

class LocationSingle : LocationData {
    private var frozen = false

    private var location: Location

    override val all: List<Location>
        get() = ArrayList(setOf(location))

    override val first: Location
        get() = location.clone()

    override val random: Location
        get() = first

    override val size: Int = 1

    constructor(location: Location){
        this.location = location.clone()
    }

    constructor(x: Double, y: Double, z: Double, yaw: Float, pitch: Float) : this(Location(null, x, y, z, yaw, pitch))

    constructor(x: Int, y: Int, z: Int, yaw: Float, pitch: Float): this(x.toDouble(), y.toDouble(), z.toDouble(), yaw, pitch)

    constructor(x: Double, y: Double, z: Double): this(x, y, z, 0f, 0f)

    constructor(x: Int, y: Int, z: Int): this(x.toDouble(), y.toDouble(), z.toDouble())

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
            location = location.toCenterFloor()
            return this
        }

    override val head: LocationSingle
        get() {
            Preconditions.checkArgument(!frozen, "LocationList is frozen")
            location.add(0.0, 1.7, 0.0)
            return this
        }
}
