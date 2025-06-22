package me.udnek.fnafu.mechanic.system.door

import me.udnek.coreu.mgu.Originable
import me.udnek.fnafu.map.location.LocationSingle
import org.bukkit.Location

class DoorButton(private val button: LocationSingle) : Originable {
    override fun setOrigin(origin: Location) {
        button.setOrigin(origin)
    }

    val location: Location
        get() = button.first.clone()
}
