package me.udnek.fnafu.map.type;

import me.udnek.fnafu.game.mechanic.door.Door;
import me.udnek.fnafu.game.mechanic.door.DoorButtonPair;
import me.udnek.fnafu.map.LocationType;
import me.udnek.fnafu.map.MapLocations;
import me.udnek.fnafu.map.location.LocationSingle;
import org.bukkit.Location;

public class Fnaf1PizzeriaMapLocations extends MapLocations {
    public Fnaf1PizzeriaMapLocations(Location origin) {
        super(origin);
    }

    @Override
    public String getNameId() {
        return "FNAF1";
    }

    @Override
    public void build() {
        addLocation(LocationType.SPAWN_SURVIVOR, LocationSingle.from(0, 0, 3, 180, 0).centerFloorAll());
        addLocation(LocationType.SPAWN_ANIMATRONIC, LocationSingle.from(0, 0, -3, 0, 0).centerFloorAll());

        addDoor(DoorButtonPair.from(-4, 0, -1, Door.Direction.Z, -3, 1, 1));
        addDoor(DoorButtonPair.from(0, 0, -6, Door.Direction.X, - 2, 1, -5));
    }
}
