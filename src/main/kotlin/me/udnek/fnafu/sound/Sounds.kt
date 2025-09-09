package me.udnek.fnafu.sound

import me.udnek.coreu.custom.component.instance.TranslatableThing
import me.udnek.coreu.custom.registry.CustomRegistries
import me.udnek.coreu.custom.sound.ConstructableCustomSound
import me.udnek.fnafu.FnafU
import org.bukkit.NamespacedKey
import org.bukkit.SoundCategory

object Sounds {
    val AMBIENCE_FNAF1 = register(ConstructableCustomSound(
        "ambience/fnaf1", SoundCategory.MASTER,  TranslatableThing.ofEng("FNAF 1 Ambience"),0.3f))
    val AMBIENCE_FNAF2 = register(ConstructableCustomSound(
        "ambience/fnaf2", SoundCategory.MASTER, TranslatableThing.ofEng("FNAF 2 Ambience"),0.3f))
    val AMBIENCE_FNAF3 = register(ConstructableCustomSound(
        "ambience/fnaf3", SoundCategory.MASTER, TranslatableThing.ofEng("FNAF 3 Ambience"),0.5f))

    val DOOR_SHUT = register(ConstructableCustomSound(
        "door_shut", SoundCategory.MASTER, TranslatableThing.ofEng("Door shuts"), 3f))
    val DOOR_SLIDING = register(ConstructableCustomSound(
        "door_sliding", SoundCategory.MASTER, TranslatableThing.ofEng("Door slides"), 3f))
    val VENT_TOGGLE = register(ConstructableCustomSound(
        "vent_toggle",SoundCategory.MASTER, TranslatableThing.ofEng("Vent toggles"), 1.5f))
    val POWER_OUTAGE = register(ConstructableCustomSound(
        "power_outage", SoundCategory.MASTER, TranslatableThing.ofEng("Power is out"),8f))
    val PETROL_CANISTER_POUR = register(ConstructableCustomSound(
        "petrol_canister_pour", SoundCategory.MASTER, TranslatableThing.ofEng("Canister pours")))
    val HAPPY_END = register(ConstructableCustomSound(
        "happy_end", SoundCategory.MASTER, TranslatableThing.ofEng("Survivors win"), 0.5f))
    val CAMERA_SWITCH = register(ConstructableCustomSound(
        "ui/camera_switch", SoundCategory.MASTER, TranslatableThing.ofEng("Camera switches"), 40f))
    val CAMERA_TABLET_OPEN = register(ConstructableCustomSound(
        "ui/camera_tablet_open", SoundCategory.MASTER, TranslatableThing.ofEng("Camera Tablet opens"), 40f))
    val SYSTEM_REBOOT = register(ConstructableCustomSound(
        "ui/system_reboot", SoundCategory.MASTER, TranslatableThing.ofEng("System reboots"), 0.1f))
    val BUTTON_CLICK = register(ConstructableCustomSound(
        "ui/button_click", SoundCategory.MASTER, TranslatableThing.ofEng("Button clicks"), .3f))
    val SYSTEM_REPAIRED = BUTTON_CLICK
    val ANIMATRONIC_STEP = register(ConstructableCustomSound(
        "animatronic_step", SoundCategory.MASTER, TranslatableThing.ofEng("Animatronic steps"),
        listOf("animatronic_step/0", "animatronic_step/1", "animatronic_step/2", "animatronic_step/3"), 1f, 1f))

    val FREDDY_LAUGH = register(ConstructableCustomSound(
        "freddy/laugh", SoundCategory.MASTER, TranslatableThing.ofEng("Freddy laughs")))
    val FREDDY_JUMP_SCARE = register(ConstructableCustomSound(
        "freddy/jump_scare", SoundCategory.MASTER, TranslatableThing.ofEng("Freddy jump scares")))

//    val SPRINGTRAP_SPAWN = register(ConstructableCustomSound(getName("springtrap_spawn"), SoundCategory.MASTER))
//    val SPRINGTRAP_RAGE_START = register(ConstructableCustomSound(getName("springtrap_rage_start"), SoundCategory.MASTER))
//    val SPRINGTRAP_RAGE_STOP = register(ConstructableCustomSound(getName("springtrap_rage_stop"), SoundCategory.MASTER))
    val SPRINGTRAP_JUMP_SCARE = register(ConstructableCustomSound(
    "springtrap/jump_scare", SoundCategory.MASTER, TranslatableThing.ofEng("Springtrap jump scares")))
    val PLUSHTRAP_RUN = register(ConstructableCustomSound(
    "springtrap/plushtrap/run", SoundCategory.MASTER, TranslatableThing.ofEng("Plushtrap runs"), 2f))
    val PLUSHTRAP_NEAR = register(ConstructableCustomSound(
        "springtrap/plushtrap/near", SoundCategory.MASTER, TranslatableThing.ofEng("Plushtrap is near")))

    private fun register(sound: ConstructableCustomSound): ConstructableCustomSound {
        return CustomRegistries.SOUND.register<ConstructableCustomSound>(FnafU.instance, sound)
    }

    private fun getName(name: String): NamespacedKey= NamespacedKey(FnafU.instance, name)
}