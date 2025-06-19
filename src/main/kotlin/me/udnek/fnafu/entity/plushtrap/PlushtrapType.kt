package me.udnek.fnafu.entity.plushtrap

import me.udnek.coreu.custom.entitylike.entity.ConstructableCustomEntityType
import me.udnek.coreu.custom.entitylike.entity.CustomTickingEntityType
import me.udnek.fnafu.entity.EntityTypes
import org.bukkit.Location
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Drowned
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.loot.LootTables

class PlushtrapType : ConstructableCustomEntityType<Drowned>(), CustomTickingEntityType<Plushtrap>, Listener {

    override fun getVanillaType(): EntityType { return EntityType.DROWNED }

    override fun getRawId(): String { return "plushtrap" }

    override fun spawnNewEntity(location: Location): Drowned {
        val plushtrap = super.spawnNewEntity(location)
        plushtrap.isAware = false
        plushtrap.setBaby()
        plushtrap.getAttribute(Attribute.STEP_HEIGHT)!!.baseValue = 0.5
        plushtrap.getAttribute(Attribute.JUMP_STRENGTH)!!.baseValue = 0.0
        plushtrap.equipment.clear()
        plushtrap.lootTable = LootTables.BEE.lootTable
        plushtrap.getAttribute(Attribute.MOVEMENT_SPEED)!!.baseValue = 0.175
        if (plushtrap.isInsideVehicle) {
            val vehicle = plushtrap.vehicle
            plushtrap.leaveVehicle()
            vehicle!!.remove()
        }
        return plushtrap
    }

    @EventHandler
    fun onEntityHit(event: EntityDamageByEntityEvent) {
        val plushtrap: Plushtrap = EntityTypes.PLUSHTRAP.getIsThis(event.entity) ?: return
        plushtrap.onEntityHit(event)
    }

    override fun load(entity: Entity) {}

    override fun unload(entity: Entity) {}

    override fun createNewClass(): Plushtrap { return Plushtrap()}
}