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
    return sortedByDistance(location).first()
}

fun List<Location>.sortedByDistance(location: Location): List<Location> {
    return this.sortedBy { l -> l.distance(location) }
}

fun List<Location>.getFarthest(location: Location): Location {
    return this.sortedByDistance(location).last()
}