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
import me.udnek.fnafu.item.Items.CUT_CAMERA_TABLET
import me.udnek.fnafu.item.Items.DOOR_TABLET
import me.udnek.fnafu.item.Items.FULL_CAMERA_TABLET
import me.udnek.fnafu.item.Items.SPRINGTRAP_BREAK_CAMERAS
import me.udnek.fnafu.item.Items.SPRINGTRAP_MASK
import me.udnek.fnafu.item.Items.SPRINGTRAP_PLUSHTRAP_ABILITY
import me.udnek.fnafu.item.Items.SYSTEM_TABLET
import me.udnek.fnafu.player.FnafUPlayer
import org.bukkit.inventory.ItemStack

interface Kit : CustomComponent<MGUPlayerDataHolder>, Registrable{

    companion object {
        val REGISTRY: CustomRegistry<Kit> = CustomRegistries.addRegistry(FnafU.instance, MappedCustomRegistry("kit"))

        val SPRINGTRAP = register(AnimatronicKit("springtrap", FnafUPlayer.Type.ANIMATRONIC, SPRINGTRAP_PLUSHTRAP_ABILITY,
            listOf(SPRINGTRAP_PLUSHTRAP_ABILITY, SPRINGTRAP_BREAK_CAMERAS, SPRINGTRAP_MASK), "5eb93aaaf701c868a525a9d3dd98daed9374d2a118003bd6f3464778c361e1e8"))
        val CAMERAMAN = register(ConstructableKit("cameraman", FnafUPlayer.Type.SURVIVOR, FULL_CAMERA_TABLET, listOf(FULL_CAMERA_TABLET)))
        val DOORMAN = register(ConstructableKit("doorman", FnafUPlayer.Type.SURVIVOR, DOOR_TABLET, listOf(CUT_CAMERA_TABLET, DOOR_TABLET)))
        val SYSTEMMAN = register(ConstructableKit("systemman", FnafUPlayer.Type.SURVIVOR, SYSTEM_TABLET, listOf(SYSTEM_TABLET, CUT_CAMERA_TABLET)))

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
