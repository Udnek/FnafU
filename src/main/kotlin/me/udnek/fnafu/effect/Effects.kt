package me.udnek.fnafu.effect

import me.udnek.fnafu.FnafU
import me.udnek.itemscoreu.customeffect.CustomEffect
import me.udnek.itemscoreu.customregistry.CustomRegistries

object Effects {
    val STUN_EFFECT: CustomEffect = register(StunEffect())

    private fun register(customEffect: CustomEffect): CustomEffect {
        return CustomRegistries.EFFECT.register(FnafU.instance, customEffect)
    }
}