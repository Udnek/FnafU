package me.udnek.fnafu.component.animatronic.freddy

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
import me.udnek.fnafu.player.FnafUPlayer
import org.bukkit.event.player.PlayerInteractEvent


open class FreddyTeleportToTrapAbility : FnafUActiveAbility {

    companion object {
        val DEFAULT = FreddyTeleportToTrapAbility()
    }

    constructor(){
        components.set(AttributeBasedProperty(5.0*20, RPGUComponents.ABILITY_COOLDOWN_TIME))
    }

    override fun action(
        item: CustomItem,
        player: FnafUPlayer,
        slot: Either<UniversalInventorySlot?, SingleSlot?>,
        event: PlayerInteractEvent
    ): ActionResult {
        player.teleport(player.data.getOrDefault(FnafUComponents.FREDDY_TRAP_DATA).trap?.real?.location ?: return ActionResult.NO_COOLDOWN)
        return ActionResult.FULL_COOLDOWN
    }



    override fun getType(): CustomComponentType<out RPGUActiveAbilityItem, out CustomComponent<RPGUActiveAbilityItem>> {
        return FnafUComponents.FREDDY_TELEPORT_TO_TRAP_ABILITY
    }
}





