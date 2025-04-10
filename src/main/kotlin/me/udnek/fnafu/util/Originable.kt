package me.udnek.fnafu.util

import org.bukkit.Location


fun Location.setOrigin(origin: Location): Location {
    this.world = origin.world
    return add(origin.x, origin.y, origin.z)
}

interface Originable {
    fun setOrigin(origin: Location)
}
