package me.udnek.fnafu.component.survivor.tablet

import me.udnek.coreu.custom.component.CustomComponent
import me.udnek.coreu.custom.component.CustomComponentType
import me.udnek.coreu.custom.equipmentslot.slot.SingleSlot
import me.udnek.coreu.custom.equipmentslot.universal.UniversalInventorySlot
import me.udnek.coreu.custom.item.CustomItem
import me.udnek.coreu.rpgu.component.RPGUActiveAbilityItem
import me.udnek.coreu.util.Either
import me.udnek.fnafu.component.FnafUActiveAbility
import me.udnek.fnafu.component.FnafUComponents
import me.udnek.fnafu.player.FnafUPlayer
import org.bukkit.event.player.PlayerInteractEvent

open class SystemTabletAbility : FnafUActiveAbility() {

    companion object {
        val DEFAULT = SystemTabletAbility()
    }

    override fun getType(): CustomComponentType<in RPGUActiveAbilityItem, out CustomComponent<in RPGUActiveAbilityItem>?> {
        return FnafUComponents.SYSTEM_TABLET_ABILITY
    }

    override fun action(
        item: CustomItem,
        player: FnafUPlayer,
        slot: Either<UniversalInventorySlot?, SingleSlot?>,
        event: PlayerInteractEvent
    ): ActionResult {
        player.game.systems.openTablet(player)
        return ActionResult.FULL_COOLDOWN
    }

}