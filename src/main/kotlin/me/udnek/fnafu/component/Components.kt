package me.udnek.fnafu.component

import me.udnek.fnafu.FnafU
import me.udnek.fnafu.component.survivor.TabletComponent
import me.udnek.itemscoreu.customcomponent.ConstructableComponentType
import me.udnek.itemscoreu.customcomponent.CustomComponentType
import me.udnek.itemscoreu.customregistry.CustomRegistries

object Components {

    val KIT = register(ConstructableComponentType("kit", Kit.CAMERAMAN))
    val TABLET_COMPONENT = register(ConstructableComponentType("camera_component", TabletComponent.EMPTY))

    private fun <C : CustomComponentType<*, *>> register(type: C): C {
        return CustomRegistries.COMPONENT_TYPE.register(FnafU.instance, type)
    }

}
