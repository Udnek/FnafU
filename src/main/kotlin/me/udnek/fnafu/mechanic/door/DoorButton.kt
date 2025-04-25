package me.udnek.fnafu.mechanic.door

import me.udnek.fnafu.map.location.LocationSingle
import me.udnek.itemscoreu.custom.minigame.Originable
import org.bukkit.Location

class DoorButton(private val button: LocationSingle) : Originable {
    override fun setOrigin(origin: Location) {
        button.setOrigin(origin)
    }

    val location: Location
        get() = button.first.clone()
}
