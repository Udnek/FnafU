package me.udnek.fnafu.component.animatronic.freddy

import me.udnek.coreu.custom.component.CustomComponent
import me.udnek.coreu.custom.component.CustomComponentType
import me.udnek.coreu.custom.equipmentslot.slot.SingleSlot
import me.udnek.coreu.custom.equipmentslot.universal.UniversalInventorySlot
import me.udnek.coreu.custom.item.CustomItem
import me.udnek.coreu.nms.Nms
import me.udnek.coreu.rpgu.component.RPGUActiveAbilityItem
import me.udnek.coreu.rpgu.component.RPGUComponents
import me.udnek.coreu.rpgu.component.ability.property.AttributeBasedProperty
import me.udnek.coreu.util.Either
import me.udnek.fnafu.FnafU
import me.udnek.fnafu.component.FnafUActiveAbility
import me.udnek.fnafu.component.FnafUComponents
import me.udnek.fnafu.effect.Effects
import me.udnek.fnafu.misc.Utils
import me.udnek.fnafu.player.FnafUPlayer
import me.udnek.fnafu.sound.Sounds
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.scheduler.BukkitRunnable


open class FreddyShadowAbility : FnafUActiveAbility {

    companion object {
        val DEFAULT = FreddyShadowAbility()
    }

    constructor(){
        components.set(AttributeBasedProperty(5.0*20, RPGUComponents.ABILITY_COOLDOWN_TIME))
        components.set(AttributeBasedProperty(5.0*20, RPGUComponents.ABILITY_DURATION))
    }

    override fun action(
        item: CustomItem,
        player: FnafUPlayer,
        slot: Either<UniversalInventorySlot?, SingleSlot?>,
        event: PlayerInteractEvent
    ): ActionResult {
        val duration = components.get(RPGUComponents.ABILITY_DURATION)!!.get(player.player).toInt()
        val mask = player.player.inventory.helmet
        Effects.DISARM.applyInvisible(player.player, duration, 0)
        player.player.addPotionEffects(listOf(
            PotionEffect(PotionEffectType.INVISIBILITY, duration, 0, false, false, false),
            PotionEffect(PotionEffectType.SPEED, duration, 0, false, false, false),
            PotionEffect(PotionEffectType.NIGHT_VISION, duration, 0, false, false, false)
        ))
        player.game.playerContainer.survivors.forEach {
            Nms.get().sendFakeEquipment(player.player, it.player, EquipmentSlot.HEAD, null)
        }
        object : BukkitRunnable(){
            override fun run() {
                player.game.playerContainer.survivors.forEach {
                    Nms.get().sendFakeEquipment(player.player, it.player, EquipmentSlot.HEAD, mask)
                }
                Utils.freddySpawnDustParticle(player.player.location)
            }
        }.runTaskLater(FnafU.instance, duration.toLong())
        Utils.freddySpawnDustParticle(player.player.location)
        Sounds.FREDDY_LAUGH.play(player.player.location)
        return ActionResult.FULL_COOLDOWN
    }



    override fun getType(): CustomComponentType<in RPGUActiveAbilityItem, out CustomComponent<in RPGUActiveAbilityItem>?> {
        return FnafUComponents.FREDDY_SHADOW_ABILITY
    }
}





