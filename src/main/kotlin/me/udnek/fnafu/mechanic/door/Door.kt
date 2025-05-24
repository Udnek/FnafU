package me.udnek.fnafu.mechanic.door

import me.udnek.fnafu.FnafU
import me.udnek.fnafu.map.location.LocationSingle
import me.udnek.fnafu.util.Sounds
import me.udnek.itemscoreu.custom.minigame.Originable
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.block.BlockFace
import org.bukkit.block.data.BlockData
import org.bukkit.block.data.type.Wall
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.Vector

open class Door(protected val location: LocationSingle, private val direction: Direction) : Originable {

    companion object {
        const val CLOSE_DELAY: Int = 2
        const val OPEN_DELAY: Int = 8
    }

    var isClosed: Boolean = false
        private set

    fun toggle() = if (isClosed) open() else close()

    private fun getLayerBlockData(layer: Int): BlockData {
        return when (layer) {
            0 -> direction.modifyBlockState(Material.PRISMARINE_WALL.createBlockData() as Wall)
            1 -> direction.modifyBlockState(Material.NETHER_BRICK_WALL.createBlockData() as Wall)
            2 -> direction.modifyBlockState(Material.RED_NETHER_BRICK_WALL.createBlockData() as Wall)
            else -> Material.AIR.createBlockData()
        }
    }

    fun close() {
        if (isClosed) return
        isClosed = true

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

                for (layer in 2 - step until 2 - step + 3) {
                    val blockData = getLayerBlockData(layer)

                    doorLocation.add(0.0, -1.0, 0.0)
                    for (i in 0..2) {
                        world.setBlockData(doorLocation.add(xStep.toDouble(), 0.0, zStep.toDouble()), blockData)
                    }
                    doorLocation.add((-xStep * 3).toDouble(), 0.0, (-zStep * 3).toDouble())
                }

                if (step == 2) cancel()
                step += 1
            }
        }.runTaskTimer(FnafU.instance, 0, CLOSE_DELAY.toLong())
    }

    fun open() {
        if (!isClosed) return
        isClosed = false

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
                    for (i in 0..2) {
                        world.setBlockData(doorLocation.add(xStep.toDouble(), 0.0, zStep.toDouble()), blockData)
                    }
                    doorLocation.add((-xStep * 3).toDouble(), 0.0, (-zStep * 3).toDouble())
                }

                if (step == 2) cancel()
                step += 1
            }
        }.runTaskTimer(FnafU.instance, 0, OPEN_DELAY.toLong())
    }

    override fun setOrigin(origin: Location) {
        location.setOrigin(origin)
    }

    fun getLocation(): Location {
        return location.first
    }

    enum class Direction(vector: Vector) {
        X(Vector(1, 0, 0)) {
            override fun modifyBlockState(blockData: Wall): Wall {
                blockData.setHeight(BlockFace.WEST, Wall.Height.TALL)
                blockData.setHeight(BlockFace.EAST, Wall.Height.TALL)
                blockData.isUp = false
                return blockData
            }
        },
        Z(Vector(0, 0, 1)) {
            override fun modifyBlockState(blockData: Wall): Wall {
                blockData.setHeight(BlockFace.SOUTH, Wall.Height.TALL)
                blockData.setHeight(BlockFace.NORTH, Wall.Height.TALL)
                blockData.isUp = false
                return blockData
            }
        };

        private val vector = vector.normalize()

        val alignedVector: Vector
            get() = vector.clone()

        abstract fun modifyBlockState(blockData: Wall): Wall
    }


}
