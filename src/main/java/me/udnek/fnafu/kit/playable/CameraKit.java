package me.udnek.fnafu.kit.playable;

import me.udnek.fnafu.ability.playable.CameraTabletAbility;
import me.udnek.fnafu.item.Items;
import me.udnek.fnafu.kit.Kit;
import me.udnek.fnafu.player.FnafUPlayer;
import me.udnek.fnafu.player.type.Survivor;

public class CameraKit extends Kit {
    public CameraKit(FnafUPlayer player) {
        super(player);
    }

    @Override
    public void setUp() {
        regive();

        if (!(player instanceof Survivor)) return;
        Survivor survivor = (Survivor) player;

        survivor.getAbilitiesHolder().add(new CameraTabletAbility(survivor));
    }

    @Override
    public void regive() {
        player.give(Items.cameraTablet.getItem());
    }
}
