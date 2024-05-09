package me.udnek.fnafu.kit;

import me.udnek.fnafu.player.FnafUPlayer;

public abstract class Kit {

    protected final FnafUPlayer player;

    public Kit(FnafUPlayer player){
        this.player = player;
    }

    public abstract void setUp();
    public abstract void regive();
}
