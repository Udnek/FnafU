package me.udnek.fnafu.util

import me.udnek.coreu.custom.registry.CustomRegistries
import me.udnek.coreu.custom.sound.ConstructableCustomSound
import me.udnek.coreu.custom.sound.CustomSound
import me.udnek.fnafu.FnafU
import org.bukkit.NamespacedKey
import org.bukkit.SoundCategory

object Sounds {
    val AMBIENCE_FNAF1 : CustomSound = register(ConstructableCustomSound(getName("ambience_fnaf1"), SoundCategory.AMBIENT))
    val AMBIENCE_FNAF2 : CustomSound = register(ConstructableCustomSound(getName("ambience_fnaf2"), SoundCategory.AMBIENT))
    val DOOR : CustomSound = register(ConstructableCustomSound(getName("door"), SoundCategory.AMBIENT))

    val ANIMATRONIC_STEP : CustomSound = register(ConstructableCustomSound(getName("animatronic_step"), SoundCategory.HOSTILE))

    val FREDDY_LAUGH : CustomSound = register(ConstructableCustomSound(getName("freddy_laugh"), SoundCategory.HOSTILE))

    val SPRINGTRAP_SPAWN : CustomSound = register(ConstructableCustomSound(getName("springtrap_spawn"), SoundCategory.HOSTILE))
    val SPRINGTRAP_RAGE_START : CustomSound = register(ConstructableCustomSound(getName("springtrap_rage_start"), SoundCategory.HOSTILE))
    val SPRINGTRAP_RAGE_STOP : CustomSound = register(ConstructableCustomSound(getName("springtrap_rage_stop"), SoundCategory.HOSTILE))
    val SPRINGTRAP_SCREAMER : CustomSound = register(ConstructableCustomSound(getName("springtrap_screamer"), SoundCategory.HOSTILE))

    val PLUSHTRAP_RUN : CustomSound = register(ConstructableCustomSound(getName("plushtrap_run"), SoundCategory.HOSTILE))
    val PLUSHTRAP_NEAR : CustomSound = register(ConstructableCustomSound(getName("plushtrap_near"), SoundCategory.HOSTILE))

    private fun register(sound: CustomSound): CustomSound {
        return CustomRegistries.SOUND.register<CustomSound>(FnafU.instance, sound)
    }

    private fun getName(name: String): NamespacedKey {
        return NamespacedKey(FnafU.instance, name)
    }
}