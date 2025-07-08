package me.udnek.fnafu.misc

import me.udnek.coreu.util.ComponentU
import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component

object TextUtils {

    fun getMapImage(offset: Int, size: Int, name: String): Component {
        return ComponentU.textWithNoSpaceSpaceFont(
            offset,
            Component.translatable("map.fnafu.$name.image").font(Key.key("fnafu:map")),
            size
        )
    }
}
