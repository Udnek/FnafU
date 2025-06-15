package me.udnek.fnafu.item

import com.google.gson.JsonParser
import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.datacomponent.item.CustomModelData
import me.udnek.coreu.custom.component.instance.AutoGeneratingFilesItem
import me.udnek.coreu.custom.event.ResourcepackInitializationEvent
import me.udnek.coreu.custom.item.ConstructableCustomItem
import me.udnek.coreu.resourcepack.path.VirtualRpJsonFile
import me.udnek.fnafu.FnafU
import me.udnek.fnafu.mechanic.door.Door
import net.kyori.adventure.text.Component
import org.bukkit.Color
import org.bukkit.NamespacedKey
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.inventory.ItemStack

class DoorButton : ConstructableCustomItem(), Listener {
    companion object {
        fun getWithCamera(door: Door, index: Int): ItemStack {
            val color: Color
            if (door.isClosed) color = Color.BLACK
            else color = Color.WHITE
            return Items.DOOR_BUTTON.item.also {
                it.setData(DataComponentTypes.ITEM_NAME, Component.text(index))
                it.setData(DataComponentTypes.CUSTOM_MODEL_DATA, CustomModelData.customModelData().addFloat(index.toFloat()).addColor(color))
            }
        }
    }

    override fun update(itemStack: ItemStack): ItemStack = itemStack

    override fun getRawId(): String = "door_button"

    @EventHandler
    fun onGenerate(event: ResourcepackInitializationEvent) {
        val model = """
                {
                	"parent": "item/generated",
	                "textures": {
		                "layer0": "fnafu:item/door/button/background",
		                "layer1": "fnafu:item/door/button/%number%"
	                }
                }
                """
        for (i in 0..12) {
            event.addFile(
                VirtualRpJsonFile(
                    JsonParser.parseString(model.replace("%number%", i.toString())).asJsonObject,
                    AutoGeneratingFilesItem.GENERATED.getModelPath(
                        NamespacedKey(FnafU.instance, "door/button/$i")
                    )
                )
            )
        }
    }
}