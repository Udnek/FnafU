package me.udnek.fnafu.item;

import me.udnek.fnafu.game.Game;
import me.udnek.fnafu.ability.Abilities;
import me.udnek.fnafu.manager.GameManager;
import me.udnek.itemscoreu.customitem.InteractableItem;
import org.bukkit.event.player.PlayerInteractEvent;

public interface AbilityItem extends InteractableItem {
    @Override
    default void onRightClicks(PlayerInteractEvent event){
        if (!event.getAction().isRightClick()) return;
        Game game = GameManager.getManager().getGame(event.getPlayer());
        if (game == null) return;
        game.getEventHandler().onPlayerActivatesAbility(event, getActivatingAbility());
    }

    Abilities getActivatingAbility();
}
