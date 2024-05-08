package me.udnek.fnafu.ability;

import me.udnek.fnafu.player.FnafUPlayer;

public abstract class Ability<T extends FnafUPlayer>{

    protected final T player;
    public Ability(T player){
        this.player = player;
    }
    public abstract void activate();
}
