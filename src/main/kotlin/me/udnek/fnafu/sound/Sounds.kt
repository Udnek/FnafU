package me.udnek.fnafu.sound

import me.udnek.coreu.custom.registry.CustomRegistries
import me.udnek.coreu.custom.sound.ConstructableCustomSound
import me.udnek.coreu.custom.sound.CustomSound
import me.udnek.fnafu.FnafU
import org.bukkit.NamespacedKey
import org.bukkit.SoundCategory

object Sounds {
    val AMBIENCE_FNAF1 = register(ConstructableCustomSound(getName("ambience_fnaf1"), SoundCategory.MASTER, 0.3f, 1f))
    val AMBIENCE_FNAF2 = register(ConstructableCustomSound(getName("ambience_fnaf2"), SoundCategory.MASTER, 0.3f, 1f))
    val AMBIENCE_FNAF3 = register(ConstructableCustomSound(getName("ambience_fnaf3"), SoundCategory.MASTER, 0.5f, 1f))

    val DOOR = register(ConstructableCustomSound(getName("door"), SoundCategory.MASTER, 3f, 1f))
    val POWER_OUTAGE = register(ConstructableCustomSound(getName("power_outage"), SoundCategory.MASTER, 8f, 1f))
    val PETROL_CANISTER_POUR= register(ConstructableCustomSound(getName("petrol_canister_pour"), SoundCategory.MASTER))
    val ANIMATRONIC_STEP = register(ConstructableCustomSound(getName("animatronic_step"), SoundCategory.MASTER))

    val FREDDY_LAUGH = register(ConstructableCustomSound(getName("freddy_laugh"), SoundCategory.MASTER))

    val SPRINGTRAP_SPAWN = register(ConstructableCustomSound(getName("springtrap_spawn"), SoundCategory.MASTER))
    val SPRINGTRAP_RAGE_START = register(ConstructableCustomSound(getName("springtrap_rage_start"), SoundCategory.MASTER))
    val SPRINGTRAP_RAGE_STOP = register(ConstructableCustomSound(getName("springtrap_rage_stop"), SoundCategory.MASTER))
    val SPRINGTRAP_JUMP_SCARE = register(ConstructableCustomSound(getName("springtrap_jump_scare"), SoundCategory.MASTER))
    val PLUSHTRAP_RUN = register(ConstructableCustomSound(getName("plushtrap_run"), SoundCategory.MASTER, 2f, 1f))
    val PLUSHTRAP_NEAR = register(ConstructableCustomSound(getName("plushtrap_near"), SoundCategory.MASTER))

    private fun register(sound: CustomSound): CustomSound {
        return CustomRegistries.SOUND.register<CustomSound>(FnafU.instance, sound)
    }

    private fun getName(name: String): NamespacedKey {
        return NamespacedKey(FnafU.instance, name)
    }
}