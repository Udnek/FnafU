package me.udnek.fnafu.ability;

import me.udnek.fnafu.game.event.Event;
import me.udnek.fnafu.player.FnafUPlayer;

import java.util.ArrayList;
import java.util.List;

public class AbilitiesHolder<T extends FnafUPlayer>{

    private final List<Ability<T>> abilities = new ArrayList<>();

    public void add(Ability<T> ability){
        abilities.add(ability);
    }

    public void activate(Abilities rawAbility){
        Ability<T> ability = getAbility(rawAbility);
        if (ability == null) return;
        ability.activate();
    }

    private Ability<T> getAbility(Abilities rawAbility){
        return abilities.stream().filter(rawAbility::isAbility).findFirst().orElse(null);
    }

    public void handleEvent(Event event){
        abilities.forEach(ability -> ability.handleEvent(event));
    }

}
