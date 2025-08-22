package me.udnek.fnafu.item

import me.udnek.coreu.custom.item.CustomItem
import me.udnek.coreu.custom.registry.CustomRegistries
import me.udnek.fnafu.FnafU
import me.udnek.fnafu.block.Blocks
import me.udnek.fnafu.component.survivor.PetrolCanisterAbility
import me.udnek.fnafu.item.animatronic.AnimatronicMask
import me.udnek.fnafu.item.animatronic.freddy.FreddySetTrapItem
import me.udnek.fnafu.item.animatronic.freddy.FreddyShadowItem
import me.udnek.fnafu.item.animatronic.freddy.FreddyTeleportToTrapItem
import me.udnek.fnafu.item.animatronic.freddy.FreddyTrap
import me.udnek.fnafu.item.animatronic.springtrap.SpringtrapBreakCamerasItem
import me.udnek.fnafu.item.animatronic.springtrap.SpringtrapPlushtrapItem
import me.udnek.fnafu.item.decor.FnafUBlockDecorItem
import me.udnek.fnafu.item.decor.NightstandItem
import me.udnek.fnafu.item.decor.Poster
import me.udnek.fnafu.item.decor.UniversalSlab
import me.udnek.fnafu.item.survivor.PetrolCanister
import me.udnek.fnafu.item.survivor.SystemStation
import me.udnek.fnafu.item.survivor.SystemTablet
import me.udnek.fnafu.item.survivor.camera.CameraButton
import me.udnek.fnafu.item.survivor.camera.CutCameraTablet
import me.udnek.fnafu.item.survivor.camera.FullCameraTablet
import me.udnek.fnafu.item.survivor.doorman.DoormanTablet
import me.udnek.fnafu.item.survivor.doorman.DoormanTabletButton
import me.udnek.fnafu.item.survivor.ventilation.VentilationTablet
import me.udnek.fnafu.item.survivor.ventilation.VentilationTabletButton
import org.bukkit.Material


object Items {
    val CAMERA_BUTTON: CustomItem = register(CameraButton())
    val CAMERA_OVERLAY: CustomItem = register(EmptyItem("camera_overlay"))

    val FULL_CAMERA_TABLET: CustomItem = register(FullCameraTablet())
    val CUT_CAMERA_TABLET: CustomItem = register(CutCameraTablet())

    val SYSTEM_TABLET: CustomItem = register(SystemTablet())

    val DOOR_TABLET: CustomItem = register(DoormanTablet())
    val DOOR_BUTTON: CustomItem = register(DoormanTabletButton())

    val VENTILATION_TABLET: CustomItem = register(VentilationTablet())
    val VENTILATION_BUTTON: CustomItem = register(VentilationTabletButton())

    val UP_BUTTON: CustomItem = register(EmptyItem("up_button").hiddenModel())
    val DOWN_BUTTON: CustomItem = register(EmptyItem("down_button").hiddenModel())
    val ENTER_BUTTON: CustomItem = register(EmptyItem("enter_button").hiddenModel())
    val EXIT_BUTTON: CustomItem = register(EmptyItem("exit_button").hiddenModel())
    val CURSOR_ICON: CustomItem = register(EmptyItem("cursor"))
    val ERROR_ICON: CustomItem = register(EmptyItem("error"))
    val REBOOT_ICON: CustomItem = register(EmptyItem("reboot"))
    val CANCEL_BUTTON = register(CancelButton())
    val READY_BUTTON = register(ReadyButton())

    val SPRINGTRAP_PLUSHTRAP_ABILITY: CustomItem = register(SpringtrapPlushtrapItem())
    val SPRINGTRAP_BREAK_CAMERAS: CustomItem = register(SpringtrapBreakCamerasItem())
    val SPRINGTRAP_MASK: CustomItem = register(AnimatronicMask("springtrap", 0.07))

    val FREDDY_SHADOW: CustomItem = register(FreddyShadowItem())
    val FREDDY_MASK: CustomItem = register(AnimatronicMask("freddy", 0.03))
    val FREDDY_TRAP: CustomItem = register(FreddyTrap())
    val FREDDY_SET_TRAP: CustomItem = register(FreddySetTrapItem())
    val FREDDY_TELEPORT_TO_TRAP: CustomItem = register(FreddyTeleportToTrapItem())

