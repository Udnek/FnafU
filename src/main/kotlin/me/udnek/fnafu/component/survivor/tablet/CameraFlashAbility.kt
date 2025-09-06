package me.udnek.fnafu.component.survivor.tablet

import me.udnek.coreu.custom.component.CustomComponent
import me.udnek.coreu.custom.component.CustomComponentType
import me.udnek.coreu.custom.equipmentslot.slot.CustomEquipmentSlot
import me.udnek.coreu.custom.equipmentslot.universal.UniversalInventorySlot
import me.udnek.coreu.custom.item.CustomItem
import me.udnek.coreu.rpgu.component.RPGUActiveItem
import me.udnek.coreu.rpgu.component.RPGUComponents
import me.udnek.coreu.rpgu.component.ability.active.RPGUConstructableActiveAbility
import me.udnek.coreu.rpgu.component.ability.property.AttributeBasedProperty
import me.udnek.coreu.rpgu.component.ability.property.EffectsProperty
import me.udnek.coreu.util.Either
import me.udnek.fnafu.component.FnafUComponents
import me.udnek.fnafu.misc.getFnafU
import org.bukkit.Color
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.potion.PotionEffectType

class CameraFlashAbility : RPGUConstructableActiveAbility<InventoryClickEvent> {

    companion object{
        val DEFAULT = CameraFlashAbility()
    }

    constructor(){
        components.set(AttributeBasedProperty(3.0 * 20, RPGUComponents.ABILITY_COOLDOWN_TIME))
        components.set(AttributeBasedProperty(3.0 * 20, RPGUComponents.ABILITY_DURATION))
        components.set(EffectsProperty(
            EffectsProperty.PotionData(PotionEffectType.SLOWNESS, 3 * 20, 0)
        ))
    }

    override fun getType(): CustomComponentType<in RPGUActiveItem, out CustomComponent<in RPGUActiveItem>?> {
        return FnafUComponents.CAMERA_FLASH_ABILITY
    }

    override fun action(
        customItem: CustomItem,
        entity: LivingEntity,
        slot: Either<UniversalInventorySlot?, CustomEquipmentSlot.Single?>,
        event: InventoryClickEvent
    ): ActionResult {
        val player = (entity as? Player)?.getFnafU() ?: return ActionResult.NO_COOLDOWN
        val camera = player.game.systems.camera.getSpectatingCamera(player) ?: return ActionResult.NO_COOLDOWN
        val location = camera.location.first
        for (animatronic in player.game.playerContainer.animatronics) {
            val direction = animatronic.player.location.subtract(location).toVector()
            val rayTraceBlocks = location.world.rayTraceBlocks(location, direction, direction.length() + 5)
            if (rayTraceBlocks?.hitBlock != null) continue
            animatronic.showAuraTo(
                player.game.playerContainer.survivors,
                components.getOrDefault(RPGUComponents.ABILITY_DURATION).get(player.player).toInt())
            components.getOrDefault(RPGUComponents.ABILITY_EFFECTS).applyOn(player.player, animatronic.player)
        }
        return ActionResult.FULL_COOLDOWN
    }

}
