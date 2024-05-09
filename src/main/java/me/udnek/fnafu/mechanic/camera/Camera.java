package me.udnek.fnafu.mechanic.camera;

import me.udnek.fnafu.map.Originable;
import me.udnek.fnafu.map.location.LocationSingle;
import org.bukkit.Location;

public class Camera implements Originable {

    private final LocationSingle locationSingle;
    private final String id;
    private final int tabletMenuPosition;
    private final float rotationAngle;


    public Camera(LocationSingle location, String id, int tabletMenuPosition, float rotationAngle){
        locationSingle = location;
        this.id = id;
        this.tabletMenuPosition = tabletMenuPosition;
        this.rotationAngle = rotationAngle;
    }
    public Camera(LocationSingle location, String id, int tabletMenuPosition){
        this(location, id, tabletMenuPosition, 0f);
    }

    public float getRotationAngle() {
        return rotationAngle;
    }

    public String getId() {return id;}
    public int getTabletMenuPosition() {return tabletMenuPosition;}
    public LocationSingle getLocation() {return locationSingle;}
    @Override
    public void setOrigin(Location location) {
        locationSingle.setOrigin(location);
    }
}
