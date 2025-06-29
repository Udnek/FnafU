package me.udnek.fnafu.map.instance

import com.google.common.collect.ImmutableList
import me.udnek.fnafu.map.FnafUMap
import me.udnek.fnafu.map.LocationType
import me.udnek.fnafu.map.location.LocationSingle
import me.udnek.fnafu.mechanic.system.door.door.WoodenDoor
import me.udnek.fnafu.sound.LoopedSound
import me.udnek.fnafu.sound.Sounds
import me.udnek.fnafu.util.TextUtils
import net.kyori.adventure.text.Component
import org.bukkit.Location
import java.time.Duration

class Fnaf4PizzeriaMap(origin: Location) : FnafUMap(origin){
    override var systemStationsAmount: Int = 0
    override var mapImage: Component = TextUtils.getMapImage(-8, 165, "fnaf4")
    override var ambientSound: LoopedSound = LoopedSound(Sounds.AMBIENCE_FNAF1, Duration.ofSeconds(10*60+4))

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
