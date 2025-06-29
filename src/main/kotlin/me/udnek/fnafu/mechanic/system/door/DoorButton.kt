package me.udnek.fnafu.mechanic.system.door

import me.udnek.coreu.mgu.Originable
import me.udnek.fnafu.map.location.LocationData
import org.bukkit.Location

class DoorButton(val locationData: LocationData) : Originable {
    override fun setOrigin(origin: Location) {
        locationData.setOrigin(origin)
    }

    fun hasAt(location: Location): Boolean{
        return locationData.all.find {
            it.blockX == location.blockX && it.blockY == location.blockY && it.blockZ == location.blockZ
        } != null
    }
}
