package me.udnek.fnafu.utils;

import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Predicate;

public class PredicateTest implements Predicate<Entity> {

    private final List<Entity> entities;

    public PredicateTest(List<Entity> entities){
        this.entities = entities;
    }

    @Override
    public boolean test(Entity entity) {
        return entities.contains(entity);
    }

    @NotNull
    @Override
    public Predicate<Entity> and(@NotNull Predicate<? super Entity> other) {
        return Predicate.super.and(other);
    }

    @NotNull
    @Override
    public Predicate<Entity> negate() {
        return Predicate.super.negate();
    }

    @NotNull
    @Override
    public Predicate<Entity> or(@NotNull Predicate<? super Entity> other) {
        return Predicate.super.or(other);
    }
}
