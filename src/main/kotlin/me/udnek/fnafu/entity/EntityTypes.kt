package me.udnek.fnafu.entity

import me.udnek.coreu.custom.entitylike.entity.CustomEntityType
import me.udnek.coreu.custom.entitylike.entity.CustomTickingEntityType
import me.udnek.coreu.custom.registry.CustomRegistries
import me.udnek.fnafu.FnafU
import me.udnek.fnafu.entity.plushtrap.Plushtrap
import me.udnek.fnafu.entity.plushtrap.PlushtrapType
import me.udnek.fnafu.entity.remnanttrap.RemnantTrap
import me.udnek.fnafu.entity.remnanttrap.RemnantTrapType

object EntityTypes {
    val PLUSHTRAP = register<CustomTickingEntityType<Plushtrap>>(PlushtrapType())
    val REMNANT_TRAP = register<CustomTickingEntityType<RemnantTrap>>(RemnantTrapType())

    private fun <T : CustomEntityType> register(customEntityType: T): T {
        return CustomRegistries.ENTITY_TYPE.register(FnafU.instance, customEntityType)
    }
}
