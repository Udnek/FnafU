package me.udnek.fnafu.mechanic.system

import me.udnek.fnafu.game.FnafUGame
import me.udnek.fnafu.player.FnafUPlayer
import me.udnek.fnafu.util.Resettable
import me.udnek.fnafu.util.Ticking
import net.kyori.adventure.text.Component

interface System : Resettable, Ticking {

    val isRepairing: Boolean
    val isBroken: Boolean
    var guiSlot: Int
    var game: FnafUGame

    fun destroy(systemMenu: SystemMenu)

    fun startRepairing(player: FnafUPlayer, systemMenu: SystemMenu, setRepairIcon: Boolean = true)

    fun getSidebarView(): Pair<Int, Component>
}