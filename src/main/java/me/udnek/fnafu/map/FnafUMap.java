package me.udnek.fnafu.map;

import com.google.common.base.Preconditions;
import me.udnek.fnafu.mechanic.camera.Camera;
import me.udnek.fnafu.mechanic.camera.CameraSystem;
import me.udnek.fnafu.mechanic.door.Door;
import me.udnek.fnafu.mechanic.door.DoorButtonPair;
import me.udnek.fnafu.map.location.LocationData;
import me.udnek.fnafu.utils.NameId;
import me.udnek.fnafu.utils.Resettable;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

public abstract class FnafUMap implements NameId, Resettable {

    protected final Location origin;

    private final EnumMap<LocationType, LocationData> locations = new EnumMap<>(LocationType.class);
    protected final List<DoorButtonPair> doors = new ArrayList<>();
    protected final List<Camera> cameras = new ArrayList<>();
    protected final CameraSystem cameraSystem;

    public FnafUMap(Location origin){
        origin.set(origin.getBlockX(), origin.getBlockY(), origin.getBlockZ());
        origin.setPitch(0);
        origin.setYaw(0);
        this.origin = origin;
        cameraSystem = new CameraSystem();
        build();
        cameraSystem.setOrigin(origin);
    }


    public abstract void build();

    public CameraSystem getCameraSystem() {return cameraSystem;}

    ///////////////////////////////////////////////////////////////////////////
    // DOORS
    ///////////////////////////////////////////////////////////////////////////

    protected void addDoor(DoorButtonPair doorButtonPair){
        doorButtonPair.setOrigin(origin);
        doors.add(doorButtonPair);
    }
    public List<DoorButtonPair> getDoors() {
        return doors;
    }

    public Door getDoorByButtonLocation(Location location) {
        for (DoorButtonPair doorButtonPair : doors) {
            if (doorButtonPair.hasButtonAt(location)) return doorButtonPair.getDoor();
        }
        return null;
    }

    ///////////////////////////////////////////////////////////////////////////
    // LOCATIONS
    ///////////////////////////////////////////////////////////////////////////

    protected void addLocation(LocationType locationType, LocationData locationData){
        Preconditions.checkArgument(!locations.containsKey(locationType), "LocationType " + locationType.toString() + " already added!");
        locationData.setOrigin(origin);
        locations.put(locationType, locationData);
    }

    public LocationData getLocation(LocationType locationType){
        Preconditions.checkArgument(locations.containsKey(locationType), "LocationType " + locationType.toString() + " does not exists!");
        return locations.get(locationType);
    }

    ///////////////////////////////////////////////////////////////////////////
    // MISC
    ///////////////////////////////////////////////////////////////////////////

    @Override
    public void reset() {
        for (DoorButtonPair door : doors) {
            door.getDoor().open();
        }
    }
    public Location getOrigin() {return origin.clone();}
}
