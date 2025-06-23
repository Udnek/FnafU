package me.udnek.fnafu.component

import me.udnek.coreu.custom.component.ConstructableComponentType
import me.udnek.coreu.custom.component.CustomComponentType
import me.udnek.coreu.custom.registry.CustomRegistries
import me.udnek.fnafu.FnafU
import me.udnek.fnafu.component.kit.Kit
import me.udnek.fnafu.component.springtrap.SpringtrapBreakCamerasAbility
import me.udnek.fnafu.component.springtrap.SpringtrapPlushtrapAbility
import me.udnek.fnafu.component.springtrap.SpringtrapPlushtrapData
import me.udnek.fnafu.component.survivor.*

object FnafUComponents {

    val KIT = register(ConstructableComponentType("kit", Kit.CAMERAMAN))
    val CAMERA_TABLET_ABILITY = register(ConstructableComponentType("camera_tablet_ability", CameraTabletAbility.FULL))
    val DOORMAN_TABLET_ABILITY = register(ConstructableComponentType("doorman_tablet_ability", DoormanTabletAbility.DEFAULT))
    val SYSTEM_TABLET_ABILITY = register(ConstructableComponentType("system_tablet_ability", SystemTabletAbility.DEFAULT))

    val SPECTATE_ENTITY_DATA = register(ConstructableComponentType("spectate_entity_data", SpectateEntityData.DEFAULT) { SpectateEntityData() })
    val SPECTATE_CAMERA_DATA = register(ConstructableComponentType("spectate_camera_data", SpectateCameraData.DEFAULT) { SpectateCameraData() })

    val SPRINGTRAP_PLUSHTRAP_ABILITY = register(ConstructableComponentType("springtrap_plushtrap_ability", SpringtrapPlushtrapAbility.DEFAULT))
    val SPRINGTRAP_PLUSHTRAP_DATA = register(ConstructableComponentType("springtrap_plushtrap_data", SpringtrapPlushtrapData.DEFAULT) { SpringtrapPlushtrapData() })
    val SPRINGTRAP_BREAK_CAMERAS_ABILITY = register(ConstructableComponentType("springtrap_breaks_camera_ability", SpringtrapBreakCamerasAbility.DEFAULT))





    private fun <C : CustomComponentType<*, *>> register(type: C): C {
        return CustomRegistries.COMPONENT_TYPE.register(FnafU.instance, type)
    }
}
