package me.udnek.fnafu.component.survivor

import me.udnek.coreu.custom.component.CustomComponent
import me.udnek.coreu.custom.component.CustomComponentType
import me.udnek.coreu.mgu.component.MGUPlayerData
import me.udnek.coreu.mgu.component.MGUPlayerDataHolder
import me.udnek.fnafu.component.FnafUComponents
import me.udnek.fnafu.mechanic.system.camera.Camera
import me.udnek.fnafu.mechanic.system.camera.CameraMenu
import org.bukkit.inventory.ItemStack

open class SpectateCameraData: MGUPlayerData {

    companion object {
        val DEFAULT = object : SpectateCameraData() {
            override var lastCamera: Camera?
                get() = super.lastCamera
                set(value) {throwCanNotChangeDefault()}
            override var tablet: ItemStack?
                get() = super.tablet
                set(value) {throwCanNotChangeDefault()}
            override var cameraMenu: CameraMenu?
                get() = super.cameraMenu
                set(value) {throwCanNotChangeDefault()}
        }
    }

    open var lastCamera: Camera? = null
    open var tablet: ItemStack? = null
    open var cameraMenu: CameraMenu? = null

    override fun getType(): CustomComponentType<in MGUPlayerDataHolder, out CustomComponent<in MGUPlayerDataHolder>?> {
        return FnafUComponents.SPECTATE_CAMERA_DATA
    }

    override fun reset() {
        lastCamera = null
        tablet = null
    }
}
