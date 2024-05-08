package me.udnek.fnafu.ability;

import java.util.ArrayList;
import java.util.List;

public class AbilitiesHolder {

    private final List<Ability> abilities = new ArrayList<>();

    public void add(Ability ability){
        abilities.add(ability);
    }

    public void activate(Abilities rawAbility){
        Ability ability = getAbility(rawAbility);
        if (ability == null) return;
        ability.activate();
    }

    private Ability getAbility(Abilities rawAbility){
        return abilities.stream().filter(rawAbility::isAbility).findFirst().orElse(null);
    }

}
