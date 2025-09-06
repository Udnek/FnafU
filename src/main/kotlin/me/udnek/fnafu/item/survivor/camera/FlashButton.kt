package me.udnek.fnafu.item.survivor.camera

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import me.udnek.coreu.custom.component.instance.AutoGeneratingFilesItem
import me.udnek.coreu.custom.component.instance.TranslatableThing
import me.udnek.coreu.custom.item.ConstructableCustomItem
import me.udnek.coreu.custom.item.CustomItemProperties
import me.udnek.coreu.rpgu.component.RPGUComponents
import me.udnek.fnafu.FnafU
import me.udnek.fnafu.component.survivor.tablet.CameraFlashAbility
import net.kyori.adventure.key.Key
import org.bukkit.NamespacedKey

class FlashButton : ConstructableCustomItem(){

    override fun getRawId(): String = "flash_button"

    override fun getItemModel(): CustomItemProperties.DataSupplier<Key> =
        CustomItemProperties.DataSupplier.of(NamespacedKey(FnafU.instance, "tablet/button/flash"))

    override fun getTranslations(): TranslatableThing = TranslatableThing.ofEngAndRu("Flash", "Фспышка")

    override fun initializeComponents() {
        super.initializeComponents()
        components.getOrCreateDefault(RPGUComponents.ACTIVE_ABILITY_ITEM).components.set(CameraFlashAbility.DEFAULT)
        components.set(object : AutoGeneratingFilesItem.Generated(){
            override fun getModels(itemModel: Key): List<JsonObject?> {
                return listOf(JsonParser.parseString("""
                {
                	"parent": "item/generated",
	                "textures": {
		                "layer0": "fnafu:item/system/camera/button/background",
		                "layer1": "fnafu:item/system/camera/button/flash"
	                }
                }
                """).asJsonObject)
            }
        })
    }
}
