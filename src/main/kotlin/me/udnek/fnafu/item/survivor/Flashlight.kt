package me.udnek.fnafu.item.survivor

import io.papermc.paper.datacomponent.item.Equippable
import me.udnek.coreu.custom.component.instance.TranslatableThing
import me.udnek.coreu.custom.item.ConstructableCustomItem
import me.udnek.coreu.custom.item.CustomItemProperties
import me.udnek.coreu.rpgu.component.RPGUComponents
import me.udnek.fnafu.FnafU
import me.udnek.fnafu.component.survivor.FlashlightBeamAbility
import me.udnek.fnafu.component.survivor.FlashlightChargeAbility
import org.bukkit.NamespacedKey
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.Damageable
import org.bukkit.persistence.PersistentDataType

class Flashlight : ConstructableCustomItem() {


    companion object {
        val PDC_KEY: NamespacedKey = NamespacedKey(FnafU.instance, "charge")

        fun setCharge(item: ItemStack, amount: Float){
            item.editMeta {
                (it as Damageable).damage = (FlashlightChargeAbility.MAX_CHARGE - amount).toInt()
            }
            item.editPersistentDataContainer { container -> container.set(PDC_KEY, PersistentDataType.FLOAT, amount) }
        }
        fun getCharge(item: ItemStack): Float {
            return item.persistentDataContainer.get(PDC_KEY, PersistentDataType.FLOAT) ?: FlashlightChargeAbility.MAX_CHARGE
        }
    }

    override fun getMaxDamage(): CustomItemProperties.DataSupplier<Int?> {
        return CustomItemProperties.DataSupplier.of(FlashlightChargeAbility.MAX_CHARGE.toInt())
    }

    override fun getMaxStackSize(): CustomItemProperties.DataSupplier<Int?> {
        return CustomItemProperties.DataSupplier.of(1)
    }

    override fun getRawId(): String = "flashlight"

    override fun initializeComponents() {
        super.initializeComponents()
        components.getOrCreateDefault(RPGUComponents.TOGGLE_ABILITY_ITEM).components.set(FlashlightBeamAbility.DEFAULT)
        components.getOrCreateDefault(RPGUComponents.ACTIVE_ABILITY_ITEM).components.set(FlashlightChargeAbility.DEFAULT)
    }

    override fun getEquippable(): CustomItemProperties.DataSupplier<Equippable> {
        return CustomItemProperties.DataSupplier.of(Equippable.equippable(EquipmentSlot.OFF_HAND).build())
    }

    override fun getTranslations(): TranslatableThing {
        return TranslatableThing.ofEngAndRu("Flashlight", "Фонарик")
    }
}