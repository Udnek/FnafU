package me.udnek.fnafu.mechanic.camera

import me.udnek.fnafu.map.location.LocationSingle
import me.udnek.itemscoreu.customminigame.Originable
import org.bukkit.Location

class Camera : Originable {
    val location: LocationSingle
    val id: String
    val tabletMenuPosition: Int
    val rotationAngle: Float
    var number: Int = 0

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
