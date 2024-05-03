package me.udnek.fnafu.game;

import me.udnek.fnafu.manager.GameManager;
import me.udnek.itemscoreu.utils.SelfRegisteringListener;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class GameListener extends SelfRegisteringListener {

    private final GameManager gameManager = GameManager.getManager();

    public GameListener(JavaPlugin plugin) {
        super(plugin);
    }

    @EventHandler
    public void onEntityAttacksEntity(EntityDamageByEntityEvent event){
        Entity damager = event.getDamager();
        if (!(damager instanceof Player)) return;

        Game game = gameManager.getGame((Player) damager);
        if (game == null) return;

        game.getEventHandler().onPlayerAttacksEntity(event);
    }

    @EventHandler
    public void onPlayerInteracts(PlayerInteractEvent event){

        if (event.getAction() == Action.PHYSICAL) return;

        Game game = gameManager.getGame(event.getPlayer());
        if (game == null) return;

        game.getEventHandler().onPlayerInteracts(event);
    }
    @EventHandler
    public void onPLayerDropsItem(PlayerDropItemEvent event){
        Game game = gameManager.getGame(event.getPlayer());
        if (game == null) return;

        game.getEventHandler().onPlayerDropsItem(event);
    }

    @EventHandler
    public void onPLayerClicksInInventory(InventoryClickEvent event){
        Game game = gameManager.getGame((Player) event.getWhoClicked());
        if (game == null) return;

        game.getEventHandler().onPlayerClicksInInventory(event);
    }


}
