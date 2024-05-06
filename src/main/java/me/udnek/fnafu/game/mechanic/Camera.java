package me.udnek.fnafu.game.mechanic;

import me.udnek.fnafu.map.Originable;
import me.udnek.fnafu.map.location.LocationSingle;
import org.bukkit.Location;

public class Camera implements Originable {

    private final LocationSingle locationSingle;
    private final String id;

    public Camera(LocationSingle location, String id){
        locationSingle = location;
        this.id = id;
    }

    public String getId() {return id;}
    public LocationSingle getLocation() {return locationSingle;}
    @Override
    public void setOrigin(Location location) {
        locationSingle.setOrigin(location);
    }
}
