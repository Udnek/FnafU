package me.udnek.fnafu.util

import me.udnek.fnafu.player.FnafUPlayer
import me.udnek.itemscoreu.customminigame.MGUManager
import org.bukkit.entity.Player


fun Player.getFnafU(): FnafUPlayer? {
    return MGUManager.get().getPlayer(this) as? FnafUPlayer
}
