package me.udnek.fnafu.component

import me.udnek.coreu.custom.component.ConstructableComponentType
import me.udnek.coreu.custom.component.CustomComponentType
import me.udnek.coreu.custom.registry.CustomRegistries
import me.udnek.fnafu.FnafU
import me.udnek.fnafu.component.survivor.TabletComponent

object Components {

    val KIT = register(ConstructableComponentType("kit", Kit.CAMERAMAN))
    val TABLET_COMPONENT = register(ConstructableComponentType("camera_component", TabletComponent.EMPTY))

    private fun <C : CustomComponentType<*, *>> register(type: C): C {
        return CustomRegistries.COMPONENT_TYPE.register(FnafU.instance, type)
    }

}
