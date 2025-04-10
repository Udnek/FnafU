package me.udnek.fnafu.mechanic.camera

import me.udnek.fnafu.util.Originable
import me.udnek.fnafu.map.location.LocationSingle
import org.bukkit.*

class Camera : Originable {
    val location: LocationSingle
    val id: String
    val tabletMenuPosition: Int
    val rotationAngle: Float

    constructor(location: LocationSingle, id: String, tabletMenuPosition: Int, rotationAngle: Float) {
        this.location = location
        this.id = id
        this.tabletMenuPosition = tabletMenuPosition
        this.rotationAngle = rotationAngle
    }

    override fun setOrigin(origin: Location) {
        location.setOrigin(origin)
    }
}
