package me.udnek.fnafu.effect

import me.udnek.coreu.custom.effect.ConstructableCustomEffect
import me.udnek.fnafu.FnafU
import org.bukkit.NamespacedKey
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.potion.PotionEffectType
import org.bukkit.potion.PotionEffectTypeCategory

class InGameEffect : ConstructableCustomEffect(){
    override fun getCategory(): PotionEffectTypeCategory = PotionEffectTypeCategory.NEUTRAL
    override fun getVanillaDisguise(): PotionEffectType? = null
    override fun getRawId(): String = "in_game"

    override fun addAttributes(consumer: AttributeConsumer) {
        consumer.accept(Attribute.JUMP_STRENGTH, NamespacedKey(FnafU.instance, "game_js"), -10.0, AttributeModifier.Operation.ADD_NUMBER)
        consumer.accept(Attribute.MOVEMENT_SPEED, NamespacedKey(FnafU.instance, "game_ms"), -0.15, AttributeModifier.Operation.ADD_SCALAR)
        consumer.accept(Attribute.WAYPOINT_RECEIVE_RANGE, NamespacedKey(FnafU.instance, "game_rr"), -100.0, AttributeModifier.Operation.MULTIPLY_SCALAR_1)
    }
}
