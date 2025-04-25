package me.udnek.fnafu.mechanic.door

import me.udnek.fnafu.map.location.LocationSingle
import me.udnek.itemscoreu.custom.minigame.Originable
import org.bukkit.Location

class ButtonDoorPair(val door: Door, val button: DoorButton) : Originable {

    constructor(
        doorX: Long,
        doorY: Long,
        doorZ: Long,
        direction: Door.Direction,
        buttonX: Long,
        buttonY: Long,
        buttonZ: Long
    ) : this(
        Door(LocationSingle(doorX.toDouble(), doorY.toDouble(), doorZ.toDouble()), direction),
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
