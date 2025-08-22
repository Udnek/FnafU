package me.udnek.fnafu.item.survivor.doorman

import com.google.gson.JsonParser
import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.datacomponent.item.CustomModelData
import io.papermc.paper.datacomponent.item.TooltipDisplay
import me.udnek.coreu.custom.component.instance.AutoGeneratingFilesItem
import me.udnek.coreu.custom.event.ResourcepackInitializationEvent
import me.udnek.coreu.custom.item.ConstructableCustomItem
import me.udnek.coreu.custom.item.CustomItemProperties
import me.udnek.coreu.resourcepack.path.VirtualRpJsonFile
import me.udnek.fnafu.FnafU
import me.udnek.fnafu.item.Items
import me.udnek.fnafu.mechanic.system.door.door.DoorLike
import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component
import org.bukkit.Color
import org.bukkit.NamespacedKey
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.inventory.ItemStack

class DoormanTabletButton : ConstructableCustomItem(), Listener {
    companion object {
        fun getWithDoor(door: DoorLike, index: Int): ItemStack {
            val color: Color = if (door.isClosed) Color.BLACK
            else Color.fromRGB(189, 75, 33)
            return Items.DOOR_BUTTON.item.also {
                it.setData(DataComponentTypes.ITEM_NAME, Component.text(index))
                it.setData(DataComponentTypes.CUSTOM_MODEL_DATA, CustomModelData.customModelData().addFloat(index.toFloat()).addColor(color))
            }
        }
    }

    override fun update(itemStack: ItemStack): ItemStack = itemStack

    override fun getRawId(): String = "doorman_tablet_button"

    override fun getItemModel(): CustomItemProperties.DataSupplier<Key> =
        CustomItemProperties.DataSupplier.of(NamespacedKey(FnafU.instance, "tablet/button/door"))

    override fun getTooltipDisplay() = CustomItemProperties.DataSupplier.of(TooltipDisplay.tooltipDisplay().hideTooltip(true).build())

    @EventHandler
    fun onGenerate(event: ResourcepackInitializationEvent) {
        val model = """
                {
                	"parent": "item/generated",
	                "textures": {
		                "layer0": "fnafu:item/survivor/door/button/background",
		                "layer1": "fnafu:item/survivor/door/button/%number%"
	                }
                }
                """
        for (i in 0..12) {
            event.addFile(
                VirtualRpJsonFile(
                    JsonParser.parseString(model.replace("%number%", i.toString())).asJsonObject,
                    AutoGeneratingFilesItem.GENERATED.getModelPath(
                        NamespacedKey(FnafU.instance, "survivor/door/button/$i")
                    )
                )
            )
        }
    }
}