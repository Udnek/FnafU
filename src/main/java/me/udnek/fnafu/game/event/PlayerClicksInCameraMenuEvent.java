package me.udnek.fnafu.game.event;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class PlayerClicksInCameraMenuEvent implements Event{

    private final ItemStack itemStack;
    private final Inventory inventory;
    public PlayerClicksInCameraMenuEvent(ItemStack itemStack, Inventory inventory){
        this.inventory = inventory;
        this.itemStack = itemStack;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public Inventory getInventory() {
        return inventory;
    }
}
