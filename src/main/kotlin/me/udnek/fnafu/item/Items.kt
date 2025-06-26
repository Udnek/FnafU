package me.udnek.fnafu.item

import me.udnek.coreu.custom.item.CustomItem
import me.udnek.coreu.custom.registry.CustomRegistries
import me.udnek.fnafu.FnafU
import me.udnek.fnafu.item.springtrap.SpringtrapBreakCamerasItem
import me.udnek.fnafu.item.springtrap.SpringtrapMask
import me.udnek.fnafu.item.springtrap.SpringtrapPlushtrapItem
import me.udnek.fnafu.item.survivor.SystemTablet
import me.udnek.fnafu.item.survivor.camera.CameraButton
import me.udnek.fnafu.item.survivor.camera.CutCameraTablet
import me.udnek.fnafu.item.survivor.camera.FullCameraTablet
import me.udnek.fnafu.item.survivor.doorman.DoormanTablet
import me.udnek.fnafu.item.survivor.doorman.DoormanTabletButton


object Items {
    @JvmField val CAMERA_BUTTON: CustomItem = register(CameraButton())
    @JvmField val CAMERA_OVERLAY: CustomItem = register(EmptyItem("camera_overlay"))

    @JvmField val FULL_CAMERA_TABLET: CustomItem = register(FullCameraTablet())
    @JvmField val CUT_CAMERA_TABLET: CustomItem = register(CutCameraTablet())

    @JvmField val SYSTEM_TABLET: CustomItem = register(SystemTablet())

    @JvmField val DOOR_TABLET: CustomItem = register(DoormanTablet())
    @JvmField val DOOR_BUTTON: CustomItem = register(DoormanTabletButton())

    @JvmField val UP_BUTTON: CustomItem = register(EmptyItem("up_button").hiddenModel())
    @JvmField val DOWN_BUTTON: CustomItem = register(EmptyItem("down_button").hiddenModel())
    @JvmField val ENTER_BUTTON: CustomItem = register(EmptyItem("enter_button").hiddenModel())
    @JvmField val EXIT_BUTTON: CustomItem = register(EmptyItem("exit_button").hiddenModel())
    @JvmField val CURSOR_ICON: CustomItem = register(EmptyItem("cursor"))
    @JvmField val ERROR_ICON: CustomItem = register(EmptyItem("error"))
    @JvmField val REBOOT_ICON: CustomItem = register(EmptyItem("reboot"))

    @JvmField val SPRINGTRAP_PLUSHTRAP_ABILITY: CustomItem = register(SpringtrapPlushtrapItem())
    @JvmField val SPRINGTRAP_BREAK_CAMERAS: CustomItem = register(SpringtrapBreakCamerasItem())
    @JvmField val SPRINGTRAP_MASK: CustomItem = register(SpringtrapMask())

    private fun register(customItem: CustomItem): CustomItem {
        return CustomRegistries.ITEM.register(FnafU.instance, customItem)
    }
}
