package me.udnek.fnafu.item.survivor

import me.udnek.coreu.custom.item.ConstructableCustomItem
import me.udnek.coreu.rpgu.component.RPGUComponents
import me.udnek.fnafu.FnafU
import me.udnek.fnafu.component.survivor.PetrolCanisterAbility
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

class PetrolCanister(val component: PetrolCanisterAbility, val rawID: String) : ConstructableCustomItem() {

    companion object {
        val PDC_KEY: NamespacedKey = NamespacedKey(FnafU.instance, "petrol")

        fun setPetrol(item: ItemStack, amount: Float){
            item.editPersistentDataContainer { container -> container.set(PDC_KEY, PersistentDataType.FLOAT, amount) }
        }
        fun getPetrol(item: ItemStack): Float {
            return item.persistentDataContainer.get(PDC_KEY, PersistentDataType.FLOAT) ?: 0f
        }
    }

    override fun getRawId(): String = rawID

    override fun initializeComponents() {
        super.initializeComponents()
        components.getOrCreateDefault(RPGUComponents.ACTIVE_ABILITY_ITEM).components.set(component)
    }
}
