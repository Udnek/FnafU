package me.udnek.fnafu.component

import me.udnek.fnafu.item.Items
import me.udnek.fnafu.player.FnafUPlayer
import me.udnek.itemscoreu.custom.minigame.player.MGUPlayer
import me.udnek.itemscoreu.customcomponent.CustomComponent
import me.udnek.itemscoreu.customcomponent.CustomComponentType

interface Kit : CustomComponent<MGUPlayer>{

    /*companion object {
        val EMPTY = ConstructableKit(ItemStack(Material.AIR))

        val SPRINGTRAP = ConstructableKit(Items.PLUSHTRAP.item)
        val CAMERAMAN = ConstructableKit(FullCameraTablet.getWithColor())
        val DOORMAN = ConstructableKit(CutCameraTablet.getWithColor())
    }*/

    companion object {
        val EMPTY = ConstructableKit()

        val SPRINGTRAP = ConstructableKit(Items.PLUSHTRAP)
        val CAMERAMAN = ConstructableKit(Items.FULL_CAMERA_TABLET)
        val DOORMAN = ConstructableKit(Items.CUT_CAMERA_TABLET)
    }


    fun setUp(player: FnafUPlayer)

    fun regive(player: FnafUPlayer)

    override fun getType(): CustomComponentType<out MGUPlayer, out CustomComponent<MGUPlayer>> {
        return Components.KIT
    }
}
