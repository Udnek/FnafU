package me.udnek.fnafu.ability.playable;

import me.udnek.fnafu.ability.Ability;
import me.udnek.fnafu.game.event.Event;
import me.udnek.fnafu.game.event.PlayerClicksInCameraMenuEvent;
import me.udnek.fnafu.game.event.PlayerClosesCameraMenuEvent;
import me.udnek.fnafu.game.event.PlayerOpensCameraMenuEvent;
import me.udnek.fnafu.item.Items;
import me.udnek.fnafu.mechanic.camera.Camera;
import me.udnek.fnafu.mechanic.camera.CameraSystem;
import me.udnek.fnafu.player.type.Survivor;
import me.udnek.itemscoreu.customitem.CustomItem;
import me.udnek.itemscoreu.utils.CustomItemUtils;
import org.bukkit.inventory.ItemStack;

public class CameraTabletAbility extends Ability<Survivor> {
    public CameraTabletAbility(Survivor player) {
        super(player);
    }

    @Override
    public void activate() {
        player.getGame().getMap().getCameraSystem().openMenu(player);
    }

    @Override
    public void handleEvent(Event event) {
        if (event instanceof PlayerClosesCameraMenuEvent){
            player.getGame().getMap().getCameraSystem().exitCamera(player);
            return;
        }
        if (event instanceof PlayerOpensCameraMenuEvent) {
            CameraSystem cameraSystem = player.getGame().getMap().getCameraSystem();
            cameraSystem.spectateCamera(player, cameraSystem.getCamera("main"));
            return;
        }
        if (event instanceof PlayerClicksInCameraMenuEvent) {
            PlayerClicksInCameraMenuEvent clickEvent = (PlayerClicksInCameraMenuEvent) event;
            ItemStack itemStack = clickEvent.getItemStack();
            CustomItem customItem = CustomItemUtils.getFromItemStack(itemStack);
            if (customItem.isSameIds(Items.cameraButton)) {
                String cameraId = Items.cameraButton.getCameraId(itemStack);
                CameraSystem cameraSystem = player.getGame().getMap().getCameraSystem();
                Camera camera = cameraSystem.getCamera(cameraId);
                cameraSystem.spectateCamera(player, camera);
            }
            return;
        }
    }
}
