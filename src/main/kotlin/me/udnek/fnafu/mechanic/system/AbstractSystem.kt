package me.udnek.fnafu.mechanic.system

import me.udnek.coreu.util.Utils
import me.udnek.fnafu.FnafU
import me.udnek.fnafu.event.SystemRepairedEvent
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
        protected set(value) {
            field = value
            updateSidebar()
        }
    override var isRepairing = false
        protected set
    protected abstract var sidebarLine: Component
    protected var repairTask: BukkitRunnable? = null
    override var durability: Float = 1f
        set(value) {
            val oldField = field
            field = Math.clamp(value, 0f, 1f)
            if (field == 0f && !isBroken) destroy()
            else if (oldField < 1f && field == 1f) repaired(game.systems.menu)
            game.updateSidebar()
        }


    override fun destroy(){
        game.systems.menu.inventory.setItem(guiSlot, Items.ERROR_ICON.item)
        isBroken = true
        durability = 0f
    }

    override fun startRepairing(player: FnafUPlayer, systemsMenu: SystemsMenu, repairDuration: Int, setRepairIcon: Boolean) {
        if (isRepairing) return
        if (setRepairIcon) systemsMenu.inventory.setItem(guiSlot, Items.REBOOT_ICON.item)
        startRepairingTask(player, repairDuration, systemsMenu)
    }

    protected open fun repaired(systemsMenu: SystemsMenu){
        systemsMenu.inventory.setItem(guiSlot, ItemStack(Material.AIR))
        systemsMenu.inventory.setItem(Systems.REBOOT_ALL_ICON_POSITION, ItemStack(Material.AIR))
        isRepairing = false
        isBroken = false
        durability = 1f
        SystemRepairedEvent(this).callEvent()
    }
    protected open fun failedRepairing(systemsMenu: SystemsMenu) {
        isRepairing = false
        systemsMenu.inventory.setItem(guiSlot, ItemStack(Material.AIR))
        systemsMenu.inventory.setItem(Systems.REBOOT_ALL_ICON_POSITION, ItemStack(Material.AIR))
        if (isBroken) systemsMenu.inventory.setItem(guiSlot, Items.ERROR_ICON.item)
    }

    protected open fun startRepairingTask(player: FnafUPlayer, duration: Int, systemsMenu: SystemsMenu){
        isRepairing = true
        repairTask = object : BukkitRunnable(){
            var time = 0
            override fun run() {
                if (!systemsMenu.isOpenedByAnyone){
                    failedRepairing(systemsMenu)
                    cancel()
                    return
                }
                time += 10
                if (time >= duration){
                    repaired(systemsMenu)
                    cancel()
                    return
                }
            }
        }
        repairTask!!.runTaskTimer(FnafU.instance, 10, 10)
    }

    protected fun updateSidebar(){
        val (position, component) = getSidebarLine()
        game.sidebar.setLine(position, component)
        game.sidebar.updateForAll()
    }

    override fun getSidebarLine(): Pair<Int, Component>{
        var component = sidebarLine.append(Component.text(" (${Utils.roundToTwoDigits(durability*100.0)}%)"))
        if (isBroken) {
            component = component.append(Component.text(" ").append(Component.translatable("system.fnafu.broken_icon"))).color(NamedTextColor.RED)
        } else {
            component = component.color(Utils.mixColors(NamedTextColor.RED, NamedTextColor.GREEN, durability))
        }
        return Pair(sidebarPosition, component)
    }

    override fun reset() {
        isBroken = false
        isRepairing = false
        durability = 1f
        repairTask?.cancel()
    }
}
