package me.udnek.fnafu.map.instance

import me.udnek.fnafu.map.FnafUMap
import me.udnek.fnafu.map.LocationType
import me.udnek.fnafu.map.location.LocationList
import me.udnek.fnafu.map.location.LocationSingle
import me.udnek.fnafu.mechanic.camera.Camera
import me.udnek.fnafu.mechanic.door.ButtonDoorPair
import me.udnek.fnafu.mechanic.door.Door
import me.udnek.fnafu.util.Sounds
import me.udnek.fnafu.util.TextUtils
import org.bukkit.Location

class Fnaf1PizzeriaMap(origin: Location) : FnafUMap(origin) {
    override fun build() {
        addLocation(LocationType.SPAWN_SURVIVOR, LocationSingle(0.0, 0.0, 3.0, 180f, 0f).centerFloor)
        addLocation(LocationType.SPAWN_ANIMATRONIC, LocationSingle(0.0, 0.0, -3.0, 0f, 0f).centerFloor)
        addLocation(LocationType.PICK_STAGE_SPAWN_SURVIVOR, LocationSingle(-4.0, 6.0, -5.0, 0f, 0f).centerFloor)
        addLocation(LocationType.PICK_STAGE_SPAWN_ANIMATRONIC, LocationSingle(-4.0, 12.0, -5.0, 0f, 0f).centerFloor)
        addLocation(LocationType.RESPAWN_SURVIVOR, LocationList().add(0.0, 0.0, 3.0, 180f, 0f).add(0.0, 5.0, 12.0, 180f, 0f))

        addDoor(ButtonDoorPair(4, 0, -1, Door.Direction.Z, 3, 1, 1))
        addDoor(ButtonDoorPair(9, 0, -1, Door.Direction.Z, 10, 1, 1))
        addDoor(ButtonDoorPair(-5, 0, -1, Door.Direction.Z, -4, 1, 1))
        addDoor(ButtonDoorPair(-10, 0, -1, Door.Direction.Z, -11, 1, 1))
        addDoor(ButtonDoorPair(-17, 0, 5, Door.Direction.X, -19, 1, 4))
        addDoor(ButtonDoorPair(-25, 0, 27, Door.Direction.Z, -26, 1, 29))
        addDoor(ButtonDoorPair(-25, 0, 15, Door.Direction.Z, -26, 1, 13))
        addDoor(ButtonDoorPair(20, 0, 28, Door.Direction.Z, 21, 1, 26))
        addDoor(ButtonDoorPair(16, 0, 5, Door.Direction.X, 14, 1, 4))
        addDoor(ButtonDoorPair(6, 0, -10, Door.Direction.X, 4, 1, -11))

        cameras = listOf(
            Camera(LocationSingle(19.7, 2.2, 35.3, 65.37F, 32.41F).head, "main_entrance", 1, 0F, true),
            Camera(LocationSingle(30.7, 2.2, 25.42, 70.12F, 32.89F).head, "backstage", 9, 0F, true),
            Camera(LocationSingle(19.7, 2.2, 35.3, 65.37F, 32.41F).head, "dining_area", 19, 0F, false),
            Camera(LocationSingle(-21.3, 2.2, 12.3, 11.28F, 31.57F).head, "restrooms", 24, 0F, true),
            Camera(LocationSingle(-34.7, 0.2, 4.3, -4.6F, 9.76F).head, "east_vent_corner", 35, 0F, false),
            Camera(LocationSingle(23.7, 2.2, -5.7, 46.85F, 28.92F).head, "arcade_room", 37, 0F, false),
            Camera(LocationSingle(3.7, 2.2, -3.7, 40.85F, 33.98F).head, "office", 40, 0F, false),
            Camera(LocationSingle(-19.7, 2.2, -2.7, -40.58F, 26.75F).head, "kitchen", 42, 0F, false),
            Camera(LocationSingle(3.3, 2.2, -14.7, -46.12F, 33.13F).head, "generator", 48, 0F, false),
            Camera(LocationSingle(-8.7, 2.12, -8.7, -45.16F, 30.12F).head, "east_hall_corner", 50, 0F, true)
        )

        cameraImage = TextUtils.getMapImage(-8, 165, "fnaf1")

        ambientSound = Sounds.AMBIENCE_FNAF1
    }
}
