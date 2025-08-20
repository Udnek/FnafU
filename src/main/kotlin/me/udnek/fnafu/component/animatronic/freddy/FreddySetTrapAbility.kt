package me.udnek.fnafu.component.animatronic.freddy

import me.udnek.coreu.custom.component.CustomComponent
import me.udnek.coreu.custom.component.CustomComponentType
import me.udnek.coreu.custom.equipmentslot.slot.SingleSlot
import me.udnek.coreu.custom.equipmentslot.universal.UniversalInventorySlot
import me.udnek.coreu.custom.item.CustomItem
import me.udnek.coreu.nms.Nms
import me.udnek.coreu.rpgu.component.RPGUActiveAbilityItem
import me.udnek.coreu.rpgu.component.RPGUComponents
import me.udnek.coreu.rpgu.component.ability.property.AttributeBasedProperty
import me.udnek.coreu.util.Either
import me.udnek.fnafu.component.FnafUActiveAbility
import me.udnek.fnafu.component.FnafUComponents
import me.udnek.fnafu.entity.EntityTypes
import me.udnek.fnafu.player.FnafUPlayer
import org.bukkit.event.player.PlayerInteractEvent


open class FreddySetTrapAbility : FnafUActiveAbility {

    companion object {
        val DEFAULT = FreddySetTrapAbility()
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
        val location = player.player.location
        location.pitch = 0f
        location.yaw = 0f
        location.add(0.0, 0.2, 0.0)

        player.data.getOrDefault(FnafUComponents.FREDDY_TRAP_DATA).trap?.remove()
        val trap = EntityTypes.REMNANT_TRAP.spawnAndGet(location)
        trap.game = player.game
        trap.teleportLocation = player.player.location
        player.data.getOrCreateDefault(FnafUComponents.FREDDY_TRAP_DATA).trap = trap

        player.game.playerContainer.survivors.forEach {
            Nms.get().sendFakeDestroyEntities(listOf(trap.real), it.player)
        }
        return ActionResult.FULL_COOLDOWN
    }



    override fun getType(): CustomComponentType<out RPGUActiveAbilityItem, out CustomComponent<RPGUActiveAbilityItem>> {
        return FnafUComponents.FREDDY_SET_TRAP_ABILITY
    }
}





