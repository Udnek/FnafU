package me.udnek.fnafu.component.survivor.tablet

import me.udnek.coreu.custom.component.CustomComponent
import me.udnek.coreu.custom.component.CustomComponentType
import me.udnek.coreu.custom.equipmentslot.slot.SingleSlot
import me.udnek.coreu.custom.equipmentslot.universal.UniversalInventorySlot
import me.udnek.coreu.custom.item.CustomItem
import me.udnek.coreu.rpgu.component.RPGUActiveAbilityItem
import me.udnek.coreu.rpgu.component.RPGUComponents
import me.udnek.coreu.rpgu.component.ability.property.AttributeBasedProperty
import me.udnek.coreu.rpgu.lore.ability.ActiveAbilityLorePart
import me.udnek.coreu.util.Either
import me.udnek.coreu.util.Utils
import me.udnek.fnafu.component.FnafUActiveAbility
import me.udnek.fnafu.component.FnafUComponents
import me.udnek.fnafu.mechanic.system.ventilation.VentilationSystem
import me.udnek.fnafu.player.FnafUPlayer
import net.kyori.adventure.text.Component
import org.bukkit.event.player.PlayerInteractEvent

open class VentilationTabletAbility : FnafUActiveAbility {

    companion object {
        val DEFAULT = VentilationTabletAbility()
    }

    constructor(){
        components.set(AttributeBasedProperty(5.0 * 20, RPGUComponents.ABILITY_COOLDOWN_TIME))
    }

    override fun getType(): CustomComponentType<in RPGUActiveAbilityItem, out CustomComponent<in RPGUActiveAbilityItem>?> {
        return FnafUComponents.VENTILATION_TABLET_ABILITY
    }

    override fun addPropertyLines(componentable: ActiveAbilityLorePart) {
        super.addPropertyLines(componentable)
        componentable.addAbilityStat(
            Component.translatable("ability.fnafu.ventilation_tablet.damage_per_second", listOf(
                Component.text(Utils.roundToTwoDigits(VentilationSystem.CLOSED_VENT_DPS*100.0)))))
    }

    override fun action(
        item: CustomItem,
        player: FnafUPlayer,
        slot: Either<UniversalInventorySlot?, SingleSlot?>,
        event: PlayerInteractEvent
    ): ActionResult {
        val ventilation = player.game.systems.ventilation
        if (ventilation.isBroken || player.game.energy.isEndedUp) return ActionResult.NO_COOLDOWN
        ventilation.openMenu(player)
        return ActionResult.NO_COOLDOWN
    }

    fun onPlayerClickButton(item: CustomItem, player: FnafUPlayer) {
        cooldown(item, player.player)
    }
}