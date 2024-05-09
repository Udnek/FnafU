package me.udnek.fnafu.map.location;

import com.google.common.base.Preconditions;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LocationSingle implements LocationData {

    private Location location;
    private boolean frozen = false;

    public LocationSingle add(Location location){
        Preconditions.checkArgument(!frozen, "Location is frozen");
        this.location = location;
        return this;
    }
    public LocationSingle add(double x, double y, double z){
        add(new Location(null, x, y, z, 0, 0));
        return this;
    }
    public LocationSingle add(double x, double y, double z, float yaw, float pitch){
        add(new Location(null, x, y, z, yaw, pitch));
        return this;
    }

    public List<Location> getAll(){
        return new ArrayList<>(Collections.singleton(location));
    }

    public void setOrigin(Location origin){
        Preconditions.checkArgument(!frozen, "Location is frozen");
        LocationData.locationAddOrigin(location, origin);
        frozen = true;
    }


    public Location getFirst(){
        return location.clone();
    }

    public Location getRandom(){
        return getFirst();
    }

    @Override
    public Location get(int n) {return getFirst();}

    public int getSize() { return (location == null ? 0 : 1); }


    public LocationSingle center(){
        Preconditions.checkArgument(!frozen, "Location is frozen");
        Location center = location.toCenterLocation();
        location.set(center.getX(), center.getY(), center.getZ());
        return this;
    }
    public LocationSingle centerFloor(){
        Preconditions.checkArgument(!frozen, "Location is frozen");
        Location center = location.toCenterLocation();
        location.set(center.getX(), center.getBlockY(), center.getZ());
        return this;
    }


    public static LocationSingle from(double x, double y, double z){
        return new LocationSingle().add(x, y, z);
    }

    public static LocationSingle from(double x, double y, double z, float yaw, float pitch){
        return new LocationSingle().add(x, y, z, yaw, pitch);
    }
    public static LocationSingle from(Location location){
        return new LocationSingle().add(location);
    }

}
