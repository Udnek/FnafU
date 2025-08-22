package me.udnek.fnafu.map

import me.udnek.coreu.mgu.Resettable
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.data.BlockData
import org.bukkit.block.data.Directional
import org.bukkit.block.data.Levelled
import org.bukkit.block.data.Lightable
import org.bukkit.block.data.type.CopperBulb
import org.bukkit.block.data.type.Light

class MapLight(): Resettable {

    private var isOn: Boolean = true

    private val poses: MutableMap<Location, BlockData> = HashMap()

    fun cachedLightPoses(): Map<Location, BlockData> = poses

    fun tryAddLightPos(location: Location){
        if (!canBeTurnedOff(location.block.blockData)) return
        poses[location.clone()] = location.block.blockData
    }

    private fun getTurnedOff(blockData: BlockData): BlockData?{
        when (blockData.material) {
            Material.REDSTONE_LAMP -> return (blockData.clone() as Lightable).also { it.isLit = false }
            Material.LIGHT -> return (blockData.clone() as Levelled).also { it.level = 0 }
            // LAMP
            Material.MEDIUM_AMETHYST_BUD -> {
                return (Material.SMALL_AMETHYST_BUD.createBlockData() as Directional)
                    .also { it.facing = (blockData as Directional).facing }
            }
            else -> {}
        }
        if (blockData is CopperBulb) return (blockData.clone() as CopperBulb).also { it.isLit = false }
        return null
    }

    fun canBeTurnedOff(blockData: BlockData): Boolean{
        when (blockData.material) {
            Material.REDSTONE_LAMP -> return (blockData as Lightable).isLit
            Material.LIGHT -> return (blockData as Light).level > 0
            // LAMP
            Material.MEDIUM_AMETHYST_BUD -> return true
            else -> {}
        }
        if (blockData is CopperBulb) return blockData.isLit
        return false
    }

    fun turnOff(){
        if (!isOn) return
        isOn = false
        poses.forEach {
            val location = it.key
            val blockData = it.value
            val turnedOff = getTurnedOff(blockData) ?: return@forEach
            location.block.setBlockData(turnedOff, false)
        }
    }

    fun turnOn(){
        if (isOn) return
        isOn = true
        poses.forEach {
            val location = it.key
            val blockData = it.value
            location.block.setBlockData(blockData, false)
        }
    }

    override fun reset() = turnOn()
}










