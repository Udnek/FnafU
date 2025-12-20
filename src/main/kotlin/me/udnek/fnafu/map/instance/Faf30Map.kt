package me.udnek.fnafu.map.instance

import me.udnek.fnafu.map.FnafUMap
import me.udnek.fnafu.map.LocationType
import me.udnek.fnafu.map.location.LocationList
import me.udnek.fnafu.map.location.LocationSingle
import me.udnek.fnafu.mechanic.system.camera.Camera
import me.udnek.fnafu.mechanic.system.door.door.MetalDoor
import me.udnek.fnafu.mechanic.system.ventilation.TrapdoorVent
import me.udnek.fnafu.misc.TextUtils
import me.udnek.fnafu.sound.LoopedSound
import me.udnek.fnafu.sound.Sounds
import net.kyori.adventure.text.Component
import org.bukkit.Location
import org.bukkit.block.BlockFace
import org.bukkit.util.BoundingBox
import java.time.Duration


class Faf30Map(origin: Location) : FnafUMap(origin) {

    override var systemStationsAmount: Int = 5
    override var ambientSound: LoopedSound = LoopedSound(Sounds.AMBIENCE_FNAF1, Duration.ofSeconds(10*60+4))
    override var mapImage: Component = TextUtils.getMapImage(-8, 165, "faf30")

    override fun relativeBounds(): BoundingBox = BoundingBox(46.0, -1.0, 16.0, -19.91, 11.81, -89.41)

    override fun build() {
        addLocation(LocationType.SPAWN_SURVIVOR, LocationSingle(-1, 0, -1, -180.0f, -0.0f).centerFloor)
        addLocation(LocationType.SPAWN_ANIMATRONIC, LocationSingle(13.47, 1.0, -54.66, 0.5f, -2.1f).centerFloor)
        addLocation(LocationType.PRESPAWN_ANIMATRONIC, LocationSingle(20.46, .0, -64.47, 0.8f, 1.2f).centerFloor)
        addLocation(LocationType.RESPAWN_SURVIVOR, LocationList()
            .add(43.75, .0, -48.3, 54f, -1.1f)
            .add(44.46, .0, -17.52, 124.4f, 0.2f)
            .add(26.23, .0, -1.08, 136.9f, 1.6f)
            .add(-.42, .0, -.33, -180f, -0.4f)
            .add(-11.58, .0, -51.5, -87.5f, 1f)
            .add(10.4, .0, -85.48, 19.1f, 0.4f)
        )

        // RIGHT OFFICE
        addDoor(MetalDoor.pairOf2x3(2, 0, -2, MetalDoor.Direction.Z, 48, 1, 1, -4))
        // LEFT OFFICE
        addDoor(MetalDoor.pairOf2x3(-4, 0, -2, MetalDoor.Direction.Z, 47, -3, 1, -4))
        // LEFT ZIG
        addDoor(MetalDoor.pairOf2x3(5, 0, -56, MetalDoor.Direction.X, 12, 5, 1, -57))
        // RIGHT ZIG
        addDoor(MetalDoor.pairOf2x3(22, 0, -56, MetalDoor.Direction.X, 14, 21, 1, -57))

        // RIGHT ZIG
        addVent(TrapdoorVent(LocationSingle(28.0, .0, -67.0), TrapdoorVent.Direction.Z, 5, 1))
        // RIGHT SCENE
        addVent(TrapdoorVent(LocationSingle(36.0, 1.0, -56.0), TrapdoorVent.Direction.Z, 15, 1))
        // LEFT ZIG
        addVent(TrapdoorVent(LocationSingle(5.0, .0, -75.0), TrapdoorVent.Direction.Z, 3, 1))
        // STORAGE
        addVent(TrapdoorVent(LocationSingle(-10.0, .0, -56.0), TrapdoorVent.Direction.Z, 10, 1))
        // FROM CORRIDOR TO SECRET
        addVent(TrapdoorVent(LocationSingle(1.0, .0, -21.0), TrapdoorVent.Direction.Z, 29, 1))
        // FROM SECRET TO CORRIDOR
        addVent(TrapdoorVent(LocationSingle(11.0, .0, -24.0), TrapdoorVent.Direction.Z, 30, 1))
        // FOXY
        addVent(TrapdoorVent(LocationSingle(15.0, 1.0, -26.0), TrapdoorVent.Direction.Z, 31, 1))
        // FREDDY
        addVent(TrapdoorVent(LocationSingle(36.0, 1.0, -13.0), TrapdoorVent.Direction.Z, 42, 1))
        // MARIONETTE
        addVent(TrapdoorVent(LocationSingle(27.0, .0, -6.0), TrapdoorVent.Direction.Z, 50, 1))
        // RIGHT TO OFFICE
        addVent(TrapdoorVent(LocationSingle(9.0, .0, -11.0), TrapdoorVent.Direction.Z, 40, 1))
        // OFFICE TO RIGHT
        addVent(TrapdoorVent(LocationSingle(2.0, .0, -7.0), TrapdoorVent.Direction.Z, 39, 1))
        // LEFT OFFICE
        addVent(TrapdoorVent(LocationSingle(-4.0, .0, 6.0), TrapdoorVent.Direction.Z, 47, 1))

        cameras = listOf(
            Camera(LocationSingle(-13.7, 2.2, -1.05, -127.5f, 23.6f).head, "left_hall", 46, true),
            Camera(LocationSingle(14.7, 2.17, -1.05, 129.6f, 22.9f).head, "right_hall", 49, true),
            Camera(LocationSingle(26.7, 1.98, -.3, 136.5f, 17.8f).head, "marionette", 50, true),
            Camera(LocationSingle(44.7, 3.2, -24.75, 79.5f, 31.4f).head, "freddy", 33, true),
            Camera(LocationSingle(44.7, 3.2, -36.3, 139.5f, 33.7f).head, "bonnie_and_chica", 24, true),
            Camera(LocationSingle(28.7, 2.2, -29.7, 48.3f, 29.8f).head, "foxy", 32, true),
            Camera(LocationSingle(2.3, 5.2, -34.3, -135.2f, 37.5f).head, "main", 21, true),
            Camera(LocationSingle(-13.7, 2.2, -47.3, -132.7f, 31.1f).head, "storage", 19, true),
            Camera(LocationSingle(.7, 2.2, -54.7, 9.6f, 19.6f).head, "upper_left_hall", 11, true),
            Camera(LocationSingle(26.7, 2.2, -70.7, 49.6f, 30f).head, "zig_hall", 11, true),
        )

        systemStations = listOf(
            // OFFICE
            LocationSingle(-2.0, 1.0, 7.0) to BlockFace.NORTH,
            // STORAGE
            LocationSingle(-6.0, 1.0, -48.0) to BlockFace.NORTH,
            // ZIG CENTER
            LocationSingle(16.0, 1.0, -65.0) to BlockFace.WEST,
            // FREDDY
            LocationSingle(44.0, 1.0, -32.0) to BlockFace.WEST,
            // SECRET
            LocationSingle(10.0, 1.0, -25.0) to BlockFace.EAST,
            // LEFT HALL CENTER
            LocationSingle(.0, 1.0, -31.0) to BlockFace.WEST,
            // MAIN
            LocationSingle(2.0, 1.0, -54.0) to BlockFace.EAST,
        )
    }
}
