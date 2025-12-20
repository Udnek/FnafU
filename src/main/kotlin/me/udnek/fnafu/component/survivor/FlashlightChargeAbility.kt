package me.udnek.fnafu.component.survivor

import me.udnek.coreu.custom.component.CustomComponent
import me.udnek.coreu.custom.component.CustomComponentType
import me.udnek.coreu.custom.equipmentslot.universal.UniversalInventorySlot
import me.udnek.coreu.custom.item.CustomItem
import me.udnek.coreu.rpgu.component.RPGUActiveItem
import me.udnek.coreu.rpgu.component.RPGUComponents
import me.udnek.coreu.rpgu.component.ability.property.AttributeBasedProperty
import me.udnek.coreu.rpgu.lore.ability.AbilityLorePart
import me.udnek.coreu.util.Utils
import me.udnek.fnafu.block.Blocks
import me.udnek.fnafu.component.FnafUActiveAbility
import me.udnek.fnafu.component.FnafUComponents
import me.udnek.fnafu.item.survivor.Flashlight
import me.udnek.fnafu.misc.getCustom
import me.udnek.fnafu.misc.toApache
import me.udnek.fnafu.player.FnafUPlayer
import net.kyori.adventure.text.Component
import org.apache.commons.lang3.tuple.Pair
import org.bukkit.event.player.PlayerInteractEvent
import kotlin.math.min

class FlashlightChargeAbility : FnafUActiveAbility {

    companion object{
        val MAX_CHARGE: Float = 60f
        val CHARGE_RATE_PER_SECOND: Float = 10f

        val DEFAULT = FlashlightChargeAbility()
    }

    constructor(){
        components.set(AttributeBasedProperty(20.0, RPGUComponents.ABILITY_COOLDOWN_TIME))
    }

    override fun addPropertyLines(lorePart: AbilityLorePart) {
        super.addPropertyLines(lorePart)
        lorePart.addAbilityStat(Component.translatable(
            "ability.fnafu.flashlight_charge.max_charge",
            listOf(Component.text(Utils.roundToTwoDigits(MAX_CHARGE.toDouble())))))
        lorePart.addAbilityStat(Component.translatable(
            "ability.fnafu.flashlight_charge.charge_rate_per_second",
            listOf(Component.text(Utils.roundToTwoDigits(CHARGE_RATE_PER_SECOND.toDouble())))))

    }

    override fun getEngAndRuDescription(): Pair<List<String?>?, List<String?>?> {
        return (listOf("Charges at generator (doesn't drain generator power)") to
                listOf("Заряжается от генератора (не расходует заряд генератора)")).toApache()
    }


    override fun action(
        item: CustomItem,
        player: FnafUPlayer,
        slot: UniversalInventorySlot,
        event: PlayerInteractEvent
    ): ActionResult {
        val block = event.clickedBlock?.getCustom()
        if (block != Blocks.GENERATOR) return ActionResult.NO_COOLDOWN
        if (player.game.energy.isEndedUp) return ActionResult.NO_COOLDOWN
        var chargedAtLeastABit = false
        slot.modifyItem({item ->
            var charge = Flashlight.getCharge(item)
            if (charge == MAX_CHARGE) return@modifyItem item
            chargedAtLeastABit = true
            charge += CHARGE_RATE_PER_SECOND / 20f * getDefaultCooldown(player.player).toFloat()
            charge = min(MAX_CHARGE, charge)
            Flashlight.setCharge(item, charge)
            player.currentInventory.replaceByType(item)
            return@modifyItem item
        }, player.player)

        if (chargedAtLeastABit){
            return ActionResult.FULL_COOLDOWN
        }
        return ActionResult.NO_COOLDOWN
    }

    override fun getType(): CustomComponentType<in RPGUActiveItem, out CustomComponent<in RPGUActiveItem>?> {
        return FnafUComponents.FLASHLIGHT_CHARGE_ABILITY
    }
}
