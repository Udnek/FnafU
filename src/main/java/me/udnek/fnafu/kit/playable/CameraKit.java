package me.udnek.fnafu.kit.playable;

import me.udnek.fnafu.ability.playable.CameraTabletAbility;
import me.udnek.fnafu.item.Items;
import me.udnek.fnafu.kit.Kit;
import me.udnek.fnafu.player.type.Survivor;

public class CameraKit extends Kit<Survivor> {
    @Override
    public void setUp() {
        player.give(Items.cameraTablet.getItem());
        player.getAbilitiesHolder().add(new CameraTabletAbility(player));
    }
}
