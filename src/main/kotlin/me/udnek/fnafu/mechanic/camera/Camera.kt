package me.udnek.fnafu.mechanic.camera

import me.udnek.fnafu.map.location.LocationSingle
import me.udnek.itemscoreu.custom.minigame.Originable
import org.bukkit.Location

class Camera : Originable {
    val location: LocationSingle
    val id: String
    val tabletMenuPosition: Int
    val rotationAngle: Float
    var number: Int = 0
    val isCut: Boolean

    constructor(location: LocationSingle, id: String, tabletMenuPosition: Int, rotationAngle: Float, isCut: Boolean) {
        this.location = location
        this.id = id
        this.tabletMenuPosition = tabletMenuPosition
        this.rotationAngle = rotationAngle
        this.isCut = isCut
    }

    override fun setOrigin(origin: Location) {
        location.setOrigin(origin)
    }
}
