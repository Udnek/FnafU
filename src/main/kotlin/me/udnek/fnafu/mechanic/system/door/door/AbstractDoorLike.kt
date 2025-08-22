package me.udnek.fnafu.mechanic.system.door.door

import me.udnek.fnafu.map.location.LocationSingle
import me.udnek.fnafu.misc.toCenterFloor
import org.bukkit.Location

abstract class AbstractDoorLike(protected val location: LocationSingle, override val tabletMenuPosition: Int) : DoorLike{

    override var isClosed: Boolean = false
    override var isLocked: Boolean = false
    override val stunCenter: Location
        get() = location.first.toCenterFloor()
    override val debugLocation: Location
        get() = location.first

    override fun open() {
        if (!isClosed || isLocked) return
        isClosed = false
        physicallyOpen()
    }

    override fun close() {
        if (isClosed || isLocked) return
        isClosed = true
        physicallyClose()
    }

    protected abstract fun physicallyOpen()
    protected abstract fun physicallyClose()

    override fun setOrigin(p0: Location) = location.setOrigin(p0)

    override fun reset() {
        isLocked = false
        open()
        physicallyOpen()
    }

}














