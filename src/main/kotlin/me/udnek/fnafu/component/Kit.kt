package me.udnek.fnafu.component

import me.udnek.fnafu.FnafU
import me.udnek.fnafu.item.Items
import me.udnek.fnafu.player.FnafUPlayer
import me.udnek.itemscoreu.custom.minigame.player.MGUPlayer
import me.udnek.itemscoreu.customcomponent.CustomComponent
import me.udnek.itemscoreu.customcomponent.CustomComponentType
import me.udnek.itemscoreu.customregistry.CustomRegistries
import me.udnek.itemscoreu.customregistry.CustomRegistry
import me.udnek.itemscoreu.customregistry.MappedCustomRegistry
import me.udnek.itemscoreu.customregistry.Registrable

interface Kit : CustomComponent<MGUPlayer>, Registrable{

    companion object {
        val REGISTRY: CustomRegistry<ConstructableKit> = CustomRegistries.addRegistry(FnafU.instance, MappedCustomRegistry("kit"))

        val SPRINGTRAP = register(ConstructableKit("springtrap", FnafUPlayer.Type.ANIMATRONIC, Items.PLUSHTRAP.item, listOf(Items.PLUSHTRAP, Items.BREAK_CAMERAS)))
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
        return Components.KIT
    }
}
