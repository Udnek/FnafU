package me.udnek.fnafu.map.instance

import com.google.common.collect.ImmutableList
import me.udnek.coreu.custom.sound.CustomSound
import me.udnek.fnafu.map.FnafUMap
import me.udnek.fnafu.map.LocationType
import me.udnek.fnafu.map.location.LocationSingle
import me.udnek.fnafu.mechanic.system.door.door.WoodenDoor
import me.udnek.fnafu.util.Sounds
import me.udnek.fnafu.util.TextUtils
import net.kyori.adventure.text.Component
import org.bukkit.Location

class Fnaf4PizzeriaMap(origin: Location) : FnafUMap(origin){
    override var systemStationsAmount: Int = 0
    override var mapImage: Component = TextUtils.getMapImage(-8, 165, "fnaf4")
    override var ambientSound: CustomSound = Sounds.AMBIENCE_FNAF1

    override fun build() {
        addLocation(LocationType.SPAWN_SURVIVOR, LocationSingle(0, 0, 0, 0f, 0f).centerFloor)
        addLocation(LocationType.PICK_STAGE_SPAWN_ANIMATRONIC, LocationSingle(0, 0, 0, 0f, 0f).centerFloor)
        addLocation(LocationType.PICK_STAGE_SPAWN_SURVIVOR, LocationSingle(0, 0, 0, 0f, 0f).centerFloor)
        addLocation(LocationType.SPAWN_ANIMATRONIC, LocationSingle(0, 0, 0, 0f, 0f).centerFloor)
        addLocation(LocationType.RESPAWN_SURVIVOR, LocationSingle(0, 0, 0, 0f, 0f).centerFloor)

        addDoor(WoodenDoor.pairOf(-5, 0, 0, WoodenDoor.Direction.WEST, 0))
        addDoor(WoodenDoor.pairOf(12, 0, 2, WoodenDoor.Direction.SOUTH, 1))

        systemStations = ImmutableList.of()
        cameras = ImmutableList.of()
    }


}
