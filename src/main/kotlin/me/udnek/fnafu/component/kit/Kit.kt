package me.udnek.fnafu.component.kit

import me.udnek.coreu.custom.component.CustomComponent
import me.udnek.coreu.custom.component.CustomComponentType
import me.udnek.coreu.custom.registry.CustomRegistries
import me.udnek.coreu.custom.registry.CustomRegistry
import me.udnek.coreu.custom.registry.MappedCustomRegistry
import me.udnek.coreu.custom.registry.Registrable
import me.udnek.coreu.mgu.ability.MGUPlayerDataHolder
import me.udnek.fnafu.FnafU
import me.udnek.fnafu.component.FnafUComponents
import me.udnek.fnafu.item.Items
import me.udnek.fnafu.player.FnafUPlayer
import org.bukkit.inventory.ItemStack

interface Kit : CustomComponent<MGUPlayerDataHolder>, Registrable{

    companion object {
        val REGISTRY: CustomRegistry<Kit> = CustomRegistries.addRegistry(FnafU.instance, MappedCustomRegistry("kit"))

        val SPRINGTRAP = register(ConstructableKit("springtrap", FnafUPlayer.Type.ANIMATRONIC, Items.SPRINGTRAP_PLUSHTRAP_ABILITY.item, listOf(Items.SPRINGTRAP_PLUSHTRAP_ABILITY, Items.SPRINGTRAP_BREAK_CAMERAS)))
        val CAMERAMAN = register(ConstructableKit("cameraman", FnafUPlayer.Type.SURVIVOR, Items.FULL_CAMERA_TABLET.item,listOf(Items.FULL_CAMERA_TABLET)))
        val DOORMAN = register(ConstructableKit("doorman", FnafUPlayer.Type.SURVIVOR, Items.DOOR_TABLET.item, listOf(Items.CUT_CAMERA_TABLET, Items.DOOR_TABLET)))
        val SYSTEMMAN = register(ConstructableKit("systemman", FnafUPlayer.Type.SURVIVOR, Items.SYSTEM_TABLET.item, listOf(Items.SYSTEM_TABLET, Items.CUT_CAMERA_TABLET)))

        fun register(kit: ConstructableKit): ConstructableKit {
            return REGISTRY.register(FnafU.instance, kit)
        }
    }


    val displayItem: ItemStack
    val items: List<ItemStack>
    val playerType: FnafUPlayer.Type

    fun setUp(player: FnafUPlayer)

    fun regive(player: FnafUPlayer)

    override fun getType(): CustomComponentType<out MGUPlayerDataHolder?, out CustomComponent<MGUPlayerDataHolder>> {
        return FnafUComponents.KIT
    }
}
