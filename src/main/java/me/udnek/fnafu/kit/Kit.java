package me.udnek.fnafu.kit;

import me.udnek.fnafu.player.FnafUPlayer;

public abstract class Kit<T extends FnafUPlayer> {

    protected T player;
    public void setPlayer(T player) {this.player = player;}
    public abstract void setUp();
}
