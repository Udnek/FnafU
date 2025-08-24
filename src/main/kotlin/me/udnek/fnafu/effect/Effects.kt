package me.udnek.fnafu.effect

import me.udnek.coreu.custom.effect.CustomEffect
import me.udnek.coreu.custom.registry.CustomRegistries
import me.udnek.fnafu.FnafU

object Effects {
    val STUN_EFFECT = register(StunEffect())

    private fun register(customEffect: CustomEffect): CustomEffect {
        return CustomRegistries.EFFECT.register(FnafU.instance, customEffect)
    }
}