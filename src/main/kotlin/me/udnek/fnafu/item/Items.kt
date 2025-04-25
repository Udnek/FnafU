package me.udnek.fnafu.item

import me.udnek.fnafu.FnafU
import me.udnek.itemscoreu.customitem.CustomItem
import me.udnek.itemscoreu.customregistry.CustomRegistries

object Items {
    val CAMERA_BUTTON: CustomItem = register(CameraButton())
    val CAMERA_OVERLAY: CustomItem = register(CameraOverlay())

    val FULL_CAMERA_TABLET: CustomItem = register(FullCameraTablet())
    val CUT_CAMERA_TABLET: CustomItem = register(CutCameraTablet())

    val PLUSHTRAP: CustomItem = register(Plushtrap())

    private fun register(customItem: CustomItem): CustomItem {
        return CustomRegistries.ITEM.register(FnafU.instance, customItem)
    }
}
