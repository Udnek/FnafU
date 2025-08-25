package me.udnek.fnafu.item.survivor.ventilation

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
import me.udnek.fnafu.mechanic.system.ventilation.Vent
import me.udnek.fnafu.mechanic.system.ventilation.VentilationMenu
import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component
import org.bukkit.Color
import org.bukkit.NamespacedKey
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.inventory.ItemStack

class VentilationTabletButton : ConstructableCustomItem(), Listener {
    companion object {
        fun getWithVent(vent: Vent, index: Int): ItemStack {
            val color: Color = if (vent.isClosed) Color.fromRGB(VentilationMenu.ACTIVE_COLOR)
            else Color.fromRGB(VentilationMenu.INACTIVE_COLOR)
            return Items.VENTILATION_BUTTON.item.also {
                it.setData(DataComponentTypes.ITEM_NAME, Component.text(index))
                it.setData(DataComponentTypes.CUSTOM_MODEL_DATA, CustomModelData.customModelData().addFloat(index.toFloat()).addColor(color))
            }
        }
    }

    override fun update(itemStack: ItemStack): ItemStack = itemStack

    override fun getRawId(): String = "ventilation_tablet_button"

    override fun getItemModel(): CustomItemProperties.DataSupplier<Key> =
        CustomItemProperties.DataSupplier.of(NamespacedKey(FnafU.instance, "tablet/button/ventilation"))

    override fun getTooltipDisplay() = CustomItemProperties.DataSupplier.of(TooltipDisplay.tooltipDisplay().hideTooltip(true).build())

    @EventHandler
    fun onGenerate(event: ResourcepackInitializationEvent) {
        val model = """
                {
	                "format_version": "1.21.6",
	                "credit": "Made with Blockbench",
	                "textures": {
	                	"0": "fnafu:item/system/ventilation/button/background",
	                	"1": "fnafu:item/system/ventilation/button/%number%",
	                	"particle": "fnafu:item/system/ventilation/button/background"
	                },
	                "elements": [
	                	{
	                		"from": [0, 0, 9],
	                		"to": [16, 16, 9],
	                		"rotation": {"angle": 0, "axis": "y", "origin": [0, 0, 9]},
	                		"faces": {
	                			"north": {"uv": [0, 0, 16, 16], "texture": "#missing"},
	                			"east": {"uv": [0, 0, 0, 16], "texture": "#missing"},
	                			"south": {"uv": [0, 0, 16, 16], "texture": "#1", "tintindex": 0},
	                			"west": {"uv": [0, 0, 0, 16], "texture": "#missing"},
	                			"up": {"uv": [0, 0, 16, 0], "texture": "#missing"},
	                			"down": {"uv": [0, 0, 16, 0], "texture": "#missing"}
	                		}
	                	},
	                	{
	                		"from": [0, 0, 7],
	                		"to": [16, 16, 7],
	                		"rotation": {"angle": 0, "axis": "y", "origin": [0, 0, 7]},
	                		"faces": {
	                			"north": {"uv": [0, 0, 16, 16], "texture": "#missing"},
	                			"east": {"uv": [0, 0, 0, 16], "texture": "#missing"},
	                			"south": {"uv": [0, 0, 16, 16], "texture": "#0", "tintindex": 0},
	                			"west": {"uv": [0, 0, 0, 16], "texture": "#missing"},
	                			"up": {"uv": [0, 0, 16, 0], "texture": "#missing"},
	                			"down": {"uv": [0, 0, 16, 0], "texture": "#missing"}
	                		}
	                	}
	                ],
	                "gui_light": "front",
	                "display": {
	                	"thirdperson_righthand": {
	                		"translation": [0, 3, 1],
	                		"scale": [0.55, 0.55, 0.55]
	                	},
	                	"thirdperson_lefthand": {
	                		"translation": [0, 3, 1],
	                		"scale": [0.55, 0.55, 0.55]
	                	},
	                	"firstperson_righthand": {
	                		"rotation": [0, -90, 25],
	                		"translation": [1.13, 3.2, 1.13],
	                		"scale": [0.68, 0.68, 0.68]
	                	},
	                	"firstperson_lefthand": {
	                		"rotation": [0, -90, 25],
	                		"translation": [1.13, 3.2, 1.13],
	                		"scale": [0.68, 0.68, 0.68]
	                	},
	                	"ground": {
	                		"translation": [0, 2, 0],
	                		"scale": [0.5, 0.5, 0.5]
	                	},
	                	"head": {
	                		"rotation": [0, -180, 0],
	                		"translation": [0, 13, 7]
	                	},
	                	"fixed": {
	                		"rotation": [0, -180, 0]
	                	}
	                }
                }
                """
        for (i in 0..12) {
            event.addFile(
                VirtualRpJsonFile(
                    JsonParser.parseString(model.replace("%number%", i.toString())).asJsonObject,
                    AutoGeneratingFilesItem.GENERATED.getModelPath(
                        NamespacedKey(FnafU.instance, "system/ventilation/button/$i")
                    )
                )
            )
        }
    }
}