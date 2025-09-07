package me.udnek.fnafu.component.survivor.tablet

import me.udnek.coreu.custom.component.CustomComponent
import me.udnek.coreu.custom.component.CustomComponentType
import me.udnek.coreu.custom.equipmentslot.slot.CustomEquipmentSlot
import me.udnek.coreu.custom.equipmentslot.universal.UniversalInventorySlot
import me.udnek.coreu.custom.item.CustomItem
import me.udnek.coreu.rpgu.component.RPGUActiveItem
import me.udnek.coreu.rpgu.component.RPGUComponents
import me.udnek.coreu.rpgu.component.ability.property.AttributeBasedProperty
import me.udnek.coreu.rpgu.lore.ability.AbilityLorePart
import me.udnek.coreu.rpgu.lore.ability.ActiveAbilityLorePart
import me.udnek.coreu.util.Either
import me.udnek.coreu.util.Utils
import me.udnek.fnafu.component.FnafUActiveAbility
import me.udnek.fnafu.component.FnafUComponents
import me.udnek.fnafu.game.EnergyGame
import me.udnek.fnafu.player.FnafUPlayer
import net.kyori.adventure.text.Component
import org.bukkit.event.player.PlayerInteractEvent

open class DoormanTabletAbility : FnafUActiveAbility {

    companion object {
        val DEFAULT = DoormanTabletAbility()
        const val DAMAGE_PER_USAGE = EnergyGame.SURVIVOR_TOGGLE_DOOR_DAMAGE * 2
    }

    constructor(){
        components.set(AttributeBasedProperty(5.0 * 20, RPGUComponents.ABILITY_COOLDOWN_TIME))
    }

    override fun getType(): CustomComponentType<in RPGUActiveItem, out CustomComponent<in RPGUActiveItem>?> {
        return FnafUComponents.DOORMAN_TABLET_ABILITY
    }

    override fun addPropertyLines(lorePart: AbilityLorePart) {
        super.addPropertyLines(lorePart)
        lorePart.addAbilityStat(
            Component.translatable("ability.fnafu.doorman_tablet.damage_per_usage", listOf(
                Component.text(Utils.roundToTwoDigits(DAMAGE_PER_USAGE*100.0)))))
    }

    override fun action(
        item: CustomItem,
        player: FnafUPlayer,
        slot: Either<UniversalInventorySlot?, CustomEquipmentSlot.Single?>,
        event: PlayerInteractEvent
    ): ActionResult {
        val doors = player.game.systems.door
        if (doors.isBroken || player.game.energy.isEndedUp) return ActionResult.NO_COOLDOWN
        doors.openMenu(player)
        return ActionResult.NO_COOLDOWN
    }

    fun onPlayerClickButton(item: CustomItem, player: FnafUPlayer) {
        cooldown(item, player.player)
    }
}