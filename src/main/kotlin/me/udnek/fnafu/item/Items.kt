package me.udnek.fnafu.item

import me.udnek.coreu.custom.item.CustomItem
import me.udnek.coreu.custom.registry.CustomRegistries
import me.udnek.fnafu.FnafU
import me.udnek.fnafu.item.animatronic.AnimatronicMask
import me.udnek.fnafu.item.animatronic.freddy.FreddySetTrapItem
import me.udnek.fnafu.item.animatronic.freddy.FreddyShadowItem
import me.udnek.fnafu.item.animatronic.freddy.FreddyTeleportToTrapItem
import me.udnek.fnafu.item.animatronic.springtrap.SpringtrapBreakCamerasItem
import me.udnek.fnafu.item.animatronic.springtrap.SpringtrapPlushtrapItem
import me.udnek.fnafu.item.survivor.SystemTablet
import me.udnek.fnafu.item.survivor.camera.CameraButton
import me.udnek.fnafu.item.survivor.camera.CutCameraTablet
import me.udnek.fnafu.item.survivor.camera.FullCameraTablet
import me.udnek.fnafu.item.survivor.doorman.DoormanTablet
import me.udnek.fnafu.item.survivor.doorman.DoormanTabletButton


object Items {
    val CAMERA_BUTTON: CustomItem = register(CameraButton())
    val CAMERA_OVERLAY: CustomItem = register(EmptyItem("camera_overlay"))

    val FULL_CAMERA_TABLET: CustomItem = register(FullCameraTablet())
    val CUT_CAMERA_TABLET: CustomItem = register(CutCameraTablet())

    val SYSTEM_TABLET: CustomItem = register(SystemTablet())

    val DOOR_TABLET: CustomItem = register(DoormanTablet())
    val DOOR_BUTTON: CustomItem = register(DoormanTabletButton())

    val UP_BUTTON: CustomItem = register(EmptyItem("up_button").hiddenModel())
    val DOWN_BUTTON: CustomItem = register(EmptyItem("down_button").hiddenModel())
    val ENTER_BUTTON: CustomItem = register(EmptyItem("enter_button").hiddenModel())
    val EXIT_BUTTON: CustomItem = register(EmptyItem("exit_button").hiddenModel())
    val CURSOR_ICON: CustomItem = register(EmptyItem("cursor"))
    val ERROR_ICON: CustomItem = register(EmptyItem("error"))
    val REBOOT_ICON: CustomItem = register(EmptyItem("reboot"))

    val SPRINGTRAP_PLUSHTRAP_ABILITY: CustomItem = register(SpringtrapPlushtrapItem())
    val SPRINGTRAP_BREAK_CAMERAS: CustomItem = register(SpringtrapBreakCamerasItem())
    val SPRINGTRAP_MASK: CustomItem = register(AnimatronicMask("springtrap"))

    val FREDDY_SHADOW: CustomItem = register(FreddyShadowItem())
    val FREDDY_MASK: CustomItem = register(AnimatronicMask("freddy"))
    val FREDDY_SET_TRAP: CustomItem = register(FreddySetTrapItem())
    val FREDDY_TELEPORT_TO_TRAP: CustomItem = register(FreddyTeleportToTrapItem())

    private fun register(customItem: CustomItem): CustomItem {
        return CustomRegistries.ITEM.register(FnafU.instance, customItem)
    }
}
