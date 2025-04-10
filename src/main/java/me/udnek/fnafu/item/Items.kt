package me.udnek.fnafu.item

import me.udnek.fnafu.FnafU
import me.udnek.itemscoreu.customitem.CustomItem
import me.udnek.itemscoreu.customregistry.CustomRegistries

object Items {
    val CAMERA_BUTTON: CustomItem = register(CameraButton())
    val CAMERA_TABLET: CustomItem = register(CameraTablet())
    val PLUSHTRAP: CustomItem = register(Plushtrap())

    private fun register(customItem: CustomItem): CustomItem {
        return CustomRegistries.ITEM.register(FnafU.instance, customItem)
    }
}
