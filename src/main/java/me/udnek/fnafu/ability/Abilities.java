package me.udnek.fnafu.ability;

import me.udnek.fnafu.ability.playable.CameraTabletAbility;

public enum Abilities {

    CAMERA_TABLET(CameraTabletAbility.class);

    private final Class<? extends Ability<?>> abilityClass;
    Abilities(Class<? extends Ability<?>> ability){
        this.abilityClass = ability;
    }
    public boolean isAbility(Ability<?> ability){
        return ability.getClass() == this.abilityClass;
    }
}
