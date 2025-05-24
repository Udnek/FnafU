package me.udnek.fnafu.item

import com.google.gson.JsonParser
import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.datacomponent.item.CustomModelData
import me.udnek.fnafu.FnafU
import me.udnek.fnafu.mechanic.camera.Camera
import me.udnek.itemscoreu.customcomponent.instance.AutoGeneratingFilesItem
import me.udnek.itemscoreu.customevent.ResourcepackInitializationEvent
import me.udnek.itemscoreu.customitem.ConstructableCustomItem
import me.udnek.itemscoreu.resourcepack.path.VirtualRpJsonFile
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

    @EventHandler
    fun onGenerate(event: ResourcepackInitializationEvent) {
        val model = """
                {
                	"parent": "item/generated",
	                "textures": {
		                "layer0": "fnafu:item/camera/button/background",
		                "layer1": "fnafu:item/camera/button/%number%"
	                }
                }
                """
        for (i in 0..12) {
            event.addFile(
                VirtualRpJsonFile(
                    JsonParser.parseString(model.replace("%number%", i.toString())).asJsonObject,
                    AutoGeneratingFilesItem.GENERATED.getModelPath(
                        NamespacedKey(FnafU.instance, "camera/button/$i")
                    )
                )
            )
        }

        /*var definition = """
                {
                  "model": {
                    "type": "minecraft:range_dispatch",
                    "property": "minecraft:custom_model_data",
                    "entries": [
                      {
                        "threshold": 0,
                        "model": {
                          "type": "minecraft:model",
                          "model": "fnafu:item/camera/button/0",
                          "tints": [
                            {
                              "type": "minecraft:custom_model_data",
                              "default": -1
                            }
                          ]
                        }
                      } 
                """
        val definitionReferense = """,{
                                     "threshold": %number%,
                                     "model": {
                                       "type": "minecraft:model",
                                       "model": "fnafu:item/camera/button/%number%",
                                       "tints": [
                                         {
                                           "type": "minecraft:custom_model_data",
                                           "default": -1
                                         }
                                       ]
                                     }
                                   }"""
        for (i in 1..12) {
            definition += definitionReferense.replace("%number%", i.toString())
        }
        definition += """]
                  }
                }"""
        println(definition)
        event.addFile(
            VirtualRpJsonFile(
                JsonParser.parseString(definition).asJsonObject,
                AutoGeneratingFilesItem.GENERATED.getDefinitionPath(
                    NamespacedKey(FnafU.instance, "camera_button")
                )
            )
        )*/
    }
}