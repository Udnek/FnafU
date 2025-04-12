package me.udnek.fnafu.util

import me.udnek.fnafu.FnafU
import me.udnek.itemscoreu.customregistry.CustomRegistries
import me.udnek.itemscoreu.customsound.ConstructableCustomSound
import me.udnek.itemscoreu.customsound.CustomSound
import org.bukkit.NamespacedKey
import org.bukkit.SoundCategory

object Sounds {
    val AMBIENCE : CustomSound = register(ConstructableCustomSound(NamespacedKey(FnafU.instance, "ambience"), SoundCategory.AMBIENT, 1f, 1f))

    private fun register(sound: CustomSound): CustomSound {
        return CustomRegistries.SOUND.register<CustomSound>(FnafU.instance, sound)
    }
}
///TODO Фиксится 4%