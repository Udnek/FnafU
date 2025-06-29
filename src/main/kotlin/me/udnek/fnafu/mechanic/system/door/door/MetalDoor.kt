package me.udnek.fnafu.mechanic.system.door.door

import me.udnek.fnafu.FnafU
import me.udnek.fnafu.map.location.LocationSingle
import me.udnek.fnafu.mechanic.system.door.ButtonDoorPair
import me.udnek.fnafu.mechanic.system.door.DoorButton
import me.udnek.fnafu.util.Sounds
import me.udnek.fnafu.sound.Sounds
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.block.BlockFace
import org.bukkit.block.data.BlockData
import org.bukkit.block.data.type.Wall
import org.bukkit.scheduler.BukkitRunnable

open class MetalDoor(location: LocationSingle, private val direction: Direction, tabletMenuPosition: Int) : AbstractDoor(location, tabletMenuPosition) {

    companion object {

        const val CLOSE_TICKRATE: Int = 2
        const val OPEN_TICKRATE: Int = 8

        fun pairOf(doorX: Long,
                   doorY: Long,
                   doorZ: Long,
                   direction: Direction,
                   tabletMenuPosition: Int,
                   buttonX: Long,
                   buttonY: Long,
                   buttonZ: Long): ButtonDoorPair {
            return ButtonDoorPair(
                MetalDoor(
                    LocationSingle(doorX.toDouble(), doorY.toDouble(), doorZ.toDouble()),
                    direction,
                    tabletMenuPosition
                ),
                DoorButton(LocationSingle(buttonX.toDouble(), buttonY.toDouble(), buttonZ.toDouble()))
            )
        }
    }

    private fun getLayerBlockData(layer: Int): BlockData {
        return when (layer) {
            0 -> direction.modifyBlockState(Material.PRISMARINE_WALL.createBlockData() as Wall)
            1 -> direction.modifyBlockState(Material.NETHER_BRICK_WALL.createBlockData() as Wall)
            2 -> direction.modifyBlockState(Material.RED_NETHER_BRICK_WALL.createBlockData() as Wall)
            else -> Material.AIR.createBlockData()
        }
    }

    override fun physicallyClose() {
        Sounds.DOOR.play(location.first)

        val xStep = if (direction == Direction.X) 1 else 0
        val zStep = if (direction == Direction.X) 0 else 1

        object : BukkitRunnable() {
            var step: Int = 0
            var doorLocation: Location = location.first
            val world: World = doorLocation.world

            override fun run() {
                doorLocation = location.first
                doorLocation.add((-xStep * 2).toDouble(), 3.0, (-zStep * 2).toDouble())

                for (layer in 2 - step until 2 - step + 3) {
                    val blockData = getLayerBlockData(layer)

                    doorLocation.add(0.0, -1.0, 0.0)
                    repeat(3) { world.setBlockData(doorLocation.add(xStep.toDouble(), 0.0, zStep.toDouble()), blockData) }
                    doorLocation.add((-xStep * 3).toDouble(), 0.0, (-zStep * 3).toDouble())
                }

                if (step == 2) cancel()
                step += 1
            }
        }.runTaskTimer(FnafU.instance, 0, CLOSE_TICKRATE.toLong())
    }

    override fun physicallyOpen() {
        Sounds.DOOR.play(location.first)

        val xStep = if (direction === Direction.X) 1 else 0
        val zStep = if (direction === Direction.X) 0 else 1

        object : BukkitRunnable() {
            var step: Int = 0
            var doorLocation: Location = location.first
            val world: World = doorLocation.world

            override fun run() {
                doorLocation = location.first
                doorLocation.add((-xStep * 2).toDouble(), 3.0, (-zStep * 2).toDouble())

                for (layer in 1 + step until 1 + step + 3) {
                    val blockData = getLayerBlockData(layer)

                    doorLocation.add(0.0, -1.0, 0.0)
                    repeat(3) { world.setBlockData(doorLocation.add(xStep.toDouble(), 0.0, zStep.toDouble()), blockData) }
                    doorLocation.add((-xStep * 3).toDouble(), 0.0, (-zStep * 3).toDouble())
                }

                if (step == 2) cancel()
                step += 1
            }
        }.runTaskTimer(FnafU.instance, 0, OPEN_TICKRATE.toLong())
    }

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
