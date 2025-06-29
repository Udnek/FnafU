package me.udnek.fnafu.entity.trap

import me.udnek.coreu.custom.entitylike.entity.ConstructableCustomEntityType
import me.udnek.coreu.custom.entitylike.entity.CustomTickingEntityType
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.BlockDisplay
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType

class TrapType : ConstructableCustomEntityType<BlockDisplay>(), CustomTickingEntityType<Trap> {
    override fun getVanillaType() = EntityType.BLOCK_DISPLAY
    override fun getRawId() = "trap"
    override fun load(p0: Entity) {}
    override fun unload(p0: Entity) {}
    override fun createNewClass() = Trap()

    override fun spawnNewEntity(location: Location): BlockDisplay {
        val trap = super.spawnNewEntity(location)
        trap.block = Material.COPPER_BLOCK.createBlockData()
        return trap
    }
}