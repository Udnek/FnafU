package me.udnek.fnafu.component.animatronic.freddy

import me.udnek.coreu.custom.component.CustomComponent
import me.udnek.coreu.custom.component.CustomComponentType
import me.udnek.coreu.custom.equipmentslot.slot.CustomEquipmentSlot
import me.udnek.coreu.custom.equipmentslot.universal.UniversalInventorySlot
import me.udnek.coreu.custom.item.CustomItem
import me.udnek.coreu.nms.Nms
import me.udnek.coreu.rpgu.component.RPGUActiveItem
import me.udnek.coreu.rpgu.component.RPGUComponents
import me.udnek.coreu.rpgu.component.ability.property.AttributeBasedProperty
import me.udnek.coreu.rpgu.component.ability.property.EffectsProperty
import me.udnek.coreu.util.Either
import me.udnek.fnafu.FnafU
import me.udnek.fnafu.component.FnafUActiveAbility
import me.udnek.fnafu.component.FnafUComponents
import me.udnek.fnafu.effect.Effects
import me.udnek.fnafu.misc.Utils
import me.udnek.fnafu.misc.toApache
import me.udnek.fnafu.player.FnafUPlayer
import me.udnek.fnafu.sound.Sounds
import me.udnek.jeiu.component.Components
import org.apache.commons.lang3.tuple.Pair
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.scheduler.BukkitRunnable


open class FreddyShadowAbility : FnafUActiveAbility {

    companion object {
        val DEFAULT = FreddyShadowAbility()
        const val DURATION = 6 * 20
    }

    constructor(){
        components.set(AttributeBasedProperty(DURATION.toDouble(), RPGUComponents.ABILITY_DURATION))
        components.set(AttributeBasedProperty(10.0*20, RPGUComponents.ABILITY_COOLDOWN_TIME))
        components.set(EffectsProperty(
            EffectsProperty.PotionData(PotionEffect(PotionEffectType.INVISIBILITY, DURATION, 0, false, false, false)),
            EffectsProperty.PotionData(PotionEffect(PotionEffectType.SPEED, DURATION, 0, false, false, false)),
            EffectsProperty.PotionData(PotionEffect(Effects.DISARM.bukkitType, DURATION, 0, false, false, false))
        ))
    }

    override fun getEngAndRuDescription(): Pair<List<String?>?, List<String?>?> {
        return (listOf("Becomes invisible, but unable to attack") to listOf("Становитесь невидимым, но не можете атаковать")).toApache()
    }

    override fun action(
        item: CustomItem,
        player: FnafUPlayer,
        slot: UniversalInventorySlot,
        event: PlayerInteractEvent
    ): ActionResult {
        components.getOrException(RPGUComponents.ABILITY_EFFECTS).applyOn(player.player, player.player)
        val duration = components.get(RPGUComponents.ABILITY_DURATION)!!.get(player.player).toInt()
        val mask = player.player.inventory.helmet
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



    override fun getType(): CustomComponentType<in RPGUActiveItem, out CustomComponent<in RPGUActiveItem>?> {
        return FnafUComponents.FREDDY_SHADOW_ABILITY
    }
}





