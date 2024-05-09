package me.udnek.fnafu.game;

import me.udnek.fnafu.ability.Abilities;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public interface EventHandler{
    default void onPlayerAttacksEntity(EntityDamageByEntityEvent event){}
    default void onPlayerInteracts(PlayerInteractEvent event){}
    default void onPlayerDropsItem(PlayerDropItemEvent event){}
    default void onPlayerClicksInInventory(InventoryClickEvent event){}
    default void onPlayerClicksInCameraMenu(InventoryClickEvent event){}
    default void onPlayerClosesCameraMenu(InventoryCloseEvent event){}
    default void onPlayerOpensCameraMenu(InventoryOpenEvent event){}
    default void onPlayerActivatesAbility(PlayerInteractEvent event, Abilities rawAbility){}
}
