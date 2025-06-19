package me.udnek.fnafu.component.kit

import me.udnek.coreu.custom.component.CustomComponent
import me.udnek.coreu.custom.component.CustomComponentType
import me.udnek.coreu.custom.registry.CustomRegistries
import me.udnek.coreu.custom.registry.CustomRegistry
import me.udnek.coreu.custom.registry.MappedCustomRegistry
import me.udnek.coreu.custom.registry.Registrable
import me.udnek.coreu.mgu.player.MGUPlayer
import me.udnek.fnafu.FnafU
import me.udnek.fnafu.component.FnafUComponents
import me.udnek.fnafu.item.Items
import me.udnek.fnafu.player.FnafUPlayer

interface Kit : CustomComponent<MGUPlayer>, Registrable{

    companion object {
        val REGISTRY: CustomRegistry<ConstructableKit> = CustomRegistries.addRegistry(FnafU.instance, MappedCustomRegistry("kit"))

        val SPRINGTRAP = register(ConstructableKit("springtrap", FnafUPlayer.Type.ANIMATRONIC, Items.SPRINGTRAP_PLUSHTRAP_ABILITY.item, listOf(Items.SPRINGTRAP_PLUSHTRAP_ABILITY, Items.SPRINGTRAP_BREAK_CAMERAS)))
        val CAMERAMAN = register(ConstructableKit("cameraman", FnafUPlayer.Type.SURVIVOR, Items.FULL_CAMERA_TABLET.item,listOf(Items.FULL_CAMERA_TABLET)))
        val DOORMAN = register(ConstructableKit("doorman", FnafUPlayer.Type.SURVIVOR, Items.CAMERA_OVERLAY.item, listOf(Items.CUT_CAMERA_TABLET)))
        val SYSTEMMAN = register(ConstructableKit("systemman", FnafUPlayer.Type.SURVIVOR, Items.SYSTEM_TABLET.item, listOf(Items.SYSTEM_TABLET, Items.CUT_CAMERA_TABLET)))

        fun register(kit: ConstructableKit): ConstructableKit {
            return REGISTRY.register(FnafU.instance, kit)
        }
    }

    fun setUp(player: FnafUPlayer)

    fun regive(player: FnafUPlayer)



    override fun getType(): CustomComponentType<out MGUPlayer, out CustomComponent<MGUPlayer>> {
        return FnafUComponents.KIT
    }
}
