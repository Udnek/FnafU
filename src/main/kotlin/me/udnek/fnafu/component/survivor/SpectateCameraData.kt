package me.udnek.fnafu.component.survivor

import me.udnek.coreu.custom.component.CustomComponent
import me.udnek.coreu.custom.component.CustomComponentType
import me.udnek.coreu.mgu.ability.MGUPlayerDataHolder
import me.udnek.fnafu.component.FnafUComponents
import me.udnek.fnafu.mechanic.system.camera.Camera

open class SpectateCameraData: CustomComponent<MGUPlayerDataHolder> {

    companion object {
        val DEFAULT = object : SpectateCameraData() {
            override var lastCamera: Camera?
                get() = super.lastCamera
                set(value) {throwCanNotChangeDefault()}
        }
    }

    open var lastCamera: Camera? = null

    override fun getType(): CustomComponentType<out MGUPlayerDataHolder, out CustomComponent<MGUPlayerDataHolder>> {
        return FnafUComponents.SPECTATE_CAMERA_DATA
    }
}
