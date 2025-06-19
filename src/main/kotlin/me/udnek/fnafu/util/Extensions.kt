package me.udnek.fnafu.util

import me.udnek.coreu.custom.item.CustomItem
import me.udnek.coreu.mgu.MGUManager
import me.udnek.coreu.mgu.Originable
import me.udnek.fnafu.item.survivor.CameraButton
import me.udnek.fnafu.player.FnafUPlayer
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack


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