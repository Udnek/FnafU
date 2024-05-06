package me.udnek.fnafu.map.type;

import me.udnek.fnafu.game.mechanic.door.Door;
import me.udnek.fnafu.game.mechanic.door.DoorButtonPair;
import me.udnek.fnafu.map.FnafUMap;
import me.udnek.fnafu.map.LocationType;
import me.udnek.fnafu.map.location.LocationSingle;
import org.bukkit.Location;

public class Fnaf1PizzeriaMap extends FnafUMap {
    public Fnaf1PizzeriaMap(Location origin) {
        super(origin);
    }
    @Override
    public void build() {
        addLocation(LocationType.SPAWN_SURVIVOR, LocationSingle.from(0, 0, 3, 180, 0).centerFloor());
        addLocation(LocationType.SPAWN_ANIMATRONIC, LocationSingle.from(0, 0, -3, 0, 0).centerFloor());

        addDoor(new DoorButtonPair(-4, 0, -1, Door.Direction.Z, -3, 1, 1));
        addDoor(new DoorButtonPair(0, 0, -6, Door.Direction.X, - 2, 1, -5));
    }
}
