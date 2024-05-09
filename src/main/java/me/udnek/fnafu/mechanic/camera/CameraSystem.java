package me.udnek.fnafu.mechanic.camera;

import com.google.common.base.Preconditions;
import me.udnek.fnafu.FnafU;
import me.udnek.fnafu.map.Originable;
import me.udnek.fnafu.player.FnafUPlayer;
import me.udnek.fnafu.utils.Resettable;
import net.kyori.adventure.text.Component;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CameraSystem implements Resettable, Originable {

    private final List<Camera> cameras = new ArrayList<>();
    private final HashMap<FnafUPlayer, Camera> playerSpectatingCameras = new HashMap<>();
    private CameraMenu cameraMenu;
    private Component mapImage = Component.text("NOT SET");


    public void setMapImage(Component mapImage) {
        this.mapImage = mapImage;
    }

    public Camera getSpectatingCamera(FnafUPlayer player){
        return playerSpectatingCameras.get(player);
    }

    public List<Camera> getCameras(){return cameras;}

    public void spectateCamera(FnafUPlayer player, Camera camera){
        Camera spectatingCamera = getSpectatingCamera(player);
        if (spectatingCamera != null){
            player.getSpectatingEntity().remove();
        }
        setPlayerSpectatingCameras(player, camera);

        ArmorStand cameraEntity = (ArmorStand) camera.getLocation().getFirst().getWorld().spawnEntity(camera.getLocation().getFirst(), EntityType.ARMOR_STAND);
        cameraEntity.setGravity(false);
        cameraEntity.setMarker(true);

        player.spectateEntity(cameraEntity);

        player.setGameMode(GameMode.SPECTATOR);

        new BukkitRunnable() {
            @Override
            public void run() {
                player.setGameMode(GameMode.SURVIVAL);
            }
        }.runTaskLater(FnafU.getInstance(), 10);


        if (camera.getRotationAngle() == 0f) return;
        new BukkitRunnable() {

            float rotateCounter = 0f;
            float rotatePerTick = 1f;
            int rotationDelay = 0;
            final float rotationAngle = camera.getRotationAngle();

            @Override
            public void run() {
                if (cameraEntity.isDead()) {cancel(); return;}

                if (rotationDelay > 0){
                    rotationDelay -= 1;
                    return;
                }

                cameraEntity.setRotation(cameraEntity.getYaw()+rotatePerTick , cameraEntity.getPitch());
                rotateCounter += rotatePerTick;

                if (Math.abs(rotateCounter) >= rotationAngle){
                    rotatePerTick *= -1f;
                    rotationDelay = 20;
                }



            }
        }.runTaskTimer(FnafU.getInstance(), 20, 1);



    }
    public void exitCamera(FnafUPlayer player){
        Camera camera = getSpectatingCamera(player);
        if (camera == null) return;
        player.getSpectatingEntity().remove();
        player.spectateSelf();
        setPlayerSpectatingCameras(player, null);
    }


    private void setPlayerSpectatingCameras(FnafUPlayer player, Camera camera){
        if (camera == null) playerSpectatingCameras.remove(player);
        else playerSpectatingCameras.put(player, camera);
    }

    public CameraSystem addCamera(Camera camera){
        Preconditions.checkArgument(getCamera(camera.getId()) == null, "Camera with id '"+ camera.getId() + " is already exists!");
        cameras.add(camera);
        return this;
    }
    public void openMenu(FnafUPlayer player){
        player.openMenu(cameraMenu);
    }

    public Camera getCamera(String id){
        for (Camera camera : cameras) {
            if (camera.getId().equals(id)){
                return camera;
            }
        }
        return null;
    }

    @Override
    public void reset() {
        for (FnafUPlayer player : playerSpectatingCameras.keySet()) {
            exitCamera(player);
        }
        playerSpectatingCameras.clear();
    }

    @Override
    public void setOrigin(Location location) {
        for (Camera camera : cameras) {
            camera.setOrigin(location);
        }
        cameraMenu = new CameraMenu(cameras, mapImage);
    }
}
