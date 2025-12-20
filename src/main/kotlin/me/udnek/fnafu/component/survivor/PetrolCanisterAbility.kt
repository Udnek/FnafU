package me.udnek.fnafu.component.survivor

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
import me.udnek.fnafu.block.Blocks
import me.udnek.fnafu.component.FnafUActiveAbility
import me.udnek.fnafu.component.FnafUComponents
import me.udnek.fnafu.item.survivor.PetrolCanister
import me.udnek.fnafu.misc.getCustom
import me.udnek.fnafu.misc.toApache
import me.udnek.fnafu.player.FnafUPlayer
import me.udnek.fnafu.sound.Sounds
import net.kyori.adventure.text.Component
import org.apache.commons.lang3.tuple.Pair
import org.bukkit.event.player.PlayerInteractEvent
import kotlin.math.max
import kotlin.math.min

class PetrolCanisterAbility : FnafUActiveAbility {

    val maxCapacity: Int
    val bandwidthPerSec: Float

    companion object{
        val SMALL = PetrolCanisterAbility(15, 1.5f)
        val LARGE = PetrolCanisterAbility(25, 3f)
    }

    constructor(maxCapacity: Int, bandwidthPerSec: Float) : super() {
        this.maxCapacity = maxCapacity
        this.bandwidthPerSec = bandwidthPerSec
        components.set(AttributeBasedProperty(10.0, RPGUComponents.ABILITY_COOLDOWN_TIME))
    }


    override fun addPropertyLines(lorePart: AbilityLorePart) {
        super.addPropertyLines(lorePart)
        lorePart.addAbilityStat(Component.translatable(
            "ability.fnafu.petrol_canister.max_capacity",
            listOf(Component.text(maxCapacity))))
        lorePart.addAbilityStat(Component.translatable(
            "ability.fnafu.petrol_canister.bandwidth",
            listOf(Component.text(Utils.roundToTwoDigits(bandwidthPerSec.toDouble())))))
    }

    override fun getEngAndRuDescription(): Pair<List<String?>?, List<String?>?> {
        return (listOf("Fills at fuels station", "Usees petrol to charge generator") to
                listOf("Заполняется на заправке", "Использует топливо для подпитки генератора")).toApache()
    }


    override fun action(
        item: CustomItem,
        player: FnafUPlayer,
        slot: UniversalInventorySlot,
        event: PlayerInteractEvent
    ): ActionResult {
        val block = event.clickedBlock?.getCustom()
        var result: ActionResult
        var petrol: Float
        if (block == Blocks.PETROL_STATION || block == Blocks.GENERATOR){
            val old = event.item!!
            val new = old.clone()
            petrol = PetrolCanister.getPetrol(old)
            var change = bandwidthPerSec / 20 * getDefaultCooldown(player.player)
            if (block == Blocks.GENERATOR) {
                change = min(petrol, change)
                player.game.energy.energy += change
                change *= -1
            }
            petrol = Math.clamp((petrol + change), 0f, maxCapacity.toFloat())
            if (maxCapacity > petrol && petrol > 0){
                Sounds.PETROL_CANISTER_POUR.play(player.player.location)
            }
            PetrolCanister.setPetrol(new, petrol)
            player.currentInventory.replaceByType(new)
            player.player.inventory.setItem(event.hand!!, new)
            result = ActionResult.FULL_COOLDOWN
        } else{
            petrol = PetrolCanister.getPetrol(event.item!!)
            result = ActionResult.NO_COOLDOWN
        }
        player.player.sendActionBar(Component.translatable("ability.fnafu.petrol_canister.current_fuel",
            listOf(Component.text(Utils.roundToTwoDigits(petrol.toDouble())), Component.text(maxCapacity.toString()))))
        return result
    }

    override fun getType(): CustomComponentType<in RPGUActiveItem, out CustomComponent<in RPGUActiveItem>?> {
        return FnafUComponents.FUEL_CANISTER_ABILITY
    }
}
