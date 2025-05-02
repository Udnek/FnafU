package me.udnek.fnafu.mechanic

import me.udnek.fnafu.mechanic.system.System
import me.udnek.fnafu.util.Resettable
import me.udnek.itemscoreu.custom.minigame.Originable
import org.bukkit.Location

class AudioSystem : Resettable, Originable, System(16) {



    override fun reset() {}

    override fun setOrigin(p0: Location) {}
}