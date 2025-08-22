package me.udnek.fnafu.item.decor

import me.udnek.coreu.custom.component.instance.BlockPlacingItem
import me.udnek.coreu.custom.item.ConstructableCustomItem
import me.udnek.coreu.custom.item.CustomItemProperties
import me.udnek.fnafu.FnafU
import me.udnek.fnafu.block.Blocks
import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.DyeColor
import org.bukkit.Material
import org.bukkit.NamespacedKey
import java.util.function.Consumer


class Poster(val path: String, val isGlowing: Boolean = false, val color: DyeColor = DyeColor.WHITE): ConstructableCustomItem(){

    companion object{
        val FONT: Key = NamespacedKey(FnafU.instance, "poster")
        var currentChar: Char = Char.MIN_VALUE

        fun nextChar(): Char{
            while (!currentChar.isLetterOrDigit()) currentChar++
            return currentChar++
        }
    }

    var fontChar: Char = nextChar()

    override fun getMaterial(): Material = Material.RED_STAINED_GLASS

    override fun getRawId(): String = "poster_${path.replace("/", "_")}"

    override fun getItemModel(): CustomItemProperties.DataSupplier<Key> {
        return CustomItemProperties.DataSupplier.of(NamespacedKey(FnafU.instance, "decor/poster"))
    }

    override fun getLore(consumer: Consumer<Component>) {
        consumer.accept(Component.text(fontChar)
            .font(FONT)
            .color(TextColor.color(color.color.asRGB()))
            .decoration(TextDecoration.ITALIC, false))
    }

    override fun getItemName(): CustomItemProperties.DataSupplier<Component> {
        return CustomItemProperties.DataSupplier.of(Component.text(
            "Poster '${rawId.split("_").joinToString(" "){str -> str.replaceFirstChar{ if (it.isLowerCase()) it.titlecase() else it.toString() }}}'"
        ))
    }

    override fun initializeComponents() {
        super.initializeComponents()
//        components.set(object : AutoGeneratingFilesItem.Generated(){
//            override fun replacePlaceHolders(data: String, itemModel: Key): String {
//                return data
//                    .replace("%namespace%", itemModel.namespace())
//                    .replace("%key%", itemModel.value())
//                    .replace("%texture_path%", itemModel.namespace() + ":font/poster/${path}")
//                    .replace("%model_path%", itemModel.namespace() + ":item/" + itemModel.value())
//            }
//        })
        components.set(BlockPlacingItem(Blocks.POSTER))
    }
}
