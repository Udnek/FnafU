package me.udnek.fnafu.mechanic

import me.udnek.fnafu.game.EnergyGame
import me.udnek.fnafu.mechanic.system.System
import me.udnek.fnafu.util.Resettable

class VentilationSystem : Resettable, System {
    override val game: EnergyGame
    override val sidebarPosition: Int = 1

    constructor(game: EnergyGame) : super(32, "sidebar.fnafu.ventilation_system") {
        this.game = game
    }


    override fun reset() {}
}