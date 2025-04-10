package me.udnek.fnafu.item

import io.papermc.paper.datacomponent.DataComponentTypes
import me.udnek.fnafu.FnafU
import me.udnek.fnafu.mechanic.camera.Camera
import me.udnek.itemscoreu.customitem.ConstructableCustomItem
import net.kyori.adventure.text.Component
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

fun ItemStack.getCameraId(): String? {
    return CameraButton.getCameraId(this)
}

class CameraButton : ConstructableCustomItem() {
    companion object {
        val PDC_KEY: NamespacedKey = NamespacedKey(FnafU.instance, "camera_id")

        fun getWithCamera(camera: Camera): ItemStack {
            return Items.CAMERA_BUTTON.item.also {
                item ->
                item.editPersistentDataContainer {
                    container -> container.set(PDC_KEY, PersistentDataType.STRING, camera.id)
                }
                item.setData(DataComponentTypes.ITEM_NAME, Component.text(camera.id))
            }
        }

        fun getCameraId(itemStack: ItemStack): String? {
            return itemStack.persistentDataContainer.get(PDC_KEY, PersistentDataType.STRING)
        }
    }

    override fun update(itemStack: ItemStack): ItemStack {return itemStack}

    override fun getRawId(): String = "camera_button"
}