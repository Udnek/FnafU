package me.udnek.fnafu.component

import me.udnek.coreu.custom.component.ConstructableComponentType
import me.udnek.coreu.custom.component.CustomComponentType
import me.udnek.coreu.custom.registry.CustomRegistries
import me.udnek.fnafu.FnafU
import me.udnek.fnafu.component.animatronic.SpringtrapCameraAbility
import me.udnek.fnafu.component.animatronic.SpringtrapPlushtrapAbility
import me.udnek.fnafu.component.survivor.SpectateEntityAbility

object Abilities {

    val SPECTATE_ENTITY = register(ConstructableComponentType("spectate_entity_ability", SpectateEntityAbility.DEFAULT) { SpectateEntityAbility() })

    val SPRINGTRAP_PLUSHTRAP = register(ConstructableComponentType("springtrap_plushtrap_ability", SpringtrapPlushtrapAbility.DEFAULT) { SpringtrapPlushtrapAbility() })
    val SPRINGTRAP_CAMERA = register(ConstructableComponentType("springtrap_camera_ability", SpringtrapCameraAbility.DEFAULT) { SpringtrapCameraAbility() })

    private fun <C : CustomComponentType<*, *>> register(type: C): C {
        return CustomRegistries.COMPONENT_TYPE.register(FnafU.instance, type)
    }
}
