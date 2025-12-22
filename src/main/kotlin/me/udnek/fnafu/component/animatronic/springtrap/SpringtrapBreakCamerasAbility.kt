package me.udnek.fnafu.component.animatronic.springtrap

import me.udnek.coreu.custom.component.CustomComponent
import me.udnek.coreu.custom.component.CustomComponentType
import me.udnek.coreu.custom.equipmentslot.universal.UniversalInventorySlot
import me.udnek.coreu.custom.item.CustomItem
import me.udnek.coreu.rpgu.component.RPGUActiveItem
import me.udnek.coreu.rpgu.component.RPGUComponents
import me.udnek.coreu.rpgu.component.ability.property.AttributeBasedProperty
import me.udnek.fnafu.component.FnafUActiveAbility
import me.udnek.fnafu.component.FnafUComponents
import me.udnek.fnafu.event.SystemRepairedEvent
import me.udnek.fnafu.player.FnafUPlayer
import org.apache.commons.lang3.tuple.Pair
import org.bukkit.event.player.PlayerInteractEvent


open class SpringtrapBreakCamerasAbility : FnafUActiveAbility {

    companion object {
        val DEFAULT = SpringtrapBreakCamerasAbility()
    }

    constructor(){
        components.set(AttributeBasedProperty(30.0*20, RPGUComponents.ABILITY_COOLDOWN_TIME))
    }

    override fun action(
        item: CustomItem,
        player: FnafUPlayer,
        slot: UniversalInventorySlot,
        event: PlayerInteractEvent
    ): ActionResult {
        val systems = player.game.systems
        if (!systems.camera.isBroken) {
            systems.camera.destroy()
        }

        return ActionResult.INFINITE_COOLDOWN
    }

    fun onSystemRepaired(item: CustomItem, player: FnafUPlayer, event: SystemRepairedEvent){
        cooldown(item, player.player)
    }

    override fun getType(): CustomComponentType<in RPGUActiveItem, out CustomComponent<in RPGUActiveItem>?> {
        return FnafUComponents.SPRINGTRAP_BREAK_CAMERAS_ABILITY
    }

    override fun getEngAndRuDescription(): Pair<List<String?>?, List<String?>?>? {
        return Pair.of(listOf("Breaks camera system"), listOf("Ломает систему камер"))
    }
}





