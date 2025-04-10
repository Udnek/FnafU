package me.udnek.fnafu.component

import me.udnek.fnafu.item.Items
import me.udnek.fnafu.player.FnafUPlayer
import me.udnek.itemscoreu.customcomponent.CustomComponent
import me.udnek.itemscoreu.customcomponent.CustomComponentType
import me.udnek.itemscoreu.customminigame.player.MGUPlayer

interface Kit : CustomComponent<MGUPlayer>{

    companion object {
        val EMPTY = ConstructableKit()

        val SPRINGTRAP = ConstructableKit(Items.PLUSHTRAP)
        val CAMERAMAN = ConstructableKit(Items.CAMERA_TABLET)
    }


    fun setUp(player: FnafUPlayer)

    fun regive(player: FnafUPlayer)

    override fun getType(): CustomComponentType<out MGUPlayer, out CustomComponent<MGUPlayer>> {
        return Components.KIT
    }
}
