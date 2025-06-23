package me.udnek.fnafu.mechanic.system.door

import me.udnek.fnafu.FnafU
import me.udnek.fnafu.component.FnafUComponents
import me.udnek.fnafu.game.FnafUGame
import me.udnek.fnafu.mechanic.system.AbstractSystem
import me.udnek.fnafu.player.FnafUPlayer
import me.udnek.fnafu.util.Ticking
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable

class DoorSystem : AbstractSystem {

    override val sidebarPosition: Int = 3
    val doors: MutableList<ButtonDoorPair>
    override var guiSlot: Int = 16
    override var sidebarComponent: Component = Component.translatable("sidebar.fnafu.door_system")
    private val menu: DoorMenu

    constructor(game: FnafUGame, doors: MutableList<ButtonDoorPair>) : super(game){
        this.doors = ArrayList(doors)
        this.menu = DoorMenu(game.map.mapImage, this.doors)
    }

    override fun tick() {
        if (game.energy.isEndedUp) {
            doors.forEach {
                it.door.open()
                it.door.isLocked = true
            }
        }
    }


    fun openMenu(player: FnafUPlayer) {
        menu.open(player.player)
        object : BukkitRunnable() {
            override fun run() { for (i in 0..8) player.player.inventory.setItem(i, ItemStack(Material.AIR)) }
        }.runTaskLater(FnafU.instance, 1)
    }

    fun exitMenu(player: FnafUPlayer) {
        player.kit.regive(player)
    }

    fun updateDoorMenu() {
        menu.updateDoors()
    }

    fun onPlayerClickButton(player: FnafUPlayer) {
        updateEnergy()
        player.player.closeInventory()
        game.applyForEveryAbility { component, player, item ->
            component.components.get(FnafUComponents.DOORMAN_TABLET_ABILITY)?.onPlayerClickButton(item, player)
        }
    }

    fun updateEnergy() {
        game.energy.updateConsumption()
    }

    override fun reset() {
        super.reset()
        doors.forEach { it.door.open() }
    }
}