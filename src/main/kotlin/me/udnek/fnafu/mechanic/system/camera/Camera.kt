package me.udnek.fnafu.mechanic.system.camera

import me.udnek.coreu.mgu.Originable
import me.udnek.fnafu.map.location.LocationSingle
import org.bukkit.Location

class Camera : Originable {
    val location: LocationSingle
    val id: String
    val tabletMenuPosition: Int
    val rotationAngle: Float
    var number: Int = 0
    val isInCutMenu: Boolean

    constructor(location: LocationSingle, id: String, tabletMenuPosition: Int, rotationAngle: Float, isCut: Boolean) {
        this.location = location
        this.id = id
        this.tabletMenuPosition = tabletMenuPosition
        this.rotationAngle = rotationAngle
        this.isInCutMenu = isCut
    }

    constructor(location: LocationSingle, id: String, tabletMenuPosition: Int, isCut: Boolean):
            this(location, id, tabletMenuPosition, 0f, isCut)

    override fun setOrigin(origin: Location) {
        location.setOrigin(origin)
    }
}
