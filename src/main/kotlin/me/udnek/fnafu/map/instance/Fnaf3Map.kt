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


class Fnaf3Map(origin: Location) : FnafUMap(origin) {

    override var systemStationsAmount: Int = 3
    override var ambientSound: LoopedSound = LoopedSound(Sounds.AMBIENCE_FNAF3, Duration.ofSeconds(1*60+25))
    override var mapImage: Component = TextUtils.getMapImage(-8, 165, "fnaf3")

    override fun relativeBounds(): BoundingBox = BoundingBox(-30.0, -1.0, 7.0, 27.0, 7.0, -54.0)

    override fun build() {
        addLocation(LocationType.PICK_STAGE_SPAWN_SURVIVOR, LocationSingle(-1.47, .0, -.61, 180f, 0f).centerFloor)
        addLocation(LocationType.PICK_STAGE_SPAWN_ANIMATRONIC, LocationSingle(19.43, .0, -43.42, 0f, 0f).centerFloor)
        addLocation(LocationType.SPAWN_SURVIVOR, LocationSingle(-1.47, .0, -.61, 180f, 0f).centerFloor)
        addLocation(LocationType.PRESPAWN_ANIMATRONIC, LocationSingle(19.43, .0, -43.42, 0f, 0f).centerFloor)
        addLocation(LocationType.SPAWN_ANIMATRONIC, LocationSingle(19.48, .0, -38.51, 0f, 0f).centerFloor)


        addLocation(LocationType.RESPAWN_SURVIVOR, LocationList()
            .add(-1.47, .0, -.61, 180f, 0f)
            .add(16.42, .0, -7.46, -180f, 0f)
            .add(19.35, .0, -36.48, 90f, 0f)
            .add(-15.47, .0, -17.67, -180f, 0f)
            .add(-2.68, .0, -0.45, -180f, 0f)
            .add(-25.54, .0, -42.58, 0f, 0f)
            .centerFloor
        )

        addDoor(MetalDoor.pairOf3x3(-9, 0, -1, MetalDoor.Direction.Z, 5*9+3, -7, 1, 1)) // офис
        addDoor(MetalDoor.pairOf3x3(5, 0, -9, MetalDoor.Direction.Z, 4*9+5, 3, 1, -11)) // дверь за окном офиса
        addDoor(MetalDoor.pairOf3x3(12, 0, -9, MetalDoor.Direction.Z, 4*9+6, 14, 1, -11)) // кномнота с головой фокси возле винтов
        addDoor(MetalDoor.pairOf3x3(12, 0, -25, MetalDoor.Direction.Z, 2*9+6, 14, 1, -23)) // комната с головой фокси возле головы фокси
        addDoor(MetalDoor.pairOf3x3(3, 0, -33, MetalDoor.Direction.X, 1*9+5, 5, 1, -35)) // склад (старотовая точка спринтрапа в фнаф3)
        addDoor(MetalDoor.pairOf2x3(-11, 0, -22, MetalDoor.Direction.Z, 2*9+3, -14, 1, -21)) //  Вход в аркаду номер 1
        addDoor(MetalDoor.pairOf2x3(-11, 0, -29, MetalDoor.Direction.Z, 1*9+3, -14, 1, -31)) // воход в аркаду номер два ( из комнаты с головой чики)

        // SPAWN ANIM
        addVent(TrapdoorVent(LocationSingle(17.0, .0, -33.0), TrapdoorVent.Direction.X, 15, 2))
        // SPAWN SURV
        addVent(TrapdoorVent(LocationSingle(6.0, .0, -1.0), TrapdoorVent.Direction.Z, 50, 2))
        // LEFT SOS
        addVent(TrapdoorVent(LocationSingle(8.0, .0, -5.0), TrapdoorVent.Direction.X, 41, 2))
        // PRAV SOS
        addVent(TrapdoorVent(LocationSingle(15.0, .0, -5.0), TrapdoorVent.Direction.X, 42, 2))
        // PRAV SOS VERH
        addVent(TrapdoorVent(LocationSingle(20.0, .0, -19.0), TrapdoorVent.Direction.Z, 34, 2))
        // ENTRENCE
        addVent(TrapdoorVent(LocationSingle(-2.0, .0, -39.0), TrapdoorVent.Direction.Z, 4, 2))
        //
        addVent(TrapdoorVent(LocationSingle(-9.0, .0, -33.0), TrapdoorVent.Direction.X, 12, 2))
        addVent(TrapdoorVent(LocationSingle(-20.0, .0, -34.0), TrapdoorVent.Direction.Z, 11, 2))
        addVent(TrapdoorVent(LocationSingle(-15.0, .0, -10.0), TrapdoorVent.Direction.Z, 38, 2))
        addVent(TrapdoorVent(LocationSingle(-6.0, .0, -19.0), TrapdoorVent.Direction.X, 21, 2))
        addVent(TrapdoorVent(LocationSingle(-6.0, .0, -13.0), TrapdoorVent.Direction.X, 30, 2))

        cameras = listOf(
            Camera(LocationSingle(4.7, 2.2, 2.7, 123.2f, 30f).head, "office", 5*9+5, 0F, false),
            Camera(LocationSingle(-10.3, 2.2, 2.7, 143.9f, 39.9f).head, "exit", 5*9+2, 0F, true),
            Camera(LocationSingle(10.7, 2.2, -6.3, 170.9f, 13.2f).head, "west_hall", 4*9+5, 0F, true),
            Camera(LocationSingle(18.7, 2.2, -26.7, 19.2f, 28.4f).head, "east_hall", 2*9+6, 0F, true),
            Camera(LocationSingle(3.97, 1.2, -22.69, 89.3f, 7.2f).head, "south_hall", 2*9+3, 0F, false),
            Camera(LocationSingle(-8.7, 2.2, -27.3, -112.1f, 29.4f).head, "north_hall", 1*9+3, 0F, true),
            Camera(LocationSingle(-13.3, 2.2, -35.7, 32.5f, 38.1f).head, "arcade", 1*9+2, 0F, true),
            Camera(LocationSingle(21.7, 2.2, -38.7, 68.4f, 22.9f).head, "entrance", 0*9+6, 0F, false),
        )

        systemStations = listOf(
            Pair(LocationSingle(16.0, 1.0, -39.0), BlockFace.SOUTH),
            Pair(LocationSingle(18.0, 1.0, -21.0), BlockFace.WEST),
            Pair(LocationSingle(-7.0, 1.0, 2.0), BlockFace.EAST),
            Pair(LocationSingle(-14.0, 1.0, -36.0), BlockFace.SOUTH),
            Pair(LocationSingle(14.0, 1.0, -12.0), BlockFace.EAST),
        )
    }
}
