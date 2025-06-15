package me.udnek.fnafu.mechanic.door

import me.udnek.fnafu.FnafU
import me.udnek.fnafu.game.EnergyGame
import me.udnek.fnafu.mechanic.system.System
import me.udnek.fnafu.player.FnafUPlayer
import me.udnek.fnafu.util.Resettable
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable

class DoorSystem : Resettable, System {

    override val game: EnergyGame
    override val sidebarPosition: Int = 3
    private var doorMenu: DoorMenu

    constructor(game: EnergyGame) : super(16, "sidebar.fnafu.door_system") {
        this.game = game
        doorMenu = DoorMenu(game.map.mapImage, game.map.doors)
    }

    fun openMenu(player: FnafUPlayer) {
        doorMenu.open(player.player)
        object : BukkitRunnable() {
            override fun run() { for (i in 0..8) player.player.inventory.setItem(i, ItemStack(Material.AIR)) }
        }.runTaskLater(FnafU.instance, 1)
    }

    fun updateEnergy() {
        game.energy.updateConsumption()
    }

    fun exitSystem(player: FnafUPlayer) {
        player.kit.regive(player)
    }

    override fun reset() {}
}