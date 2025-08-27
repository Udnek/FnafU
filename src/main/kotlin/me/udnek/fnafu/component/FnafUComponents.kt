package me.udnek.fnafu.component

import me.udnek.coreu.custom.component.ConstructableComponentType
import me.udnek.coreu.custom.component.CustomComponentType
import me.udnek.coreu.custom.registry.CustomRegistries
import me.udnek.fnafu.FnafU
import me.udnek.fnafu.component.animatronic.MovementTrackerComponent
import me.udnek.fnafu.component.animatronic.freddy.FreddySetTrapAbility
import me.udnek.fnafu.component.animatronic.freddy.FreddyShadowAbility
import me.udnek.fnafu.component.animatronic.freddy.FreddyTeleportToTrapAbility
import me.udnek.fnafu.component.animatronic.freddy.FreddyTrapData
import me.udnek.fnafu.component.animatronic.springtrap.SpringtrapBreakCamerasAbility
import me.udnek.fnafu.component.animatronic.springtrap.SpringtrapPlushtrapAbility
import me.udnek.fnafu.component.animatronic.springtrap.SpringtrapPlushtrapData
import me.udnek.fnafu.component.kit.Kit
import me.udnek.fnafu.component.survivor.*
import me.udnek.fnafu.component.survivor.tablet.CameraTabletAbility
import me.udnek.fnafu.component.survivor.tablet.DoormanTabletAbility
import me.udnek.fnafu.component.survivor.tablet.SystemTabletAbility
import me.udnek.fnafu.component.survivor.tablet.VentilationTabletAbility

object FnafUComponents {
    val CAMERA_TABLET_ABILITY = register(ConstructableComponentType("camera_tablet_ability", CameraTabletAbility.FULL))
    val DOORMAN_TABLET_ABILITY = register(ConstructableComponentType("doorman_tablet_ability", DoormanTabletAbility.DEFAULT))
    val SYSTEM_TABLET_ABILITY = register(ConstructableComponentType("system_tablet_ability", SystemTabletAbility.DEFAULT))
    val VENTILATION_TABLET_ABILITY = register(ConstructableComponentType("ventilation_tablet_ability", VentilationTabletAbility.DEFAULT))
    val FUEL_CANISTER_ABILITY = register(ConstructableComponentType("fuel_canister_ability", PetrolCanisterAbility.SMALL))
    val FLASHLIGHT_ABILITY = register(ConstructableComponentType("flashlight_ability", FlashlightAbility.DEFAULT))

    val SPECTATE_ENTITY_DATA = register(ConstructableComponentType("spectate_entity_data", SpectateEntityData.DEFAULT) { SpectateEntityData() })
    val SPECTATE_CAMERA_DATA = register(ConstructableComponentType("spectate_camera_data", SpectateCameraData.DEFAULT) { SpectateCameraData() })

    val CURRENT_INVENTORY_DATA = register(ConstructableComponentType("current_inventory_data", CurrentInventoryData.DEFAULT) { CurrentInventoryData()} )

    val MOVEMENT_TRACKER_DATA = register(ConstructableComponentType("movement_tracker_data", MovementTrackerComponent.DEFAULT) { MovementTrackerComponent() })

    val SPRINGTRAP_PLUSHTRAP_ABILITY = register(ConstructableComponentType("springtrap_plushtrap_ability", SpringtrapPlushtrapAbility.DEFAULT))
    val SPRINGTRAP_PLUSHTRAP_DATA = register(ConstructableComponentType("springtrap_plushtrap_data", SpringtrapPlushtrapData.DEFAULT) { SpringtrapPlushtrapData() })
    val SPRINGTRAP_BREAK_CAMERAS_ABILITY = register(ConstructableComponentType("springtrap_breaks_camera_ability", SpringtrapBreakCamerasAbility.DEFAULT))

    val FREDDY_SHADOW_ABILITY = register(ConstructableComponentType("freddy_shadow_ability", FreddyShadowAbility.DEFAULT))
    val FREDDY_SET_TRAP_ABILITY = register(ConstructableComponentType("freddy_set_trap_ability", FreddySetTrapAbility.DEFAULT))
    val FREDDY_TELEPORT_TO_TRAP_ABILITY = register(ConstructableComponentType("freddy_teleport_to_trap_ability", FreddyTeleportToTrapAbility.DEFAULT))
    val FREDDY_TRAP_DATA = register(ConstructableComponentType("freddy_trap_data", FreddyTrapData.DEFAULT) { FreddyTrapData() })

    val KIT = register(ConstructableComponentType("kit", Kit.CAMERAMAN))
    val KIT_STAGE_DATA = register(ConstructableComponentType("kit_stage_data", KitStageData.DEFAULT) { KitStageData() })

    private fun <C : CustomComponentType<*, *>> register(type: C): C {
        return CustomRegistries.COMPONENT_TYPE.register(FnafU.instance, type)
    }
}
