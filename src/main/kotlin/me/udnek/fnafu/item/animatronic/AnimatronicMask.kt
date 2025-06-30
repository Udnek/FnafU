package me.udnek.fnafu.item.animatronic

import io.papermc.paper.datacomponent.item.Equippable
import io.papermc.paper.datacomponent.item.ItemAttributeModifiers
import me.udnek.coreu.custom.item.ConstructableCustomItem
import me.udnek.coreu.custom.item.CustomItemProperties
import me.udnek.fnafu.FnafU
import net.kyori.adventure.key.Key
import org.bukkit.NamespacedKey
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.inventory.EquipmentSlot

class AnimatronicMask(val animatronicName: String, val speed: Double): ConstructableCustomItem() {
    override fun getRawId() = animatronicName + "_mask"
    override fun getItemModel(): CustomItemProperties.DataSupplier<Key> =
        CustomItemProperties.DataSupplier.of(NamespacedKey(FnafU.instance, "animatronic/$animatronicName/mask"))

    override fun getEquippable(): CustomItemProperties.DataSupplier<Equippable> {
        return CustomItemProperties.DataSupplier.of(Equippable.equippable(EquipmentSlot.HEAD).build())
    }

    override fun getAttributeModifiers(): CustomItemProperties.DataSupplier<ItemAttributeModifiers?>? {
        return CustomItemProperties.DataSupplier.of(ItemAttributeModifiers.itemAttributes()
            .addModifier(Attribute.MOVEMENT_SPEED, AttributeModifier(NamespacedKey(FnafU.instance, "mask_ms"),
                speed, AttributeModifier.Operation.ADD_SCALAR))
            .build())
    }
}