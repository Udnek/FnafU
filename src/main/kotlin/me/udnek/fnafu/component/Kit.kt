package me.udnek.fnafu.component

import me.udnek.fnafu.item.CutCameraTablet
import me.udnek.fnafu.item.FullCameraTablet
import me.udnek.fnafu.item.Items
import me.udnek.fnafu.player.FnafUPlayer
import me.udnek.itemscoreu.custom.minigame.player.MGUPlayer
import me.udnek.itemscoreu.customcomponent.CustomComponent
import me.udnek.itemscoreu.customcomponent.CustomComponentType
import java.util.function.Supplier

interface Kit : CustomComponent<MGUPlayer>{

    companion object {
        val EMPTY = ConstructableKit(listOf(), listOf())

        val SPRINGTRAP = ConstructableKit(listOf(), listOf(Items.PLUSHTRAP))
        val CAMERAMAN = ConstructableKit(listOf(Supplier{ FullCameraTablet.getWithColor() }), listOf())
        val DOORMAN = ConstructableKit(listOf(Supplier{ CutCameraTablet.getWithColor()}), listOf())
    }

    fun setUp(player: FnafUPlayer)

    fun regive(player: FnafUPlayer)

    override fun getType(): CustomComponentType<out MGUPlayer, out CustomComponent<MGUPlayer>> {
        return Components.KIT
    }
}
