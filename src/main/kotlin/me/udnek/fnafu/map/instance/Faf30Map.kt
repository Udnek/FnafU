package me.udnek.fnafu.map.instance

import me.udnek.fnafu.map.FnafUMap
import me.udnek.fnafu.map.LocationType
import me.udnek.fnafu.map.location.LocationList
import me.udnek.fnafu.map.location.LocationSingle
import me.udnek.fnafu.mechanic.system.door.door.MetalDoor
import me.udnek.fnafu.mechanic.system.ventilation.TrapdoorVent
import me.udnek.fnafu.misc.TextUtils
import me.udnek.fnafu.sound.LoopedSound
import me.udnek.fnafu.sound.Sounds
import net.kyori.adventure.text.Component
import org.bukkit.Location
import org.bukkit.util.BoundingBox
import java.time.Duration


class Faf30Map(origin: Location) : FnafUMap(origin) {

    override var systemStationsAmount: Int = 3
    override var ambientSound: LoopedSound = LoopedSound(Sounds.AMBIENCE_FNAF1, Duration.ofSeconds(10*60+4))
    override var mapImage: Component = TextUtils.getMapImage(-8, 165, "faf30")

    override fun relativeBounds(): BoundingBox = BoundingBox(32.0, -1.0, -24.0, -38.0, 15.0, 48.0)

    override fun build() {
        addLocation(LocationType.SPAWN_SURVIVOR, LocationSingle(0, 0, 0, 0f, 0f).centerFloor)
        addLocation(LocationType.SPAWN_ANIMATRONIC, LocationSingle(-19.5, .0, 33.5, -90f, 0f).centerFloor)
        addLocation(LocationType.PRESPAWN_ANIMATRONIC, LocationSingle(-24.38, .0, 35.49, -90f, 0f).centerFloor)
        addLocation(LocationType.PICK_STAGE_SPAWN_SURVIVOR, LocationSingle(-4, 6, -5, 0f, 0f).centerFloor)
        addLocation(LocationType.PICK_STAGE_SPAWN_ANIMATRONIC, LocationSingle(-4, 12, -5, 0f, 0f).centerFloor)
        addLocation(LocationType.RESPAWN_SURVIVOR, LocationList()
            .add(0.0, 0.0, 0.0, 0f, 0f)
            .add(18.58, .0, 37.79, 137.5f, 0f)
            .add(-18.39, .0, 38.41, -138.8f, 0f)
            .add(6.47, .0, -14.36, -39.4f, 0f)
            .add(23.23, .0, -.84, 52.3f, 0f)
            .add(-16.89, .0, .43, -47.8f, 0f)
            .add(-31.33, .0, 19.7, 179.2f, 0f)
        )

        addDoor(MetalDoor.pairOf3x3(4, 0, -1, MetalDoor.Direction.Z, 39, 3, 1, 1))

        // KITCHEN
        addVent(TrapdoorVent(LocationSingle(-22.0, .0, 4.0), TrapdoorVent.Direction.Z, 43, 1))

        cameras = listOf(

        )

        systemStations = listOf(

        )
    }
}
