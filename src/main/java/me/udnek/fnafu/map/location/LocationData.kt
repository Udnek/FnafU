package me.udnek.fnafu.map.location;

import me.udnek.fnafu.map.Originable;
import org.bukkit.Location;

import java.util.List;

public interface LocationData extends Originable {

    LocationData add(Location location);
    LocationData add(double x, double y, double z);
    LocationData add(double x, double y, double z, float yaw, float pitch);

    List<Location> getAll();
    Location getFirst();
    Location getRandom();
    Location get(int n);

    int getSize();

    LocationData center();
    LocationData centerFloor();

    static void locationAddOrigin(Location location, Location origin){
        location.set(location.getX()+origin.getX(), location.getY()+origin.getY(), location.getZ()+origin.getZ());
        location.setWorld(origin.getWorld());
    }
}
