package me.udnek.fnafu.component.animatronic

import me.udnek.coreu.custom.component.CustomComponentType
import me.udnek.coreu.mgu.component.MGUPlayerData
import me.udnek.coreu.mgu.component.MGUPlayerDataHolder
import me.udnek.fnafu.component.FnafUComponents
import org.bukkit.Location

open class MovementTrackerComponent : MGUPlayerData {

    open var lastLocation: Location = Location(null, 0.0, 0.0, 0.0)

    companion object {
        val DEFAULT = object : MovementTrackerComponent(){
            override var lastLocation: Location
                get() = super.lastLocation
                set(value) {throwCanNotChangeDefault()}
        }
    }

    fun hasMoved(newLocation: Location): Boolean{
        return !(lastLocation.x == newLocation.x &&
                lastLocation.y == newLocation.y &&
                lastLocation.z == newLocation.z)
    }


    override fun getType(): CustomComponentType<out MGUPlayerDataHolder?, out MGUPlayerData> {
        return FnafUComponents.MOVEMENT_TRACKER_DATA
    }

    override fun reset() {
        lastLocation = Location(null, 0.0, 0.0, 0.0)
    }

}