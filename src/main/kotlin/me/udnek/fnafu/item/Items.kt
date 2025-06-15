package me.udnek.fnafu.item

import me.udnek.coreu.custom.item.CustomItem
import me.udnek.coreu.custom.registry.CustomRegistries
import me.udnek.fnafu.FnafU
import me.udnek.fnafu.item.tablet.CutCameraTablet
import me.udnek.fnafu.item.tablet.Door
import me.udnek.fnafu.item.tablet.FullCameraTablet
import me.udnek.fnafu.item.tablet.System

object Items {
    val CAMERA_BUTTON: CustomItem = register(CameraButton())
    val CAMERA_OVERLAY: CustomItem = register(EmptyItem("camera_overlay"))

    val FULL_CAMERA_TABLET: CustomItem = register(FullCameraTablet())
    val CUT_CAMERA_TABLET: CustomItem = register(CutCameraTablet())

    val SYSTEM_TABLET: CustomItem = register(System())

    val DOOR_TABLET: CustomItem = register(Door())
    val DOOR_BUTTON: CustomItem = register(DoorButton())

    val UP_BUTTON: CustomItem = register(EmptyItem("up_button").hiddenModel())
    val DOWN_BUTTON: CustomItem = register(EmptyItem("down_button").hiddenModel())
    val ENTER_BUTTON: CustomItem = register(EmptyItem("enter_button").hiddenModel())
    val EXIT_BUTTON: CustomItem = register(EmptyItem("exit_button").hiddenModel())
    val CURSOR_ICON: CustomItem = register(EmptyItem("cursor"))
    val ERROR_ICON: CustomItem = register(EmptyItem("error"))
    val REBOOT_ICON: CustomItem = register(EmptyItem("reboot"))

    val PLUSHTRAP: CustomItem = register(Plushtrap())
    val BREAK_CAMERAS: CustomItem = register(BreakCameras())

    private fun register(customItem: CustomItem): CustomItem {
        return CustomRegistries.ITEM.register(FnafU.instance, customItem)
    }
}
