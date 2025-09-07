package me.udnek.fnafu.component.survivor.tablet

import me.udnek.coreu.custom.component.CustomComponent
import me.udnek.coreu.custom.component.CustomComponentType
import me.udnek.coreu.custom.equipmentslot.slot.CustomEquipmentSlot
import me.udnek.coreu.custom.equipmentslot.universal.UniversalInventorySlot
import me.udnek.coreu.custom.item.CustomItem
import me.udnek.coreu.rpgu.component.RPGUActiveItem
import me.udnek.coreu.util.Either
import me.udnek.fnafu.component.FnafUActiveAbility
import me.udnek.fnafu.component.FnafUComponents
import me.udnek.fnafu.misc.toApache
import me.udnek.fnafu.player.FnafUPlayer
import org.apache.commons.lang3.tuple.Pair
import org.bukkit.event.player.PlayerInteractEvent

open class SystemTabletAbility : FnafUActiveAbility() {

    companion object {
        val DEFAULT = SystemTabletAbility()
    }

    override fun getType(): CustomComponentType<in RPGUActiveItem, out CustomComponent<in RPGUActiveItem>?> {
        return FnafUComponents.SYSTEM_TABLET_ABILITY
    }

    override fun getEngAndRuDescription(): Pair<List<String?>?, List<String?>?> {
        return (listOf("Opens system reboot interface") to listOf("Открывает интерфейс починки систем")).toApache()
    }

    override fun action(
        item: CustomItem,
        player: FnafUPlayer,
        slot: Either<UniversalInventorySlot?, CustomEquipmentSlot.Single?>,
        event: PlayerInteractEvent
    ): ActionResult {
        player.game.systems.openTablet(player)
        return ActionResult.FULL_COOLDOWN
    }

}