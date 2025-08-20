package me.udnek.fnafu.entity.remnanttrap

import me.udnek.coreu.custom.entitylike.entity.ConstructableCustomEntityType
import me.udnek.coreu.custom.entitylike.entity.CustomTickingEntityType
import me.udnek.fnafu.item.Items
import org.bukkit.Location
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType
import org.bukkit.entity.Item
import org.bukkit.util.Vector

class RemnantTrapType : ConstructableCustomEntityType<Item>(), CustomTickingEntityType<RemnantTrap> {
    override fun getVanillaType() = EntityType.ITEM
    override fun getRawId() = "remnant_trap"
    override fun load(p0: Entity) {}
    override fun unload(p0: Entity) {}
    override fun createNewClass() = RemnantTrap()

    override fun spawnNewEntity(location: Location): Item {
        val trap = super.spawnNewEntity(location)
        trap.itemStack = Items.FREDDY_TRAP.item
        trap.isUnlimitedLifetime = true
        trap.setCanMobPickup(false)
        trap.setCanPlayerPickup(false)
        trap.velocity = Vector()
        trap.setGravity(false)
        return trap
    }
}