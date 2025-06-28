package me.udnek.fnafu.mechanic.system.door

import me.udnek.coreu.mgu.Originable
import me.udnek.fnafu.mechanic.system.door.door.Door
import me.udnek.fnafu.util.Resettable
import org.bukkit.Location

class ButtonDoorPair(val door: Door, val button: DoorButton) : Originable, Resettable {

    override fun setOrigin(origin: Location) {
        door.setOrigin(origin)
        button.setOrigin(origin)
    }

    override fun reset() {
        door.reset()
    }
}
