package me.udnek.fnafu.util

import me.udnek.coreu.util.ComponentU
import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor

object TextUtils {

    fun getMapImage(offset: Int, size: Int, name: String): Component {
        return ComponentU.textWithNoSpaceSpaceFont(
            offset,
            Component.translatable("map.fnafu.$name.image").font(Key.key("fnafu:map")).color(TextColor.color(1f, 1f, 1f)),
            size
        )
    }
}
