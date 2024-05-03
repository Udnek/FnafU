package me.udnek.fnafu.game.mechanic.door;

import me.udnek.fnafu.map.Originable;
import me.udnek.fnafu.map.location.LocationData;
import me.udnek.fnafu.map.location.LocationSingle;
import org.bukkit.Location;

public class DoorButton implements Originable {

    private LocationSingle button;

    public DoorButton(LocationSingle locationSingle){
        button = locationSingle;
    }

    @Override
    public void setOrigin(Location location) {
        button.setOrigin(location);
    }

    public Location getLocation(){
        return button.getFirst();
    }
}
