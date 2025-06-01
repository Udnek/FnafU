package me.udnek.fnafu.entity

import me.udnek.coreu.custom.entitylike.entity.CustomEntityType
import me.udnek.coreu.custom.entitylike.entity.CustomTickingEntityType
import me.udnek.coreu.custom.registry.CustomRegistries
import me.udnek.fnafu.FnafU
import me.udnek.fnafu.entity.plushtrap.Plushtrap
import me.udnek.fnafu.entity.plushtrap.PlushtrapType

object EntityTypes {
    val PLUSHTRAP = register<CustomTickingEntityType<Plushtrap>>(PlushtrapType())

    private fun <T : CustomEntityType> register(customEntityType: T): T {
        return CustomRegistries.ENTITY_TYPE.register(FnafU.instance, customEntityType)
    }
}
