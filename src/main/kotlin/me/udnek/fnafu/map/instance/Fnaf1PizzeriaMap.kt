package me.udnek.fnafu.map.instance

import me.udnek.fnafu.map.FnafUMap
import me.udnek.fnafu.map.LocationType
import me.udnek.fnafu.map.location.LocationList
import me.udnek.fnafu.map.location.LocationSingle
import me.udnek.fnafu.mechanic.system.camera.Camera
import me.udnek.fnafu.mechanic.system.door.door.MetalDoor
import me.udnek.fnafu.sound.LoopedSound
import me.udnek.fnafu.sound.Sounds
import me.udnek.fnafu.util.TextUtils
import net.kyori.adventure.text.Component
import org.bukkit.Location
import org.bukkit.block.BlockFace
import java.time.Duration


class Fnaf1PizzeriaMap(origin: Location) : FnafUMap(origin) {

    override var systemStationsAmount: Int = 3
    override var ambientSound: LoopedSound = LoopedSound(Sounds.AMBIENCE_FNAF1, Duration.ofSeconds(10*60+4))
    override var mapImage: Component = TextUtils.getMapImage(-8, 165, "fnaf1")

    override fun build() {
        addLocation(LocationType.SPAWN_SURVIVOR, LocationSingle(0, 0, 3, 180f, 0f).centerFloor)
        addLocation(LocationType.SPAWN_ANIMATRONIC, LocationSingle(-20.01, 0.0, 33.3, -88.93f, 0f).centerFloor)
        addLocation(LocationType.PRESPAWN_ANIMATRONIC, LocationSingle(-24.39, 0.0, 35.51, -89.84f, 0f).centerFloor)
        addLocation(LocationType.PICK_STAGE_SPAWN_SURVIVOR, LocationSingle(-4, 6, -5, 0f, 0f).centerFloor)
        addLocation(LocationType.PICK_STAGE_SPAWN_ANIMATRONIC, LocationSingle(-4, 12, -5, 0f, 0f).centerFloor)
        addLocation(LocationType.RESPAWN_SURVIVOR, LocationList()
            .add(0.0, 0.0, 3.0, 180f, 0f)
            .add(18.58, .0, 37.79, 137.5f, 0f)
            .add(-18.39, .0, 38.41, -138.8f, 0f)
            .add(6.47, .0, -14.36, -39.4f, 0f)
            .add(23.23, .0, -.84, 52.3f, 0f)
            .add(-16.89, .0, .43, -47.8f, 0f)
            .add(-31.33, .0, 19.7, 179.2f, 0f)
        )

        addDoor(MetalDoor.pairOf(4, 0, -1, MetalDoor.Direction.Z, 39, 3, 1, 1))
        addDoor(MetalDoor.pairOf(9, 0, -1, MetalDoor.Direction.Z, 38, 10, 1, 1))
        addDoor(MetalDoor.pairOf(-5, 0, -1, MetalDoor.Direction.Z, 40, -4, 1, 1))
        addDoor(MetalDoor.pairOf(-10, 0, -1, MetalDoor.Direction.Z, 41, -11, 1, 1))
        addDoor(MetalDoor.pairOf(-17, 0, 5, MetalDoor.Direction.X, 33, -19, 1, 4))
        addDoor(MetalDoor.pairOf(-25, 0, 27, MetalDoor.Direction.Z, 16, -26, 1, 29))
        addDoor(MetalDoor.pairOf(-25, 0, 15, MetalDoor.Direction.Z, 25, -26, 1, 13))
        addDoor(MetalDoor.pairOf(20, 0, 28, MetalDoor.Direction.Z, 10, 21, 1, 26))
        addDoor(MetalDoor.pairOf(16, 0, 5, MetalDoor.Direction.X, 29, 14, 1, 4))
        addDoor(MetalDoor.pairOf(6, 0, -10, MetalDoor.Direction.X, 48, 4, 1, -11))

        cameras = listOf(
            Camera(LocationSingle(19.7, 2.2, 35.3, 65.37F, 32.41F).head, "main_entrance", 1, 0F, true),
            Camera(LocationSingle(30.7, 2.2, 25.42, 70.12F, 32.89F).head, "backstage", 9, 0F, true),
            Camera(LocationSingle(20.49, 5.2, 17.52, 79.58F, 26.58F).head, "dining_area", 19, 0F, false),
            Camera(LocationSingle(-21.3, 2.2, 12.3, 11.28F, 31.57F).head, "restrooms", 24, 0F, true),
            Camera(LocationSingle(-34.7, 0.2, 4.3, -4.6F, 9.76F).head, "east_vent_corner", 35, 0F, false),
            Camera(LocationSingle(23.7, 2.2, -5.7, 46.85F, 28.92F).head, "arcade_room", 37, 0F, false),
            Camera(LocationSingle(3.7, 2.2, -3.7, 40.85F, 33.98F).head, "office", 40, 0F, false),
            Camera(LocationSingle(-19.7, 2.2, -2.7, -40.58F, 26.75F).head, "kitchen", 42, 0F, false),
            Camera(LocationSingle(3.3, 2.2, -14.7, -46.12F, 33.13F).head, "generator", 48, 0F, false),
            Camera(LocationSingle(-8.7, 2.12, -8.7, -45.16F, 30.12F).head, "east_hall_corner", 50, 0F, true)
        )

        systemStations = listOf(
            Pair(LocationSingle(3, 1, -15), BlockFace.EAST),
            Pair(LocationSingle(-9, 2, 33), BlockFace.NORTH),
            Pair(LocationSingle(22, 1, 23), BlockFace.SOUTH),
            Pair(LocationSingle(-35, 1, 30), BlockFace.NORTH),
            Pair(LocationSingle(10, 1, 2), BlockFace.EAST),
            Pair(LocationSingle(3, 1, -4), BlockFace.SOUTH),
            Pair(LocationSingle(-19, 1, -3), BlockFace.SOUTH),
            Pair(LocationSingle(-1, 1, 35), BlockFace.SOUTH)
        )
    }
}
