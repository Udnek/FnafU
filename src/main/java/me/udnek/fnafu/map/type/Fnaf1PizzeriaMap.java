package me.udnek.fnafu.map.type;

import me.udnek.fnafu.map.FnafUMap;
import me.udnek.fnafu.map.LocationType;
import me.udnek.fnafu.map.location.LocationSingle;
import me.udnek.fnafu.mechanic.camera.Camera;
import me.udnek.fnafu.mechanic.door.Door;
import me.udnek.fnafu.mechanic.door.DoorButtonPair;
import me.udnek.fnafu.utils.TextUtils;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;

public class Fnaf1PizzeriaMap extends FnafUMap {
    public Fnaf1PizzeriaMap(Location origin) {
        super(origin);
    }
    @Override
    public void build() {
        addLocation(LocationType.SPAWN_SURVIVOR, LocationSingle.from(0, 0, 3, 180, 0).centerFloor());
        addLocation(LocationType.SPAWN_ANIMATRONIC, LocationSingle.from(0, 0, -3, 0, 0).centerFloor());

        addDoor(new DoorButtonPair(-4, 0, -1, Door.Direction.Z, -3, 1, 1));
        addDoor(new DoorButtonPair(0, 0, -6, Door.Direction.X, - 2, 1, -5));

        Camera mainCamera = new Camera(LocationSingle.from(-5, 5, 0, -90, 45), "main", 5, 30f);
        Camera officeCamera = new Camera(LocationSingle.from(0, 0, 0), "office", 9*3 + 3, 180f);
        Camera kitchenCamera = new Camera(LocationSingle.from(0, 0, 0), "kitchen", 9*4 + 6);

        getCameraSystem()
                .addCamera(mainCamera)
                .addCamera(officeCamera)
                .addCamera(kitchenCamera);

        getCameraSystem().setMapImage(TextUtils.getMapImage(151, "fnaf1"));
    }
}
