package me.udnek.fnafu.item

import me.udnek.fnafu.component.Abilities
import me.udnek.fnafu.util.getFnafU
import me.udnek.itemscoreu.customcomponent.instance.RightClickableItem
import me.udnek.itemscoreu.customitem.ConstructableCustomItem

class Plushtrap : ConstructableCustomItem() {
    override fun initializeComponents() {
        super.initializeComponents()
        components.set(RightClickableItem
        { customItem, event ->
            event.player.getFnafU()?.also {
                it.abilities.getOrCreateDefault(Abilities.SPRINGTRAP_PLUSHTRAP).activate(it)
            }
        })
    }

    override fun getRawId(): String = "plushtrap"
}
