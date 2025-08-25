package me.udnek.fnafu.mechanic.system

import me.udnek.coreu.util.Utils
import me.udnek.fnafu.FnafU
import me.udnek.fnafu.event.SystemRepairedEvent
import me.udnek.fnafu.game.FnafUGame
import me.udnek.fnafu.item.Items
import me.udnek.fnafu.player.FnafUPlayer
import me.udnek.fnafu.sound.Sounds
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.scheduler.BukkitRunnable
import kotlin.math.floor

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
            if (field == 0f && oldField > 0f) destroy()
            else if (oldField < 1f && field == 1f) repaired(game.systems.menu)
            game.updateSidebar()
        }


    override fun destroy(){
        game.systems.menu.inventory.setItem(repairIconSlot, Items.ERROR_ICON.item)
        isBroken = true
        durability = 0f
    }

    override fun startRepairing(
        player: FnafUPlayer,
        systemsMenu: SystemsMenu,
        repairDuration: Int,
        setRepairIcon: Boolean,
        playSound: Boolean
    ) {
        if (isRepairing) return
        if (setRepairIcon) systemsMenu.setItem(repairIconSlot, Items.REBOOT_ICON)
        startRepairingTask(player, repairDuration, systemsMenu, playSound)
    }

    protected open fun repaired(systemsMenu: SystemsMenu){
        systemsMenu.setItem(repairIconSlot, null)
        systemsMenu.setItem(Systems.REBOOT_ALL_REPAIR_ICON_POSITION, null)
        isRepairing = false
        isBroken = false
        durability = 1f
        SystemRepairedEvent(this).callEvent()
    }
    protected open fun failedRepairing(menu: SystemsMenu) {
        isRepairing = false
        menu.setItem(repairIconSlot, null)
        menu.setItem(Systems.REBOOT_ALL_REPAIR_ICON_POSITION, null)
        if (isBroken) menu.inventory.setItem(repairIconSlot, Items.ERROR_ICON.item)
    }

    protected open fun startRepairingTask(player: FnafUPlayer, duration: Int, systemsMenu: SystemsMenu, playSound: Boolean){
        isRepairing = true
        val TICKRATE = 10
        repairTask = object : BukkitRunnable(){
            var time = 0
            override fun run() {
                if (!systemsMenu.isOpenedByAnyone){
                    failedRepairing(systemsMenu)
                    cancel()
                    return
                }
                if (playSound && time/TICKRATE % 2 == 0){
                    systemsMenu.viewers.forEach { Sounds.SYSTEM_REBOOT.play(it) }
                }
                time += TICKRATE
                if (time >= duration){
                    if (playSound) systemsMenu.viewers.forEach { Sounds.SYSTEM_REPAIRED.play(it) }
                    repaired(systemsMenu)
                    cancel()
                    return
                }
            }
        }
        repairTask!!.runTaskTimer(FnafU.instance, TICKRATE.toLong(), TICKRATE.toLong())
    }

    protected fun updateSidebar(){
        val (position, component) = getSidebarLine()
        game.sidebar.setLine(position, component)
        game.sidebar.updateForAll()
    }

    override fun getSidebarLine(): Pair<Int, Component>{
        var component = sidebarLine.append(Component.text(" (${(durability * 100.0).toInt()}%)"))
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
