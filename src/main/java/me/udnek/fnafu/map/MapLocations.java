package me.udnek.fnafu.map;

import com.google.common.base.Preconditions;
import me.udnek.fnafu.game.mechanic.door.DoorButtonPair;
import me.udnek.fnafu.map.location.LocationData;
import me.udnek.fnafu.utils.NameId;
import me.udnek.fnafu.utils.Resettable;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

public abstract class MapLocations implements NameId, Resettable {

     private final EnumMap<LocationType, LocationData> locations = new EnumMap<>(LocationType.class);
     protected final List<DoorButtonPair> doors = new ArrayList<>();
     protected final Location origin;

     public MapLocations(Location origin){
          this.origin = origin;
          build();
     }

     @Override
     public void reset() {
          for (DoorButtonPair door : doors) {
               door.getDoor().open();
          }
     }

     public abstract void build();

     ///////////////////////////////////////////////////////////////////////////
     // DOORS
     ///////////////////////////////////////////////////////////////////////////

     protected void addDoor(DoorButtonPair doorButtonPair){
          doorButtonPair.setOrigin(origin);
          doors.add(doorButtonPair);
     }

     public List<DoorButtonPair> getDoors() {
          return doors;
     }

     ///////////////////////////////////////////////////////////////////////////
     // LOCATIONS
     ///////////////////////////////////////////////////////////////////////////

     protected void addLocation(LocationType locationType, LocationData locationData){
          Preconditions.checkArgument(!locations.containsKey(locationType), "LocationType " + locationType.toString() + " already added!");
          locationData.setOrigin(origin);
          locations.put(locationType, locationData);
     }

     public LocationData getLocation(LocationType locationType){
          Preconditions.checkArgument(locations.containsKey(locationType), "LocationType " + locationType.toString() + " does not exists!");
          return locations.get(locationType);
     }
}
