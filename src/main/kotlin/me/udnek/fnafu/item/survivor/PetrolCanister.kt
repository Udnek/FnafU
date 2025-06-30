package me.udnek.fnafu.item.survivor

import io.papermc.paper.datacomponent.item.Consumable
import io.papermc.paper.datacomponent.item.consumable.ItemUseAnimation
import io.papermc.paper.registry.keys.SoundEventKeys
import me.udnek.coreu.custom.item.ConstructableCustomItem
import me.udnek.coreu.custom.item.CustomItemProperties

class PetrolCanister : ConstructableCustomItem() {
    override fun getRawId(): String = "petrol_canister"

    override fun getConsumable(): CustomItemProperties.DataSupplier<Consumable?> {
        return CustomItemProperties.DataSupplier.of(Consumable.consumable()
                .consumeSeconds(1.0E10f).sound(SoundEventKeys.INTENTIONALLY_EMPTY).animation(ItemUseAnimation.BOW).build()
        )
    }
}
