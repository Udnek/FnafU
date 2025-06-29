package me.udnek.fnafu.item.animatronic

import io.papermc.paper.datacomponent.item.Equippable
import me.udnek.coreu.custom.item.ConstructableCustomItem
import me.udnek.coreu.custom.item.CustomItemProperties
import me.udnek.fnafu.FnafU
import net.kyori.adventure.key.Key
import org.bukkit.NamespacedKey
import org.bukkit.inventory.EquipmentSlot

class AnimatronicMask(val animatronicName: String): ConstructableCustomItem() {
    override fun getRawId() = animatronicName + "_mask"
    override fun getItemModel(): CustomItemProperties.DataSupplier<Key> =
        CustomItemProperties.DataSupplier.of(NamespacedKey(FnafU.instance, "animatronic/$animatronicName/mask"))

    override fun getEquippable(): CustomItemProperties.DataSupplier<Equippable> {
        return CustomItemProperties.DataSupplier.of(Equippable.equippable(EquipmentSlot.HEAD).build())
    }
}