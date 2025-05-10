package me.udnek.fnafu.mechanic

import me.udnek.fnafu.game.EnergyGame
import me.udnek.fnafu.mechanic.system.System
import me.udnek.fnafu.util.Resettable

class AudioSystem : Resettable, System {

    override val game: EnergyGame
    override val sidebarPosition: Int = 3

    constructor(game: EnergyGame) : super(16, "sidebar.fnafu.audio_system") {
        this.game = game
    }

    override fun reset() {}



}