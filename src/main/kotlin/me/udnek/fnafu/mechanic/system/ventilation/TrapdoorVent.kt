package me.udnek.fnafu.mechanic.system.ventilation

import me.udnek.fnafu.map.location.LocationSingle
import me.udnek.fnafu.mechanic.system.doorlike.AbstractDoorLike
import me.udnek.fnafu.misc.toCenterFloor
import me.udnek.fnafu.sound.Sounds
import org.bukkit.Location
import org.bukkit.block.data.type.TrapDoor

class TrapdoorVent(
    location: LocationSingle,
    protected val direction: Direction,
    tabletMenuPosition: Int,
    protected val width: Int
) : AbstractDoorLike(location, tabletMenuPosition), Vent {

    override fun physicallyOpen() = physicallySet(false)
    override fun physicallyClose() = physicallySet(true)

    override val stunCenter: Location
        get() {
            val firstPoint = location.first.toCenterFloor()
            return firstPoint.add(direction.append(firstPoint.clone(), width-1)).multiply(0.5)
        }

    private fun physicallySet(open: Boolean) {
        val startLocation = location.first
        Sounds.VENT_TOGGLE.play(stunCenter)
        for (step in 0..<width) {
            for (y in 0..1){
                val location = direction
                    .append(startLocation.clone(), step)
                    .add(0.0, y.toDouble(), 0.0)

                (location.block.blockData as? TrapDoor)?.let {
                    it.isOpen = open
                    location.block.blockData = it
                }
            }
        }
    }

    enum class Direction {
        X {
            override fun append(location: Location, times: Int): Location {
                return location.add(times.toDouble(), 0.0, 0.0)
            }
        },
        Z {
            override fun append(location: Location, times: Int): Location {
                return location.add(0.0, 0.0, times.toDouble())
            }
        };
        abstract fun append(location: Location, times: Int): Location
    }
}