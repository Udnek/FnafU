package me.udnek.fnafu.map.instance

import me.udnek.fnafu.map.FnafUMap
import me.udnek.fnafu.map.LocationType
import me.udnek.fnafu.map.location.LocationList
import me.udnek.fnafu.map.location.LocationSingle
import me.udnek.fnafu.mechanic.camera.Camera
import me.udnek.fnafu.mechanic.door.ButtonDoorPair
import me.udnek.fnafu.mechanic.door.Door
import me.udnek.fnafu.util.TextUtils
import org.bukkit.Location

class Fnaf1PizzeriaMap(origin: Location) : FnafUMap(origin) {
    override fun build() {
        addLocation(LocationType.SPAWN_SURVIVOR, LocationSingle(0.0, 0.0, 3.0, 180f, 0f).centerFloor)
        addLocation(LocationType.SPAWN_ANIMATRONIC, LocationSingle(0.0, 0.0, -3.0, 0f, 0f).centerFloor)
        addLocation(LocationType.RESPAWN_SURVIVOR, LocationList().add(0.0, 0.0, 3.0, 180f, 0f).add(0.0, 5.0, 12.0, 180f, 0f))

        addDoor(ButtonDoorPair(-4, 0, -1, Door.Direction.Z, -3, 1, 1))
        addDoor(ButtonDoorPair(0, 0, -6, Door.Direction.X, -2, 1, -5))

        val mainCamera = Camera(LocationSingle(-5.0, 5.0, 0.0, -90f, 45f), "main", 5, 30f)
        val officeCamera = Camera(LocationSingle(0.0, 0.0, 0.0), "office", 9 * 3 + 3, 180f)
        val kitchenCamera = Camera(LocationSingle(0.0, 0.0, 0.0), "kitchen", 9 * 4 + 6, 0f)

        cameras = listOf(mainCamera, officeCamera, kitchenCamera)

        cameraImage = TextUtils.getMapImage(-8, 165, "fnaf1")
    }
}
