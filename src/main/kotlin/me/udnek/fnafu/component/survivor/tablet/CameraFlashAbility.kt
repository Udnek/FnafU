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
import me.udnek.fnafu.player.FnafUPlayer
import org.bukkit.FluidCollisionMode
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
        components.set(AttributeBasedProperty(8.0 * 20, RPGUComponents.ABILITY_DURATION))
        components.set(EffectsProperty(
            EffectsProperty.PotionData(PotionEffectType.SLOWNESS, 5 * 20, 1)
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

        fun hasVision(animatronic: FnafUPlayer): Boolean{
            val direction = animatronic.player.location.add(0.0, 0.5, 0.0).subtract(location).toVector()
            val rayTrace =
                location.world.rayTraceBlocks(location, direction, direction.length(), FluidCollisionMode.NEVER, true) ?: return true
            if (rayTrace.hitBlock == null) return true
            return rayTrace.hitPosition.distance(location.toVector()) > direction.length()
        }

        for (animatronic in player.game.playerContainer.animatronics) {
            if (!hasVision(animatronic)) continue
            animatronic.showAuraTo(
                player.game.playerContainer.survivors,
                components.getOrDefault(RPGUComponents.ABILITY_DURATION).get(player.player).toInt())
            components.getOrDefault(RPGUComponents.ABILITY_EFFECTS).applyOn(player.player, animatronic.player)
        }
        return ActionResult.FULL_COOLDOWN
    }

}
