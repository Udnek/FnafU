package me.udnek.fnafu.map.type;

import me.udnek.fnafu.map.FnafUMap;
import me.udnek.fnafu.map.MapLocations;
import org.bukkit.Location;

public class Fnaf1PizzeriaMap extends FnafUMap {
    public Fnaf1PizzeriaMap(Location origin) {
        super(origin);
    }

    @Override
    protected MapLocations mapLocations(Location origin) {
        return new Fnaf1PizzeriaMapLocations(origin);
    }
}
