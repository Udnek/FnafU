package me.udnek.fnafu.component.survivor

import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.datacomponent.item.BundleContents
import me.udnek.coreu.custom.component.CustomComponent
import me.udnek.coreu.custom.component.CustomComponentType
import me.udnek.coreu.custom.equipmentslot.slot.CustomEquipmentSlot
import me.udnek.coreu.custom.equipmentslot.universal.BaseUniversalSlot
import me.udnek.coreu.custom.equipmentslot.universal.UniversalInventorySlot
import me.udnek.coreu.custom.item.CustomItem
import me.udnek.coreu.nms.Nms
import me.udnek.coreu.rpgu.component.RPGUToggleItem
import me.udnek.coreu.rpgu.component.ability.toggle.RPGUConstructableToggleAbility
import me.udnek.coreu.rpgu.lore.ability.AbilityLorePart
import me.udnek.coreu.util.FakeBlock
import me.udnek.coreu.util.Utils
import me.udnek.fnafu.component.FnafUComponents
import me.udnek.fnafu.item.survivor.Flashlight
import me.udnek.fnafu.misc.getFnafU
import me.udnek.fnafu.misc.toApache
import me.udnek.fnafu.player.FnafUPlayer
import me.udnek.fnafu.sound.Sounds
import net.kyori.adventure.text.Component
import org.apache.commons.lang3.tuple.Pair
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.block.data.BlockData
import org.bukkit.block.data.type.Light
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import kotlin.math.max

class FlashlightBeamAbility : RPGUConstructableToggleAbility() {

    companion object {
        val DRAIN_PER_SECOND = 1f
        val DEFAULT = FlashlightBeamAbility()
        private val beam: BlockData = (Material.LIGHT.createBlockData() as Light).also { it.level = 8 }
        private val beamCollision: BlockData = (Material.LIGHT.createBlockData() as Light).also { it.level = 9 }
    }

    override fun setToggled(
        customItem: CustomItem,
        player: Player,
        slot: UniversalInventorySlot,
        toggle: Boolean
    ): Boolean {
        val wasToggled = isToggled(customItem, player, slot)
        if (wasToggled == toggle) return toggle

        Sounds.FLASHLIGHT_CLICK.play(player.location)
        val charge = Flashlight.getCharge(slot.getItem(player)!!)
        if (charge == 0f && toggle){
            return false
        } else if (toggle) {
            slot.modifyItem({ it.setData(DataComponentTypes.BUNDLE_CONTENTS, BundleContents.bundleContents()); it}, player)
        } else {
            slot.modifyItem( { it.unsetData(DataComponentTypes.BUNDLE_CONTENTS); it }, player)
        }
        player.getFnafU()?.currentInventory?.replaceByType(slot.getItem(player)!!)
        return super.setToggled(customItem, player, slot, toggle)
    }

    override fun action(
        item: CustomItem,
        entity: LivingEntity,
        slot: UniversalInventorySlot,
        delay: Int
    ): ActionResult {
        val player = (entity as? Player)?.getFnafU() ?: return ActionResult.NO_COOLDOWN
        if (drainEnergy(player, slot, delay) > 0){
            lightBeam(player)
        } else{
            setToggled(item, player.player, slot, false)
        }
        return ActionResult.FULL_COOLDOWN
    }

    fun drainEnergy(player: FnafUPlayer, slot: UniversalInventorySlot, delay: Int): Float {
        var charge = 0f
        slot.modifyItem({ item ->
            charge = Flashlight.getCharge(item)
            if (charge == 0f) return@modifyItem item
            charge -= DRAIN_PER_SECOND / 20f * delay.toFloat()
            charge = max(0f, charge)
            Flashlight.setCharge(item, charge)
            player.currentInventory.replaceByType(item)
            return@modifyItem item
        }, player.player)
        return charge
    }

    fun lightBeam(player: FnafUPlayer){
        val location = player.player.eyeLocation
        val direction = location.direction.normalize()
        val players = player.game.playerContainer.all.map { it.player }
        for (i in  0..15) {
            val previousLocation = location.clone()
            location.add(direction)
            if (Nms.get().getHowMuchLightBlockBlocks(location.block) > 1){
                if (previousLocation.block.isEmpty) {
                    FakeBlock.show(previousLocation, beamCollision, players, 1)
                }
                break
            } else if (previousLocation.block.isEmpty) {
                FakeBlock.show(previousLocation, beam, players, 1)
            }
        }
        return
    }

    override fun getEngAndRuDescription(): Pair<List<String?>?, List<String?>?> {
        return (listOf("Toggles flashlight") to
                listOf("Переключает фонарик")).toApache()
    }

    override fun addPropertyLines(lorePart: AbilityLorePart) {
        super.addPropertyLines(lorePart)
        lorePart.addAbilityStat(Component.translatable(
            "ability.fnafu.flashlight_beam.drain_rate_per_second",
            listOf(Component.text(Utils.roundToTwoDigits(DRAIN_PER_SECOND.toDouble())))))
        lorePart.addAbilityStat(Component.translatable(
            "ability.fnafu.flashlight_beam.button",
            listOf(Component.keybind("key.swapOffhand"))))
    }

    override fun getSlot(): CustomEquipmentSlot = CustomEquipmentSlot.OFF_HAND

    override fun getType(): CustomComponentType<in RPGUToggleItem, out CustomComponent<in RPGUToggleItem>?> {
        return FnafUComponents.FLASHLIGHT_BEAM_ABILITY
    }
}
