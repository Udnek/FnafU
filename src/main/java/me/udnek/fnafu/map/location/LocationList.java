package me.udnek.fnafu.map.location;

import com.google.common.base.Preconditions;
import me.udnek.itemscoreu.utils.LogUtils;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LocationList implements LocationData{

    private final List<Location> locations = new ArrayList<>();
    private boolean frozen = false;

    public LocationList add(Location location){
        Preconditions.checkArgument(!frozen, "LocationList is frozen");
        locations.add(location);
        return this;
    }
    public LocationList add(double x, double y, double z){
        add(new Location(null, x, y, z, 0, 0));
        return this;
    }
    public LocationList add(double x, double y, double z, float yaw, float pitch){
        add(new Location(null, x, y, z, yaw, pitch));
        return this;
    }

    public List<Location> getAll(){
        return new ArrayList<>(locations);
    }

    public void setOrigin(Location origin){
        Preconditions.checkArgument(!frozen, "LocationList is frozen");
        for (Location location : locations) {
            LocationData.locationAddOrigin(location, origin);
        }
        frozen = true;
    }


    public Location getFirst(){
        return locations.get(0);
    }

    public Location getRandom(){
        if (getSize() == 1) return getFirst();
        return locations.get(new Random().nextInt(getSize()));
    }

    public int getSize() { return locations.size(); }


    public LocationList center(){
        Preconditions.checkArgument(!frozen, "LocationList is frozen");
        for (Location location : locations) {
            Location center = location.toCenterLocation();
            location.set(center.getX(), center.getY(), center.getZ());
        }
        return this;
    }
    public LocationList centerFloor(){
        Preconditions.checkArgument(!frozen, "LocationList is frozen");
        for (Location location : locations) {
            Location center = location.toCenterLocation();
            location.set(center.getX(), center.getBlockY(), center.getZ());
        }
        return this;
    }

    public static LocationList from(double x, double y, double z){
        return new LocationList().add(x, y, z);
    }

    public static LocationList from(double x, double y, double z, float yaw, float pitch){
        return new LocationList().add(x, y, z, yaw, pitch);
    }
    public static LocationList from(Location location){
        return new LocationList().add(location);
    }

}
