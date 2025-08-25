package me.udnek.fnafu.item.animatronic

/*
class AnimatronicAbilityItem(
    private val animatronicName: String,
    private val skillName: String,
    private val kit: Kit,
    private val component: CustomComponentType<RPGUActiveAbilityItem, RPGUItemActiveAbility<*>>
) : ConstructableCustomItem() {
    override fun initializeComponents() {
        super.initializeComponents()
        components.set(AbilityIconFilesComponent(kit))
        components.getOrCreateDefault(RPGUComponents.ACTIVE_ABILITY_ITEM).components.set(component.createNewDefault())
    }

    override fun getItemModel(): CustomItemProperties.DataSupplier<Key> =
        CustomItemProperties.DataSupplier.of(NamespacedKey(FnafU.instance, "animatronic/$animatronicName/ability/$skillName"))

    override fun getRawId(): String = animatronicName + "_" + skillName
}*/ //TODO доделать  + перенести всех аниматроников
