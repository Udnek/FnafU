package me.udnek.fnafu.item.survivor

import me.udnek.coreu.custom.item.ConstructableCustomItem
import me.udnek.coreu.custom.item.CustomItemProperties
import me.udnek.coreu.rpgu.component.RPGUComponents
import me.udnek.fnafu.FnafU
import me.udnek.fnafu.component.survivor.FlashlightChargeAbility
import me.udnek.fnafu.component.survivor.PetrolCanisterAbility
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.Damageable
import org.bukkit.persistence.PersistentDataType

class PetrolCanister(val canisterAbility: PetrolCanisterAbility, val rawID: String) : ConstructableCustomItem() {

    companion object {
        val PDC_KEY: NamespacedKey = NamespacedKey(FnafU.instance, "petrol")

        fun setPetrol(item: ItemStack, amount: Float){
            item.editMeta {
                val damageable = it as Damageable
                damageable.damage = damageable.maxDamage - amount.toInt()
            }
            item.editPersistentDataContainer { container -> container.set(PDC_KEY, PersistentDataType.FLOAT, amount) }
        }
        fun getPetrol(item: ItemStack): Float {
            return item.persistentDataContainer.get(PDC_KEY, PersistentDataType.FLOAT) ?: 0f
        }
    }

    override fun getRawId(): String = rawID

    override fun getMaxStackSize(): CustomItemProperties.DataSupplier<Int?> {
        return CustomItemProperties.DataSupplier.of(1)
    }

    override fun getMaxDamage(): CustomItemProperties.DataSupplier<Int?> {
        return CustomItemProperties.DataSupplier.of(canisterAbility.maxCapacity)
    }

    override fun modifyFinalItemStack(itemStack: ItemStack) {
        super.modifyFinalItemStack(itemStack)
        itemStack.editMeta { (it as Damageable).damage = 0 }
    }

    override fun initializeComponents() {
        super.initializeComponents()
        components.getOrCreateDefault(RPGUComponents.ACTIVE_ABILITY_ITEM).components.set(canisterAbility)
    }
}
