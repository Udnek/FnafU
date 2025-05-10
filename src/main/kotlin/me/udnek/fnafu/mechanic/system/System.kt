package me.udnek.fnafu.mechanic.system

import me.udnek.fnafu.FnafU
import me.udnek.fnafu.game.EnergyGame
import me.udnek.fnafu.item.Items
import me.udnek.fnafu.player.FnafUPlayer
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable

abstract class System {

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
    open fun fix(systemMenu: SystemMenu){
        systemMenu.inventory.setItem(guiSlot, ItemStack(Material.AIR))
        systemMenu.inventory.setItem(41, ItemStack(Material.AIR))
        isBroken = false
        isRepairing = false
        updateSidebar()
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

    fun updateSidebar(){
        val (position, component) = getSidebarView()
        game.scoreboard.setLine(position, component)
        game.scoreboard.updateForAll()
    }

    fun getSidebarView(): Pair<Int, Component>{
        return Pair(sidebarPosition, if (isBroken) sidebarComponent.append(Component.text("\u26A0")).color(NamedTextColor.RED)
        else sidebarComponent.append(Component.text()).color(NamedTextColor.GREEN))
    }
}