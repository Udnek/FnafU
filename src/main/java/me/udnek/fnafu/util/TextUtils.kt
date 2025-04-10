package me.udnek.fnafu.util

import me.udnek.itemscoreu.util.ComponentU
import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor

object TextUtils {
    val defaultFont: Key = Key.key("default")

    fun getMapImage(offset: Int, size: Int, name: String): Component {
        return ComponentU.textWithNoSpace(
            offset,
            Component.translatable("fnafu.map.image.$name").font(Key.key("fnafu:map")).color(TextColor.color(1f, 1f, 1f)),
            size
        )
    }
}
