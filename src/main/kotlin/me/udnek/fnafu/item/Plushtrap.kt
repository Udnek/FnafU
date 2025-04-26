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
            if (event.player.hasCooldown(event.item!!)) return@RightClickableItem
            event.player.getFnafU()?.also {
                it.abilities.getOrCreateDefault(Abilities.SPRINGTRAP_PLUSHTRAP).activate(it)
            }
            event.player.setCooldown(event.item!!, 20)
        })
    }

    override fun getRawId(): String = "plushtrap"
}
