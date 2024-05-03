package me.udnek.fnafu.game;

import net.kyori.adventure.text.format.TextColor;

public enum GameWinner {
    SURVIVORS(TextColor.color(0f, 1f, 0f)),
    ANIMATRONICS(TextColor.color(1f, 0f, 0f)),
    NONE(TextColor.color(0.7f, 0.7f, 0.7f));


    public final TextColor color;
    GameWinner(TextColor color){
        this.color = color;
    }
}
