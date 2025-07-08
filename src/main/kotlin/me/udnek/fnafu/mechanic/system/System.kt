package me.udnek.fnafu.mechanic.system

import me.udnek.coreu.mgu.Resettable
import me.udnek.fnafu.game.FnafUGame
import me.udnek.fnafu.player.FnafUPlayer
import me.udnek.fnafu.misc.Ticking
import net.kyori.adventure.text.Component

interface System : Resettable, Ticking {

    val isRepairing: Boolean
    val isBroken: Boolean
    var guiSlot: Int
    var game: FnafUGame
    var durability: Float

    fun destroy()

    fun startRepairing(player: FnafUPlayer, systemMenu: SystemMenu, repairDuration: Int, setRepairIcon: Boolean = true)

    fun getSidebarLine(): Pair<Int, Component>
}