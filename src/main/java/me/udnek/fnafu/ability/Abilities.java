package me.udnek.fnafu.ability;

import me.udnek.fnafu.ability.playable.CameraUsingAbility;

public enum Abilities {

    CAMERA_TABLET(CameraUsingAbility.class);

    private final Class<? extends Ability<?>> abilityClass;
    Abilities(Class<? extends Ability<?>> ability){
        this.abilityClass = ability;
    }
    public boolean isAbility(Ability<?> ability){
        return ability.getClass() == this.abilityClass;
    }
}
