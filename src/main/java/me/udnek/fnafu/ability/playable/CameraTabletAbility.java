package me.udnek.fnafu.ability.playable;

import me.udnek.fnafu.ability.Ability;
import me.udnek.fnafu.player.FnafUPlayer;
import me.udnek.fnafu.player.type.Survivor;

public class CameraTabletAbility extends Ability<Survivor> {
    public CameraTabletAbility(Survivor player) {
        super(player);
    }

    @Override
    public void activate() {
        player.getGame().getMap().getCameraSystem().openMenu(player);
    }
}