    val GENERATOR: CustomItem = register(FnafUBlockItem("generator", Blocks.GENERATOR))
    val PETROL_STATION: CustomItem = register(FnafUBlockItem("petrol_station", Blocks.PETROL_STATION))
    val SYSTEM_STATION = register(SystemStation())

    val SMALL_PETROL_CANISTER: CustomItem = register(PetrolCanister(PetrolCanisterAbility.SMALL, "small_petrol_canister"))
    val LARGE_PETROL_CANISTER: CustomItem = register(PetrolCanister(PetrolCanisterAbility.LARGE, "large_petrol_canister"))

    val MAP_ICON_FNAF1 = register(MapIcon("fnaf1"))
    val MAP_ICON_FNAF4 = register(MapIcon("fnaf4"))
    val MAP_ICON_FNAF3 = register(MapIcon("fnaf3"))

    // DECOR

    val ARCADE_MACHINE : CustomItem = register(FnafUBlockDecorItem("arcade_machine", Blocks.ARCADE_MACHINE))
    val VENDING_MACHINE : CustomItem = register(FnafUBlockDecorItem("vending_machine", Blocks.VENDING_MACHINE))
    val NIGHTSTAND : CustomItem = register(NightstandItem())
    val GREY_STARS_ON_CEIL: CustomItem = register(FnafUBlockDecorItem("grey_stars_on_ceil", Blocks.GREY_STARS_ON_CEIL))
    val GOLDEN_STARS_ON_CEIL: CustomItem = register(
        FnafUBlockDecorItem(
            "golden_stars_on_ceil",
            Blocks.GOLDEN_STARS_ON_CEIL
        )
    )
    val TRASH_ON_FLOOR: CustomItem = register(FnafUVanillaBasedItem("trash_on_floor", Material.DEAD_BUBBLE_CORAL))
    val DIRTY_FLOOR: CustomItem = register(FnafUVanillaBasedItem("dirty_floor", Material.DEAD_TUBE_CORAL))
    val BLOOD: CustomItem = register(FnafUVanillaBasedItem("blood", Material.REDSTONE))
    val UNIVERSAL_SLAB = register(UniversalSlab())

    // POSTERS

    object POSTER {
        val FNAF1_ALL = register(Poster("fnaf1/all"))
        val FNAF1_BONNIE = register(Poster("fnaf1/bonnie"))
        val FNAF1_CELEBRATE = register(Poster("fnaf1/celebrate"))
        val FNAF1_CHICA = register(Poster("fnaf1/chica"))
        val FNAF1_FOXY = register(Poster("fnaf1/foxy"))
        val FNAF1_FREDDY = register(Poster("fnaf1/freddy"))

        val FNAF2_ALL = register(Poster("fnaf2/all"))
        val FNAF2_BONNIE = register(Poster("fnaf2/bonnie"))
        val FNAF2_CHICA = register(Poster("fnaf2/chica"))

        val FNAF3_ALL = register(Poster("fnaf3/all"))
        val FNAF3_CHICA_AND_BONNIE = register(Poster("fnaf3/chica_and_bonnie"))
        val FNAF3_DRAWING_BALLOON_BOY = register(Poster("fnaf3/drawing_balloon_boy"))
        val FNAF3_DRAWING_CHIKA = register(Poster("fnaf3/drawing_chica"))
        val FNAF3_DRAWING_FREDDY = register(Poster("fnaf3/drawing_freddy"))
        val FNAF3_DRAWING_PUPPET = register(Poster("fnaf3/drawing_puppet"))
        val FNAF3_FREDDY = register(Poster("fnaf3/freddy"))
        val FNAF3_EXIT = register(Poster("fnaf3/exit", true))

        val WOMEN_RESTROOMS = register(Poster("women_restrooms"))
        val MEN_RESTROOMS = register(Poster("men_restrooms"))
        val RULES = register(Poster("rules"))
    }

    fun register(customItem: CustomItem): CustomItem {
        return CustomRegistries.ITEM.register(FnafU.instance, customItem)
    }
}
