package me.udnek.fnafu.item.survivor.camera

import com.google.gson.JsonParser
import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.datacomponent.item.CustomModelData
import me.udnek.coreu.custom.component.instance.AutoGeneratingFilesItem
import me.udnek.coreu.custom.event.ResourcepackInitializationEvent
import me.udnek.coreu.custom.item.ConstructableCustomItem
import me.udnek.coreu.custom.item.CustomItemProperties
import me.udnek.coreu.resourcepack.path.VirtualRpJsonFile
import me.udnek.fnafu.FnafU
import me.udnek.fnafu.item.Items
import me.udnek.fnafu.mechanic.system.camera.Camera
import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component
import org.bukkit.Color
import org.bukkit.NamespacedKey
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

class CameraButton : ConstructableCustomItem(), Listener {
    companion object {
        val PDC_KEY: NamespacedKey = NamespacedKey(FnafU.instance, "camera_id")

        fun getWithCamera(camera: Camera, index: Int, color: Color): ItemStack {
            return Items.CAMERA_BUTTON.item.also {
                it.editPersistentDataContainer { container -> container.set(PDC_KEY, PersistentDataType.STRING, camera.id) }
                it.setData(DataComponentTypes.ITEM_NAME, Component.text(camera.id))
                it.setData(DataComponentTypes.CUSTOM_MODEL_DATA, CustomModelData.customModelData().addFloat(index.toFloat()).addColor(color))
            }
        }

        fun getCameraId(itemStack: ItemStack): String? {
            return itemStack.persistentDataContainer.get(PDC_KEY, PersistentDataType.STRING)
        }
    }

    override fun update(itemStack: ItemStack): ItemStack {return itemStack}

    override fun getRawId(): String = "camera_button"

    override fun getItemModel(): CustomItemProperties.DataSupplier<Key> =
        CustomItemProperties.DataSupplier.of(NamespacedKey(FnafU.instance, "tablet/button/camera"))

    @EventHandler
    fun onGenerate(event: ResourcepackInitializationEvent) {
        val model = """
                {
                	"parent": "item/generated",
	                "textures": {
		                "layer0": "fnafu:item/system/camera/button/background",
		                "layer1": "fnafu:item/system/camera/button/%number%"
	                }
                }
                """
        for (i in 0..12) {
            event.addFile(
                VirtualRpJsonFile(
                    JsonParser.parseString(model.replace("%number%", i.toString())).asJsonObject,
                    AutoGeneratingFilesItem.GENERATED.getModelPath(
                        NamespacedKey(FnafU.instance, "system/camera/button/$i")
                    )
                )
            )
        }

    }
}