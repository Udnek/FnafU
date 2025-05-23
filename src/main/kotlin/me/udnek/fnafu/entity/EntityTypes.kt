package me.udnek.fnafu.entity

import me.udnek.fnafu.FnafU
import me.udnek.fnafu.entity.plushtrap.Plushtrap
import me.udnek.fnafu.entity.plushtrap.PlushtrapType
import me.udnek.itemscoreu.customentitylike.entity.CustomEntityType
import me.udnek.itemscoreu.customentitylike.entity.CustomTickingEntityType
import me.udnek.itemscoreu.customregistry.CustomRegistries

object EntityTypes {
    val PLUSHTRAP = register<CustomTickingEntityType<Plushtrap>>(PlushtrapType())

    private fun <T : CustomEntityType> register(customEntityType: T): T {
        return CustomRegistries.ENTITY_TYPE.register(FnafU.instance, customEntityType)
    }
}
