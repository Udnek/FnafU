package me.udnek.fnafu.map.location

import me.udnek.itemscoreu.custom.minigame.Originable
import org.bukkit.Location

interface LocationData : Originable {
    val all: List<Location>
    val first: Location
    val random: Location
    val size: Int
    val center: LocationData
    val centerFloor: LocationData

    fun get(n: Int): Location
}
