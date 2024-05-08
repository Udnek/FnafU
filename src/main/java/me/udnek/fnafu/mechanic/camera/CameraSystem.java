package me.udnek.fnafu.mechanic.camera;

import com.google.common.base.Preconditions;
import me.udnek.fnafu.map.Originable;
import me.udnek.fnafu.player.FnafUPlayer;
import me.udnek.fnafu.utils.Resettable;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CameraSystem implements Resettable, Originable {

    private final List<Camera> cameras = new ArrayList<>();
    private final HashMap<FnafUPlayer, Camera> playerSpectatingCameras = new HashMap<>();
    private CameraMenu cameraMenu;

    public Camera getSpectatingCamera(FnafUPlayer player){
        return playerSpectatingCameras.get(player);
    }

/*    public CameraMenu getCameraMenu(){
        return cameraMenu;
    }*/

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


    public void createMenu(){
        cameraMenu = new CameraMenu(cameras);
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
        createMenu();
        for (Camera camera : cameras) {
            camera.setOrigin(location);
        }
    }
}
