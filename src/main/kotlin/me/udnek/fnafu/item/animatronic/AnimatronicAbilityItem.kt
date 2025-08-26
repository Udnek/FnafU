package me.udnek.fnafu.item.animatronic

import me.udnek.coreu.custom.component.CustomComponentType
import me.udnek.coreu.custom.item.ConstructableCustomItem
import me.udnek.coreu.custom.item.CustomItemProperties
import me.udnek.coreu.rpgu.component.RPGUActiveAbilityItem
import me.udnek.coreu.rpgu.component.RPGUComponents
import me.udnek.coreu.rpgu.component.ability.active.RPGUItemActiveAbility
import me.udnek.fnafu.FnafU
import me.udnek.fnafu.component.AbilityIconFilesComponent
import me.udnek.fnafu.component.kit.Kit
import net.kyori.adventure.key.Key
import org.bukkit.NamespacedKey

class AnimatronicAbilityItem(
    private val animatronicName: String,
    private val skillName: String,
    private val kit: Kit,
    private val component: CustomComponentType<RPGUActiveAbilityItem, out RPGUItemActiveAbility<*>>
) : ConstructableCustomItem() {
    override fun initializeComponents() {
        super.initializeComponents()
        components.set(AbilityIconFilesComponent(kit))
        components.getOrCreateDefault(RPGUComponents.ACTIVE_ABILITY_ITEM).components.set(component.createNewDefault())
    }

    override fun getItemModel(): CustomItemProperties.DataSupplier<Key> =
        CustomItemProperties.DataSupplier.of(NamespacedKey(FnafU.instance, "animatronic/$animatronicName/ability/$skillName"))

    override fun getRawId(): String = animatronicName + "_" + skillName
}
//TODO доделать  + перенести всех аниматроников
