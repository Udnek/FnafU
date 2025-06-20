package me.udnek.fnafu.util

import me.udnek.coreu.custom.item.CustomItem
import me.udnek.coreu.mgu.MGUManager
import me.udnek.coreu.mgu.Originable
import me.udnek.fnafu.item.survivor.camera.CameraButton
import me.udnek.fnafu.player.FnafUPlayer
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import java.util.ArrayList
import java.util.Collections


fun Player.getFnafU(): FnafUPlayer? {
    return MGUManager.get().getPlayer(this) as? FnafUPlayer
}

fun Location.setOrigin(origin: Location): Location {
    return Originable.setOrigin(this, origin)
}

fun ItemStack.getCameraId(): String? {
    return CameraButton.getCameraId(this)
}

fun ItemStack.getCustom(): CustomItem? {
    return CustomItem.get(this)
}

fun Location.toCenterFloor(): Location {
    val center = this.toCenterLocation()
    center.set(center.x, center.blockY.toDouble(), center.z)
    return center
}

fun List<Location>.getNearest(location: Location): Location {
    val distances = ArrayList<Double>()
    for (loc in this) distances.add(loc.distance(location))
    return this[distances.indexOf(Collections.min(distances))]
}

fun List<Location>.getFarthest(location: Location): Location {
    val distances = ArrayList<Double>()
    for (loc in this) distances.add(loc.distance(location))
    return this[distances.indexOf(Collections.max(distances))]
}