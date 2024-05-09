package me.udnek.fnafu.utils;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

public class TextUtils {

    public static final String spaceKey = "space.";

    public static Component insertImage(int size, Component image){
        return image.append(TextUtils.space(size));
    }

    public static Component space(int n){
        return Component.translatable(spaceKey + n);
    }

    public static Component getMapImage(int size, String name){
        return insertImage(size,
                Component.translatable("map.image.fnafu."+name)
                        .color(TextColor.color(1f, 1f, 1f))
                        .font(Key.key("fnafu:map"))
        );
    }

}
