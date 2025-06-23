package me.udnek.fnafu.effect

import me.udnek.coreu.custom.effect.ConstructableCustomEffect
import me.udnek.fnafu.FnafU
import org.bukkit.NamespacedKey
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.entity.LivingEntity
import org.bukkit.potion.PotionEffectType
import org.bukkit.potion.PotionEffectTypeCategory

class StunEffect : ConstructableCustomEffect(){
    override fun getCategory(): PotionEffectTypeCategory = PotionEffectTypeCategory.HARMFUL
    override fun getVanillaDisguise(): PotionEffectType? = PotionEffectType.SLOWNESS
    override fun getRawId(): String = "stun"

    override fun apply(livingEntity: LivingEntity, duration: Int, amplifier: Int, ambient: Boolean, showParticles: Boolean, showIcon: Boolean) {
        super.apply(livingEntity, duration, amplifier, ambient, showParticles, showIcon)
        //TODO if (showParticles) StunnedParticle(livingEntity, duration).play(livingEntity.getLocation())
    }

    override fun addAttributes(consumer: AttributeConsumer) {
        consumer.accept(Attribute.MOVEMENT_SPEED, NamespacedKey(FnafU.instance, "movement_speed_" + getRawId()), -100.0, AttributeModifier.Operation.ADD_NUMBER)
        consumer.accept(Attribute.JUMP_STRENGTH, NamespacedKey(FnafU.instance, "jump_strength_" + getRawId()), -100.0, AttributeModifier.Operation.ADD_NUMBER)
        consumer.accept(Attribute.BLOCK_INTERACTION_RANGE, NamespacedKey(FnafU.instance, "block_interaction_range_" + getRawId()), -100.0, AttributeModifier.Operation.ADD_NUMBER)
        consumer.accept(Attribute.ATTACK_SPEED, NamespacedKey(FnafU.instance, "attack_speed_" + getRawId()), -100.0, AttributeModifier.Operation.ADD_NUMBER)
        consumer.accept(Attribute.ENTITY_INTERACTION_RANGE, NamespacedKey(FnafU.instance, "entity_interaction_range_" + getRawId()), -100.0, AttributeModifier.Operation.ADD_NUMBER)
    }
}
