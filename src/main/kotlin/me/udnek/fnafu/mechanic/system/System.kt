package me.udnek.fnafu.mechanic.system

import me.udnek.fnafu.FnafU
import me.udnek.fnafu.item.Items
import me.udnek.fnafu.player.FnafUPlayer
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable

abstract class System {

    private var isBroken = false
    private var isRepairing = false
    private val fixTime = 20 * 7
    private var guiSlot: Int

    constructor(guiSlot: Int){
        this.guiSlot = guiSlot
    }

    open fun destroy(systemMenu: SystemMenu){
        systemMenu.inventory.setItem(guiSlot, Items.ERROR_ICON.item)
        isBroken = true
    }
    open fun fix(systemMenu: SystemMenu){
        systemMenu.inventory.setItem(guiSlot, ItemStack(Material.AIR))
        systemMenu.inventory.setItem(41, ItemStack(Material.AIR))
        isBroken = false
        isRepairing = false
    }
    open fun failedFix(systemMenu: SystemMenu){
        isRepairing = false
        systemMenu.inventory.setItem(guiSlot, Items.ERROR_ICON.item)
    }

    fun isBroken(): Boolean {return isBroken}
    fun isRepairing(): Boolean {return isRepairing}

    open fun startFix(player: FnafUPlayer, systemMenu: SystemMenu){
        if (!isBroken or isRepairing) return
        isRepairing = true
        systemMenu.inventory.setItem(guiSlot, Items.REBOOT_ICON.item)

        repairingTask(player, systemMenu)
    }

    open fun repairingTask(player: FnafUPlayer, systemMenu: SystemMenu){
        object : BukkitRunnable(){
            var tyme = 0
            override fun run() {
                if (!systemMenu.isOpened(player.player)){
                    failedFix(systemMenu)
                    cancel()
                    return
                }
                tyme += 10
                if (tyme >= fixTime){
                    fix(systemMenu)
                    cancel()
                    return
                }
            }
        }.runTaskTimer(FnafU.instance, 0, 10)
    }
}