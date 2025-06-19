package me.udnek.fnafu.mechanic.door

import me.udnek.coreu.mgu.Originable
import me.udnek.fnafu.map.location.LocationSingle
import org.bukkit.Location

class ButtonDoorPair(val door: Door, val button: DoorButton) : Originable {

    constructor(
        doorX: Long,
        doorY: Long,
        doorZ: Long,
        direction: Door.Direction,
        tabletMenuPosition: Int,
        buttonX: Long,
        buttonY: Long,
        buttonZ: Long
    ) : this(
        Door(LocationSingle(doorX.toDouble(), doorY.toDouble(), doorZ.toDouble()), direction, tabletMenuPosition),
        DoorButton(LocationSingle(buttonX.toDouble(), buttonY.toDouble(), buttonZ.toDouble()))
    )

    fun hasButtonAt(location: Location): Boolean {
        val buttonLocation = button.location
        return buttonLocation.blockX == location.blockX && buttonLocation.blockY == location.blockY && buttonLocation.blockZ == location.blockZ
    }

    override fun setOrigin(origin: Location) {
        door.setOrigin(origin)
        button.setOrigin(origin)
    }
}
