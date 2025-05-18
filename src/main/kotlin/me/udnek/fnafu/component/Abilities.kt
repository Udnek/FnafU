package me.udnek.fnafu.component

import me.udnek.fnafu.FnafU
import me.udnek.fnafu.component.animatronic.SpringtrapPlushtrapAbility
import me.udnek.fnafu.component.survivor.SpectateEntityAbility
import me.udnek.itemscoreu.customcomponent.ConstructableComponentType
import me.udnek.itemscoreu.customcomponent.CustomComponentType
import me.udnek.itemscoreu.customregistry.CustomRegistries

object Abilities {

    val SPECTATE_ENTITY = register(ConstructableComponentType("spectate_entity_ability", SpectateEntityAbility.DEFAULT) { SpectateEntityAbility() })

    val SPRINGTRAP_PLUSHTRAP = register(ConstructableComponentType("springtrap_plushtrap_ability", SpringtrapPlushtrapAbility.DEFAULT) { SpringtrapPlushtrapAbility() })

    private fun <C : CustomComponentType<*, *>> register(type: C): C {
        return CustomRegistries.COMPONENT_TYPE.register(FnafU.instance, type)
    }
}
