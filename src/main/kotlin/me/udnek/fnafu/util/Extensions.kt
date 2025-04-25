package me.udnek.fnafu.util

import me.udnek.fnafu.item.CameraButton
import me.udnek.fnafu.player.FnafUPlayer
import me.udnek.itemscoreu.custom.minigame.MGUManager
import me.udnek.itemscoreu.custom.minigame.Originable
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