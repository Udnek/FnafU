package me.udnek.fnafu.component

import me.udnek.coreu.custom.equipmentslot.slot.SingleSlot
import me.udnek.coreu.custom.equipmentslot.universal.BaseUniversalSlot
import me.udnek.coreu.custom.equipmentslot.universal.UniversalInventorySlot
import me.udnek.coreu.custom.item.CustomItem
import me.udnek.coreu.rpgu.component.ability.active.RPGUConstructableActiveAbility
import me.udnek.coreu.rpgu.component.ability.active.RPGUItemActiveAbility
import me.udnek.coreu.util.Either
import me.udnek.fnafu.misc.getFnafU
import me.udnek.fnafu.player.FnafUPlayer
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerInteractEvent

abstract class FnafUActiveAbility : RPGUConstructableActiveAbility<PlayerInteractEvent>(), RPGUItemActiveAbility<PlayerInteractEvent> {

    final override fun action(
        item: CustomItem,
        entity: LivingEntity,
        slot: Either<UniversalInventorySlot?, SingleSlot?>,
        event: PlayerInteractEvent
    ): ActionResult {
        (entity as? Player)?.getFnafU()?.let{
            return action(item, it, slot, event)
        }
        return ActionResult.NO_COOLDOWN
    }

    abstract fun action (
        item: CustomItem,
        player: FnafUPlayer,
        slot: Either<UniversalInventorySlot?, SingleSlot?>,
        event: PlayerInteractEvent
    ): ActionResult

    fun onRightClick(event: PlayerInteractEvent) {
        activate(
            CustomItem.get(event.item)!!,
            event.getPlayer(),
            Either(BaseUniversalSlot(event.hand!!), null),
            event
        )
    }

}
