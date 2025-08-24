package me.udnek.fnafu.mechanic.system

import me.udnek.coreu.mgu.Resettable
import me.udnek.fnafu.game.FnafUGame
import me.udnek.fnafu.misc.Ticking
import me.udnek.fnafu.player.FnafUPlayer
import net.kyori.adventure.text.Component

interface System : Resettable, Ticking {

    val isRepairing: Boolean
    val isBroken: Boolean
    var repairIconSlot: Int
    var game: FnafUGame
    var durability: Float

    fun destroy()

    fun startRepairing(player: FnafUPlayer, systemsMenu: SystemsMenu, repairDuration: Int, setRepairIcon: Boolean = true, playSound: Boolean = true)

    fun getSidebarLine(): Pair<Int, Component>
}