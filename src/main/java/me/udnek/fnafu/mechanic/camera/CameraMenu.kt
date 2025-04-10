package me.udnek.fnafu.mechanic.camera;

import me.udnek.fnafu.game.Game;
import me.udnek.fnafu.item.Items;
import me.udnek.fnafu.manager.GameManager;
import me.udnek.itemscoreu.custominventory.CustomInventory;
import me.udnek.itemscoreu.utils.LogUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CameraMenu extends CustomInventory {
    public CameraMenu(List<Camera> cameras, Component title){
        super(9*6, title);
        LogUtils.log(title);
        for (Camera camera : cameras) {
            this.inventory.setItem(camera.getTabletMenuPosition(), Items.cameraButton.getWithCamera(camera));
        }
    }

    @Override
    public void onPlayerClicksItem(InventoryClickEvent event) {
        Game game = GameManager.getManager().getGame((Player) event.getWhoClicked());
        if (game == null) return;
        game.getEventHandler().onPlayerClicksInCameraMenu(event);
    }

    @Override
    public void onPlayerClosesInventory(InventoryCloseEvent event) {
        Game game = GameManager.getManager().getGame((Player) event.getPlayer());
        if (game == null) return;
        game.getEventHandler().onPlayerClosesCameraMenu(event);
    }

    @Override
    public void onPlayerOpensInventory(InventoryOpenEvent event) {
        Game game = GameManager.getManager().getGame((Player) event.getPlayer());
        if (game == null) return;
        game.getEventHandler().onPlayerOpensCameraMenu(event);
    }

    @Override
    public int getInventorySize() {return 9*6;}

    @Override
    public Component getDisplayName() {
        return Component.empty();
    }
}
