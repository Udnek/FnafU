package me.udnek.fnafu.mechanic.system.door.door

import me.udnek.fnafu.FnafU
import me.udnek.fnafu.map.location.LocationSingle
import me.udnek.fnafu.mechanic.system.door.ButtonDoorPair
import me.udnek.fnafu.mechanic.system.door.DoorButton
import me.udnek.fnafu.sound.Sounds
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.BlockFace
import org.bukkit.block.data.BlockData
import org.bukkit.block.data.type.Wall
import org.bukkit.scheduler.BukkitRunnable
import kotlin.math.floor

open class MetalDoor(location: LocationSingle, protected val direction: Direction, tabletMenuPosition: Int, protected val width: Int, protected val height: Int) : AbstractDoor(location, tabletMenuPosition) {

    companion object {

        const val CLOSE_TICKRATE: Int = 2
        const val OPEN_TICKRATE: Int = 8

        fun pairOf3x3(doorX: Long,
                      doorY: Long,
                      doorZ: Long,
                      direction: Direction,
                      tabletMenuPosition: Int,
                      buttonX: Long,
                      buttonY: Long,
                      buttonZ: Long): ButtonDoorPair {
            return pairOf(doorX, doorY, doorZ, direction, tabletMenuPosition, buttonX, buttonY, buttonZ, 3, 3)
        }

        fun pairOf2x3(doorX: Long,
                      doorY: Long,
                      doorZ: Long,
                      direction: Direction,
                      tabletMenuPosition: Int,
                      buttonX: Long,
                      buttonY: Long,
                      buttonZ: Long): ButtonDoorPair {
            return pairOf(doorX, doorY, doorZ, direction, tabletMenuPosition, buttonX, buttonY, buttonZ, 2, 3)
        }


        fun pairOf(doorX: Long,
                   doorY: Long,
                   doorZ: Long,
                   direction: Direction,
                   tabletMenuPosition: Int,
                   buttonX: Long,
                   buttonY: Long,
                   buttonZ: Long,
                   width: Int,
                   height: Int): ButtonDoorPair {
            return ButtonDoorPair(
                MetalDoor(
                    LocationSingle(doorX.toDouble(), doorY.toDouble(), doorZ.toDouble()),
                    direction,
                    tabletMenuPosition,
                    width, height
                ),
                DoorButton(LocationSingle(buttonX.toDouble(), buttonY.toDouble(), buttonZ.toDouble()))
            )
        }
    }

    protected fun getLayerBlockData(layer: Int): BlockData {
        return when {
            (layer == 0) -> direction.modifyBlockState(Material.RED_NETHER_BRICK_WALL.createBlockData() as Wall)
            (0 < layer && layer < height-1) -> direction.modifyBlockState(Material.NETHER_BRICK_WALL.createBlockData() as Wall)
            (layer == height-1) -> direction.modifyBlockState(Material.PRISMARINE_WALL.createBlockData() as Wall)
            else -> Material.AIR.createBlockData()
        }
    }

    protected fun physicallySwitch(close: Boolean){
        Sounds.DOOR.play(stunCenter)

        val xStep = if (direction == Direction.X) 1 else 0
        val zStep = if (direction == Direction.X) 0 else 1

        val range: (Int) -> IntRange = when (close){
            true -> { step: Int -> -height+1+step .. 0+step}
            else -> { step: Int -> -1-step .. height-2-step }
        }

        object : BukkitRunnable() {
            var step: Int = 0
            var doorLocation: Location = location.first

            override fun run() {
                doorLocation = location.first
                doorLocation.subtract((xStep * floor(width / 2.0 + 1)), 1.0, (zStep * floor(width / 2.0 + 1)))

                for (layer in range(step)) {
                    val blockData = getLayerBlockData(layer)

                    doorLocation.add(0.0, 1.0, 0.0)

                    repeat(width) {
                        doorLocation.add(xStep.toDouble(), 0.0, zStep.toDouble()).block.setBlockData(blockData)
                    }
                    doorLocation.subtract((xStep * width).toDouble(), 0.0, (zStep * width).toDouble())
                }

                if (step == height-1) cancel()
                step += 1
            }
        }.runTaskTimer(FnafU.instance, 0, (if (close) CLOSE_TICKRATE else OPEN_TICKRATE).toLong())
    }

    override fun physicallyClose() = physicallySwitch(true)

    override fun physicallyOpen() = physicallySwitch(false)

    enum class Direction {
        X {
            override fun modifyBlockState(blockData: Wall): Wall {
                blockData.setHeight(BlockFace.WEST, Wall.Height.TALL)
                blockData.setHeight(BlockFace.EAST, Wall.Height.TALL)
                blockData.isUp = false
                return blockData
            }
        },
        Z {
            override fun modifyBlockState(blockData: Wall): Wall {
                blockData.setHeight(BlockFace.SOUTH, Wall.Height.TALL)
                blockData.setHeight(BlockFace.NORTH, Wall.Height.TALL)
                blockData.isUp = false
                return blockData
            }
        };

        abstract fun modifyBlockState(blockData: Wall): Wall
    }
}
