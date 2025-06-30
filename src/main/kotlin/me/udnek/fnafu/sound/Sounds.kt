package me.udnek.fnafu.sound

import me.udnek.coreu.custom.registry.CustomRegistries
import me.udnek.coreu.custom.sound.ConstructableCustomSound
import me.udnek.coreu.custom.sound.CustomSound
import me.udnek.fnafu.FnafU
import org.bukkit.NamespacedKey
import org.bukkit.SoundCategory

object Sounds {
    val AMBIENCE_FNAF1 : CustomSound = register(ConstructableCustomSound(getName("ambience_fnaf1"), SoundCategory.MASTER, 0.3f, 1f))
    val AMBIENCE_FNAF2 : CustomSound = register(ConstructableCustomSound(getName("ambience_fnaf2"), SoundCategory.MASTER, 0.3f, 1f))
    val DOOR : CustomSound = register(ConstructableCustomSound(getName("door"), SoundCategory.MASTER, 3f, 1f))

    val ANIMATRONIC_STEP : CustomSound = register(ConstructableCustomSound(getName("animatronic_step"), SoundCategory.MASTER))

    val FREDDY_LAUGH : CustomSound = register(ConstructableCustomSound(getName("freddy_laugh"), SoundCategory.MASTER))

    val SPRINGTRAP_SPAWN : CustomSound = register(ConstructableCustomSound(getName("springtrap_spawn"), SoundCategory.MASTER))
    val SPRINGTRAP_RAGE_START : CustomSound = register(ConstructableCustomSound(getName("springtrap_rage_start"), SoundCategory.MASTER))
    val SPRINGTRAP_RAGE_STOP : CustomSound = register(ConstructableCustomSound(getName("springtrap_rage_stop"), SoundCategory.MASTER))
    val SPRINGTRAP_JUMP_SCARE : CustomSound = register(ConstructableCustomSound(getName("springtrap_jump_scare"), SoundCategory.MASTER))

    val PLUSHTRAP_RUN : CustomSound = register(ConstructableCustomSound(getName("plushtrap_run"), SoundCategory.MASTER, 2f, 1f))
    val PLUSHTRAP_NEAR : CustomSound = register(ConstructableCustomSound(getName("plushtrap_near"), SoundCategory.MASTER))

    private fun register(sound: CustomSound): CustomSound {
        return CustomRegistries.SOUND.register<CustomSound>(FnafU.instance, sound)
    }

    private fun getName(name: String): NamespacedKey {
        return NamespacedKey(FnafU.instance, name)
    }
}