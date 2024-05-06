package me.udnek.fnafu.game.mechanic.door;

import me.udnek.fnafu.map.Originable;
import me.udnek.fnafu.map.location.LocationSingle;
import org.bukkit.Location;

public class DoorButtonPair implements Originable {

    private Door door;
    private DoorButton doorButton;

    public DoorButtonPair(Door door, DoorButton doorButton){
        this.door = door;
        this.doorButton = doorButton;
    }

    public DoorButtonPair(long doorX, long doorY, long doorZ, Door.Direction direction, long buttonX, long buttonY, long buttonZ){
        this(
                new Door(LocationSingle.from(doorX, doorY, doorZ), direction),
                new DoorButton(LocationSingle.from(buttonX, buttonY, buttonZ)));
    }


    public Door getDoor() {
        return door;
    }

    public DoorButton getDoorButton() {
        return doorButton;
    }

    public boolean hasButtonAt(Location location){
        Location buttonLocation = doorButton.getLocation();
        return buttonLocation.getBlockX() == location.getBlockX()
                && buttonLocation.getBlockY() == location.getBlockY()
                && buttonLocation.getBlockZ() == location.getBlockZ();
    }


    @Override
    public void setOrigin(Location location) {
        door.setOrigin(location);
        doorButton.setOrigin(location);
    }

}
