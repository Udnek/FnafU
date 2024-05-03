package me.udnek.fnafu.game;

import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public interface EventHandler{

    default void onPlayerAttacksEntity(EntityDamageByEntityEvent event){}
    default void onPlayerInteracts(PlayerInteractEvent event){}
    default void onPlayerDropsItem(PlayerDropItemEvent event){}
    default void onPlayerClicksInInventory(InventoryClickEvent event){}

}
