package me.udnek.fnafu.map.location

import me.udnek.fnafu.util.Originable
import org.bukkit.Location

interface LocationData : Originable {
    val all: List<Location>
    val first: Location
    val random: Location
    val size: Int

    fun get(n: Int): Location
    fun center(): LocationData
    fun centerFloor(): LocationData
}
