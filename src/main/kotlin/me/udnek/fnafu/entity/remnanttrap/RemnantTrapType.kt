package me.udnek.fnafu.entity.remnanttrap

import me.udnek.coreu.custom.entitylike.entity.ConstructableCustomEntityType
import me.udnek.coreu.custom.entitylike.entity.CustomTickingEntityType
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.BlockDisplay
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType

class RemnantTrapType : ConstructableCustomEntityType<BlockDisplay>(), CustomTickingEntityType<RemnantTrap> {
    override fun getVanillaType() = EntityType.BLOCK_DISPLAY
    override fun getRawId() = "remnant_trap"
    override fun load(p0: Entity) {}
    override fun unload(p0: Entity) {}
    override fun createNewClass() = RemnantTrap()

    override fun spawnNewEntity(location: Location): BlockDisplay {
        val trap = super.spawnNewEntity(location)
        trap.block = Material.COPPER_BLOCK.createBlockData()
        return trap
    }
}