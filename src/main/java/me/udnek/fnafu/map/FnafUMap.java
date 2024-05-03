package me.udnek.fnafu.map;

import me.udnek.fnafu.game.mechanic.door.Door;
import me.udnek.fnafu.game.mechanic.door.DoorButtonPair;
import me.udnek.fnafu.utils.NameId;
import me.udnek.fnafu.utils.Resettable;
import org.bukkit.Location;

public abstract class FnafUMap implements NameId, Resettable {

    protected final Location origin;
    protected final MapLocations mapLocations;

    public FnafUMap(Location origin){
        origin.set(origin.getBlockX(), origin.getBlockY(), origin.getBlockZ());
        origin.setPitch(0);
        origin.setYaw(0);
        this.origin = origin;
        mapLocations = mapLocations(origin);
    }

    public Location getOrigin() {return origin.clone();}
    protected abstract MapLocations mapLocations(Location origin);

    public MapLocations getMapLocations() {
        return mapLocations;
    }

    public Door getDoorByButtonLocation(Location location) {
        for (DoorButtonPair doorButtonPair : mapLocations.getDoors()) {
            if (doorButtonPair.hasButtonAt(location)) return doorButtonPair.getDoor();
        }
        return null;
    }

    @Override
    public void reset() {
        mapLocations.reset();
    }
}
