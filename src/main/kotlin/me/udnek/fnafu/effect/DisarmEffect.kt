package me.udnek.fnafu.effect

import me.udnek.coreu.custom.component.instance.TranslatableThing
import me.udnek.coreu.custom.effect.ConstructableCustomEffect
import me.udnek.fnafu.FnafU
import org.bukkit.NamespacedKey
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.potion.PotionEffectType
import org.bukkit.potion.PotionEffectTypeCategory

class DisarmEffect : ConstructableCustomEffect{

    constructor(){
        components.set(TranslatableThing.ofEngAndRu("Disarm", "Безсилие"))
    }

    override fun getCategory(): PotionEffectTypeCategory = PotionEffectTypeCategory.HARMFUL
    override fun getVanillaDisguise(): PotionEffectType = PotionEffectType.WEAKNESS
    override fun getRawId(): String = "disarm"

    override fun addAttributes(consumer: AttributeConsumer) {
        consumer.accept(Attribute.ENTITY_INTERACTION_RANGE, NamespacedKey(FnafU.instance, "entity_interaction_range_" + getRawId()), -100.0, AttributeModifier.Operation.ADD_NUMBER)
        consumer.accept(Attribute.ATTACK_SPEED, NamespacedKey(FnafU.instance, "attack_speed_" + getRawId()), -100.0, AttributeModifier.Operation.ADD_NUMBER)
    }
}
