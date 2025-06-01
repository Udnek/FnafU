package me.udnek.fnafu.util

import me.udnek.fnafu.player.FnafUPlayer
import org.bukkit.inventory.ItemStack

abstract class AbilityCooldown {
    companion object {
        const val INFINITE_COOLDOWN: Int = 999999 * 20
    }

    val baseCooldown: Int
    lateinit var player: FnafUPlayer
    lateinit var item: ItemStack
    var isActivated = false

    constructor(baseCooldown: Int) {
        this.baseCooldown = baseCooldown
    }

    fun start(player: FnafUPlayer, item: ItemStack): Boolean {
        this.player = player
        this.item = item
        if (!isActivated) return false
        setInfiniteCooldown()
        isActivated = true
        return true
    }

    fun setInfiniteCooldown() = setCooldown(INFINITE_COOLDOWN)
    fun setBaseCooldown() = setCooldown(baseCooldown)
    fun setCooldown(cooldown: Int) {
        if (this::player.isInitialized || this::item.isInitialized) player.player.setCooldown(item, cooldown)
    }
}