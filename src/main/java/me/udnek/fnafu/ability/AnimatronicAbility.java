package me.udnek.fnafu.ability;

import me.udnek.fnafu.player.type.Animatronic;
import org.bukkit.Material;

public abstract class AnimatronicAbility {

    protected final Animatronic player;
    public AnimatronicAbility(Animatronic animatronic){
        this.player = animatronic;
    }
    public abstract void activate();
    public void cooldown(){
        player.cooldownMaterial(getMaterial(), getCooldownTime());
    }
    public void infinityCooldown(){}
    public boolean isInCooldown(){
        return player.getCooldownMaterial(getMaterial()) != 0;
    }

    public abstract int getCooldownTime();
    public abstract Material getMaterial();
}
