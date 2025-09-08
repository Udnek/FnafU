package me.udnek.fnafu.item.survivor

import io.papermc.paper.datacomponent.item.Equippable
import me.udnek.coreu.custom.component.instance.AutoGeneratingFilesItem
import me.udnek.coreu.custom.component.instance.TranslatableThing
import me.udnek.coreu.custom.item.ConstructableCustomItem
import me.udnek.coreu.custom.item.CustomItemProperties
import me.udnek.coreu.rpgu.component.RPGUComponents
import me.udnek.fnafu.component.survivor.FlashlightAbility
import org.bukkit.inventory.EquipmentSlot

class Flashlight : ConstructableCustomItem() {

    override fun getRawId(): String = "flashlight"
    override fun initializeComponents() {
        super.initializeComponents()
        components.getOrCreateDefault(RPGUComponents.TOGGLE_ABILITY_ITEM).components.set(FlashlightAbility.DEFAULT)
    }

    override fun getEquippable(): CustomItemProperties.DataSupplier<Equippable> {
        return CustomItemProperties.DataSupplier.of(Equippable.equippable(EquipmentSlot.OFF_HAND).build())
    }

    override fun getTranslations(): TranslatableThing {
        return TranslatableThing.ofEngAndRu("Flashlight", "Фонарик")
    }
}