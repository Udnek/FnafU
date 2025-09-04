package me.udnek.fnafu.component.animatronic.springtrap

import me.udnek.coreu.custom.component.CustomComponent
import me.udnek.coreu.custom.component.CustomComponentType
import me.udnek.coreu.custom.equipmentslot.slot.SingleSlot
import me.udnek.coreu.custom.equipmentslot.universal.UniversalInventorySlot
import me.udnek.coreu.custom.item.CustomItem
import me.udnek.coreu.rpgu.component.RPGUActiveAbilityItem
import me.udnek.coreu.rpgu.component.RPGUComponents
import me.udnek.coreu.rpgu.component.ability.property.AttributeBasedProperty
import me.udnek.coreu.util.Either
import me.udnek.fnafu.component.FnafUActiveAbility
import me.udnek.fnafu.component.FnafUComponents
import me.udnek.fnafu.event.SystemRepairedEvent
import me.udnek.fnafu.player.FnafUPlayer
import org.bukkit.event.player.PlayerInteractEvent


open class SpringtrapBreakCamerasAbility : FnafUActiveAbility {

    companion object {
        val DEFAULT = SpringtrapBreakCamerasAbility()
    }

    constructor(){
        components.set(AttributeBasedProperty(40.0*20, RPGUComponents.ABILITY_COOLDOWN_TIME))
    }

    override fun action(
        item: CustomItem,
        player: FnafUPlayer,
        slot: Either<UniversalInventorySlot?, SingleSlot?>,
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

    override fun getType(): CustomComponentType<in RPGUActiveAbilityItem, out CustomComponent<in RPGUActiveAbilityItem>?> {
        return FnafUComponents.SPRINGTRAP_BREAK_CAMERAS_ABILITY
    }
}





