package me.udnek.fnafu.mechanic.system.ventilation

import me.udnek.fnafu.map.location.LocationSingle
import me.udnek.fnafu.mechanic.system.doorlike.AbstractDoorLike
import org.bukkit.Material
import org.bukkit.block.data.BlockData
import org.bukkit.block.data.type.TrapDoor

class TrapdoorVent(
    location: LocationSingle,
    protected val face: Direction,
    tabletMenuPosition: Int,
    protected val width: Int
) : AbstractDoorLike(location, tabletMenuPosition), Vent {

    override fun physicallyOpen() = physicallySet(false)
    override fun physicallyClose() = physicallySet(true)

    private fun physicallySet(open: Boolean) {
        val startLocation = location.first
        for (step in 0..<width) {
            val append = face.getAppend()
            val appendX = append.first * step
            val appendZ = append.second * step
            println(listOf( appendX, appendZ))
            val location = startLocation.clone().add(appendX, 0.0, appendZ)
            val data = location.block.blockData as? TrapDoor
            data?.isOpen = open
            location.block.blockData = data ?: getStandardData(open)
            val upLocation = startLocation.clone().add(appendX, 1.0, appendZ)
            val upData = upLocation.block.blockData as? TrapDoor
            upData?.isOpen = open
            upLocation.block.blockData = upData ?: getStandardData(open)
        }
    }

    private fun getStandardData(open: Boolean): BlockData {
        val blockData = Material.IRON_TRAPDOOR.createBlockData() as TrapDoor
        blockData.isOpen = open
        return blockData
    }

    enum class Direction {
        X {
            override fun getAppend(): Pair<Double, Double> {
                return Pair(1.0, 0.0)
            }
        },
        Z {
            override fun getAppend(): Pair<Double, Double> {
                return  Pair(0.0, 1.0)
            }
        };

        abstract fun getAppend(): Pair<Double, Double>
    }
}