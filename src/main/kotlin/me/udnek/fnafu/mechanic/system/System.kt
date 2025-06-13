package me.udnek.fnafu.mechanic.system

import me.udnek.fnafu.FnafU
import me.udnek.fnafu.game.EnergyGame
import me.udnek.fnafu.item.Items
import me.udnek.fnafu.player.FnafUPlayer
import me.udnek.fnafu.util.Resettable
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable

abstract class System : Resettable {
    
    companion object {
        const val REBOOT_ALL_ICON_POSITION = 41
    }

    abstract val game: EnergyGame
    abstract val sidebarPosition: Int
    private var isBroken = false
    private var isRepairing = false
    private val fixTime = 20 * 7
    private var guiSlot: Int
    private var sidebarComponent: Component

    constructor(guiSlot: Int, systemKey: String){
        this.guiSlot = guiSlot
        this.sidebarComponent = Component.translatable(systemKey)
    }

    open fun destroy(systemMenu: SystemMenu){
        systemMenu.inventory.setItem(guiSlot, Items.ERROR_ICON.item)
        isBroken = true
        updateSidebar()
    }
    open fun repaired(systemMenu: SystemMenu/*, isFakeUse: Boolean*/){
        systemMenu.inventory.setItem(guiSlot, ItemStack(Material.AIR))
        systemMenu.inventory.setItem(REBOOT_ALL_ICON_POSITION, ItemStack(Material.AIR))
        /*if (!isFakeUse)*/ isBroken = false
        isRepairing = false
        updateSidebar()
    }
    open fun failedRepairing(systemMenu: SystemMenu/*, isFakeUse: Boolean*/){
        isRepairing = false
        systemMenu.inventory.setItem(REBOOT_ALL_ICON_POSITION, ItemStack(Material.AIR))
        if (isBroken()) systemMenu.inventory.setItem(guiSlot, Items.ERROR_ICON.item)
    }

    fun isBroken(): Boolean {return isBroken}
    fun setIsRepairing(isRepairing: Boolean) {this.isRepairing = isRepairing}
    fun getIsRepairing(): Boolean {return isRepairing}

    open fun startRepairing(player: FnafUPlayer, systemMenu: SystemMenu){
        if (isRepairing) return
        systemMenu.inventory.setItem(guiSlot, Items.REBOOT_ICON.item)

        repairingTask(player, systemMenu/*, !isBroken*/)
    }

    open fun repairingTask(player: FnafUPlayer, systemMenu: SystemMenu/*, isFakeUse: Boolean*/){
        isRepairing = true
        object : BukkitRunnable(){
            var time = 0
            override fun run() {
                if (!systemMenu.isOpenedByAnyone){
                    failedRepairing(systemMenu/*, isFakeUse*/)
                    cancel()
                    return
                }
                time += 10
                if (time >= fixTime){
                    repaired(systemMenu/*, isFakeUse*/)
                    cancel()
                    return
                }
            }
        }.runTaskTimer(FnafU.instance, 0, 10)
    }

    fun updateSidebar(){
        val (position, component) = getSidebarView()
        game.scoreboard.setLine(position, component)
        game.scoreboard.updateForAll()
    }

    fun getSidebarView(): Pair<Int, Component>{
        return Pair(sidebarPosition, if (isBroken) sidebarComponent.append(Component.text(" \u26A0")).color(NamedTextColor.RED)
        else sidebarComponent.append(Component.text()).color(NamedTextColor.GREEN))
    }
}