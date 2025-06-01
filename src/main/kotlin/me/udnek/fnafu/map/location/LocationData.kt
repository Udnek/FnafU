package me.udnek.fnafu.map.location

import me.udnek.coreu.mgu.Originable
import org.bukkit.Location

interface LocationData : Originable {
    val all: List<Location>
    val first: Location
    val random: Location
    val size: Int
    val center: LocationData
    val centerFloor: LocationData
    val head: LocationData

    fun get(n: Int): Location
    fun getNearest(location: Location): Location
    fun getFarthest(location: Location): Location
}
