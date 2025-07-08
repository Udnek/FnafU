package me.udnek.fnafu.mechanic.system.door.door

import me.udnek.fnafu.map.location.LocationList
import me.udnek.fnafu.map.location.LocationSingle
import me.udnek.fnafu.mechanic.system.door.ButtonDoorPair
import me.udnek.fnafu.mechanic.system.door.DoorButton
import me.udnek.fnafu.misc.toCenterFloor
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.BlockFace
import org.bukkit.block.data.Bisected

class WoodenDoor(downLeftCorner: LocationSingle, private val direction: Direction, tabletMenuPosition: Int) : AbstractDoor(downLeftCorner, tabletMenuPosition) {

    override val stunCenter: Location
        get() {
            val xMul = when (direction){
                Direction.SOUTH -> 1
                Direction.NORTH -> 1
                else -> 0
            }
            val zMul = when (direction){
                Direction.EAST -> 1
                Direction.WEST -> 1
                else -> 0
            }
            return location.first.toCenterFloor().add(xMul*0.5, 0.0, zMul*0.5)
        }

    companion object{

        fun pairOf(x: Long,
                   y: Long,
                   z: Long,
                   direction: Direction,
                   tabletMenuPosition: Int,): ButtonDoorPair
        {
            val loc = Location(null, x.toDouble(), y.toDouble(), z.toDouble())

            val xMul = when (direction){
                Direction.SOUTH -> 1
                Direction.NORTH -> 1
                else -> 0
            }
            val zMul = when (direction){
                Direction.EAST -> 1
                Direction.WEST -> 1
                else -> 0
            }

            return ButtonDoorPair(WoodenDoor(LocationSingle(loc), direction, tabletMenuPosition),
                DoorButton(LocationList()
                    .add(loc.clone().add(0.0, 0.0, 0.0))
                    .add(loc.clone().add(0.0, 1.0, 0.0))
                    .add(loc.clone().add(1.0*xMul, 0.0, 1.0*zMul))
                    .add(loc.clone().add(1.0*xMul, 1.0, 1.0*zMul))
                ))
        }
    }

    override fun physicallyClose() = set(false)

    override fun physicallyOpen() = set(true)


    private fun set(open: Boolean){
        var xMul = 0
        var zMul = 0
        val face: BlockFace
        when (direction){
            Direction.SOUTH -> {
                face = BlockFace.SOUTH
                xMul = 1
            }
            Direction.WEST -> {
                face = BlockFace.WEST
                zMul = 1
            }
            Direction.NORTH -> {
                face = BlockFace.NORTH
                xMul = 1
            }
            Direction.EAST -> {
                face = BlockFace.EAST
                zMul = 1
            }
        }

        val location = location.first

        val doorData = Material.PALE_OAK_DOOR.createBlockData() as org.bukkit.block.data.type.Door
        doorData.facing = face
        doorData.isOpen = open

        doorData.hinge = org.bukkit.block.data.type.Door.Hinge.RIGHT
        doorData.half = Bisected.Half.BOTTOM
        location.clone().add(0.0, 0.0, 0.0).block.setBlockData(doorData, false)
        doorData.half = Bisected.Half.TOP
        location.clone().add(0.0, 1.0, 0.0).block.setBlockData(doorData, false)
        location.clone().add(0.0, 2.0, 0.0).block.setBlockData(doorData, false)

        doorData.hinge = org.bukkit.block.data.type.Door.Hinge.LEFT
        doorData.half = Bisected.Half.BOTTOM
        location.clone().add(1.0*xMul, 0.0, 1.0*zMul).block.setBlockData(doorData, false)
        doorData.half = Bisected.Half.TOP
        location.clone().add(1.0*xMul, 1.0, 1.0*zMul).block.setBlockData(doorData, false)
        location.clone().add(1.0*xMul, 2.0, 1.0*zMul).block.setBlockData(doorData, false)

//        val trapData = Material.PALE_OAK_TRAPDOOR.createBlockData() as org.bukkit.block.data.type.TrapDoor
//        trapData.facing = face
//        trapData.isOpen = true
//        location.clone().add(0.0, 2.0, 0.0).block.setBlockData(trapData)
//        location.clone().add(1.0*xMul, 2.0, 1.0*zMul).block.setBlockData(trapData)
    }


    enum class Direction {
        SOUTH,
        WEST,
        NORTH,
        EAST
    }

}
