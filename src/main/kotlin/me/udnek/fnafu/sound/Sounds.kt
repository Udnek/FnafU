package me.udnek.fnafu.sound

import me.udnek.coreu.custom.registry.CustomRegistries
import me.udnek.coreu.custom.sound.ConstructableCustomSound
import me.udnek.fnafu.FnafU
import org.bukkit.NamespacedKey
import org.bukkit.SoundCategory

object Sounds {
    val AMBIENCE_FNAF1 = register(ConstructableCustomSound(getName("ambience_fnaf1"), SoundCategory.MASTER, 0.3f))
    val AMBIENCE_FNAF2 = register(ConstructableCustomSound(getName("ambience_fnaf2"), SoundCategory.MASTER, 0.3f))
    val AMBIENCE_FNAF3 = register(ConstructableCustomSound(getName("ambience_fnaf3"), SoundCategory.MASTER, 0.5f))

    val DOOR_SHUT = register(ConstructableCustomSound(getName("door_shut"), SoundCategory.MASTER, 3f))
    val DOOR_SLIDING = register(ConstructableCustomSound(getName("door_sliding"), SoundCategory.MASTER, 3f))
    val VENT_TOGGLE = register(ConstructableCustomSound(getName("vent_toggle"), SoundCategory.MASTER, 1.5f))
    val POWER_OUTAGE = register(ConstructableCustomSound(getName("power_outage"), SoundCategory.MASTER, 8f))
    val PETROL_CANISTER_POUR = register(ConstructableCustomSound(getName("petrol_canister_pour"), SoundCategory.MASTER))
    val HAPPY_END = register(ConstructableCustomSound(getName("happy_end"), SoundCategory.MASTER, 0.5f))
    val CAMERA_SWITCH = register(ConstructableCustomSound(getName("camera_switch"), SoundCategory.MASTER, 40f))
    val CAMERA_TABLET_OPEN = register(ConstructableCustomSound(getName("camera_tablet_open"), SoundCategory.MASTER, 40f))
    val SYSTEM_REBOOT = register(ConstructableCustomSound(getName("system_reboot"), SoundCategory.MASTER, 0.1f))
    val BUTTON_CLICK = register(ConstructableCustomSound(getName("button_click"), SoundCategory.MASTER, 0.3f))
    val SYSTEM_REPAIRED = BUTTON_CLICK
    val ANIMATRONIC_STEP = register(ConstructableCustomSound(getName("animatronic_step"), SoundCategory.MASTER))

    val FREDDY_LAUGH = register(ConstructableCustomSound(getName("freddy_laugh"), SoundCategory.MASTER))

    val SPRINGTRAP_SPAWN = register(ConstructableCustomSound(getName("springtrap_spawn"), SoundCategory.MASTER))
    val SPRINGTRAP_RAGE_START = register(ConstructableCustomSound(getName("springtrap_rage_start"), SoundCategory.MASTER))
    val SPRINGTRAP_RAGE_STOP = register(ConstructableCustomSound(getName("springtrap_rage_stop"), SoundCategory.MASTER))
    val SPRINGTRAP_JUMP_SCARE = register(ConstructableCustomSound(getName("springtrap_jump_scare"), SoundCategory.MASTER))
    val PLUSHTRAP_RUN = register(ConstructableCustomSound(getName("plushtrap_run"), SoundCategory.MASTER, 2f))
    val PLUSHTRAP_NEAR = register(ConstructableCustomSound(getName("plushtrap_near"), SoundCategory.MASTER))

    private fun register(sound: ConstructableCustomSound): ConstructableCustomSound {
        return CustomRegistries.SOUND.register<ConstructableCustomSound>(FnafU.instance, sound)
    }

    private fun getName(name: String): NamespacedKey {
        return NamespacedKey(FnafU.instance, name)
    }
}