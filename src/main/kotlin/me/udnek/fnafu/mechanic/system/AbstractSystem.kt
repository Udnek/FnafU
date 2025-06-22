package me.udnek.fnafu.mechanic.system

import me.udnek.fnafu.FnafU
import me.udnek.fnafu.event.SystemRepairedEvent
import me.udnek.fnafu.game.EnergyGame
import me.udnek.fnafu.game.FnafUGame
import me.udnek.fnafu.item.Items
import me.udnek.fnafu.player.FnafUPlayer
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable

abstract class AbstractSystem(override var game: FnafUGame) : System {

    abstract val sidebarPosition: Int
    override var isBroken = false
        protected set
    override var isRepairing = false
        protected set
    protected val fixTime = 20 * 7
    protected abstract var sidebarComponent: Component
    protected var repairTask: BukkitRunnable? = null


    override fun destroy(systemMenu: SystemMenu){
        systemMenu.inventory.setItem(guiSlot, Items.ERROR_ICON.item)
        isBroken = true
        updateSidebar()
    }

    override fun startRepairing(player: FnafUPlayer, systemMenu: SystemMenu){
        if (isRepairing) return
        systemMenu.inventory.setItem(guiSlot, Items.REBOOT_ICON.item)
        startRepairingTask(player, systemMenu)
    }

    protected open fun repaired(systemMenu: SystemMenu){
        systemMenu.inventory.setItem(guiSlot, ItemStack(Material.AIR))
        systemMenu.inventory.setItem(Systems.REBOOT_ALL_ICON_POSITION, ItemStack(Material.AIR))
        isBroken = false
        isRepairing = false
        updateSidebar()
        SystemRepairedEvent(this).callEvent()
    }
    protected open fun failedRepairing(systemMenu: SystemMenu) {
        isRepairing = false
        systemMenu.inventory.setItem(Systems.REBOOT_ALL_ICON_POSITION, ItemStack(Material.AIR))
        if (isBroken) systemMenu.inventory.setItem(guiSlot, Items.ERROR_ICON.item)
    }

    protected open fun startRepairingTask(player: FnafUPlayer, systemMenu: SystemMenu){
        isRepairing = true
        repairTask = object : BukkitRunnable(){
            var time = 0
            override fun run() {
                if (!systemMenu.isOpenedByAnyone){
                    failedRepairing(systemMenu)
                    cancel()
                    return
                }
                time += 10
                if (time >= fixTime){
                    repaired(systemMenu)
                    cancel()
                    return
                }
            }
        }
        repairTask!!.runTaskTimer(FnafU.instance, 10, 10)
    }

    protected fun updateSidebar(){
        val (position, component) = getSidebarView()
        game.scoreboard.setLine(position, component)
        game.scoreboard.updateForAll()
    }

    override fun getSidebarView(): Pair<Int, Component>{
        return Pair(sidebarPosition, if (isBroken) sidebarComponent.append(Component.text(" \u26A0")).color(NamedTextColor.RED)
        else sidebarComponent.append(Component.text()).color(NamedTextColor.GREEN))
    }

    override fun reset() {
        isBroken = false
        isRepairing = false
        repairTask?.cancel()
    }
}
