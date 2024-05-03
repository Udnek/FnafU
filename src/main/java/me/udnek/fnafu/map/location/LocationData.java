package me.udnek.fnafu.map.location;

import com.google.common.base.Preconditions;
import me.udnek.fnafu.map.Originable;
import me.udnek.itemscoreu.utils.LogUtils;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public interface LocationData extends Originable {


    LocationData add(Location location);
    LocationData add(double x, double y, double z);
    LocationData add(double x, double y, double z, float yaw, float pitch);

    List<Location> getAll();

    Location getFirst();

    Location getRandom();

    int getSize();

    LocationData centerAll();
    LocationData centerFloorAll();

    static void locationAddOrigin(Location location, Location origin){
        location.set(location.getX()+origin.getX(), location.getY()+origin.getY(), location.getZ()+origin.getZ());
        location.setWorld(origin.getWorld());
    }
}
