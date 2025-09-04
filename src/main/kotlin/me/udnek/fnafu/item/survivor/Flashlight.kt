package me.udnek.fnafu.item.survivor

import me.udnek.coreu.custom.component.instance.TranslatableThing
import me.udnek.coreu.custom.item.ConstructableCustomItem
import me.udnek.coreu.rpgu.component.RPGUComponents
import me.udnek.fnafu.component.survivor.FlashlightAbility

class Flashlight : ConstructableCustomItem() {

    override fun getRawId(): String = "flashlight"
    override fun initializeComponents() {
        super.initializeComponents()
        components.getOrCreateDefault(RPGUComponents.ACTIVE_ABILITY_ITEM).components.set(FlashlightAbility.DEFAULT)
    }

    override fun getTranslations(): TranslatableThing {
        return TranslatableThing.ofEngAndRu("Flashlight", "Фонарик")
    }
}