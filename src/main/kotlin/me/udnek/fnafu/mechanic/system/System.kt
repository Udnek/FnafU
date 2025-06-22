package me.udnek.fnafu.mechanic.system

import me.udnek.fnafu.game.FnafUGame
import me.udnek.fnafu.player.FnafUPlayer
import me.udnek.fnafu.util.Resettable
import net.kyori.adventure.text.Component

interface System : Resettable {

    val isRepairing: Boolean
    val isBroken: Boolean
    var guiSlot: Int
    var game: FnafUGame

    fun destroy(systemMenu: SystemMenu)

    fun startRepairing(player: FnafUPlayer, systemMenu: SystemMenu)

    fun getSidebarView(): Pair<Int, Component>
}