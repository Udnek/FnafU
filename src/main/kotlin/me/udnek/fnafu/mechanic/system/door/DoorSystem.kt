package me.udnek.fnafu.mechanic.system.door

import me.udnek.fnafu.FnafU
import me.udnek.fnafu.component.FnafUComponents
import me.udnek.fnafu.component.survivor.DoormanTabletAbility
import me.udnek.fnafu.game.FnafUGame
import me.udnek.fnafu.mechanic.system.AbstractSystem
import me.udnek.fnafu.mechanic.system.door.door.Door
import me.udnek.fnafu.player.FnafUPlayer
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable

class DoorSystem : AbstractSystem {

    override val sidebarPosition: Int = 3
    val doors: MutableList<ButtonDoorPair>
    override var guiSlot: Int = 16
    override var sidebarLine: Component = Component.translatable("system.fnafu.door")
    private val menu: DoorMenu

    constructor(game: FnafUGame) : super(game){
        doors = ArrayList(game.map.doors)
        menu = DoorMenu(game.map.mapImage, doors)
    }

    override fun tick() {}

    fun openMenu(player: FnafUPlayer) {
        menu.open(player.player)
        object : BukkitRunnable() {
            override fun run() { for (i in 0..8) player.player.inventory.setItem(i, ItemStack(Material.AIR)) }
        }.runTaskLater(FnafU.instance, 1)
    }

    fun onMenuExit(player: FnafUPlayer) {
        player.regiveInventory()
    }

    fun updateDoorMenu() {
        menu.updateDoorIcons()
    }

    fun onPlayerClickButtonInMenu(door: Door, player: FnafUPlayer) {
        if (game.energy.isEndedUp) {
            player.player.closeInventory()
            return
        }

        if (door.isLocked) return
        door.toggle()
        updateEnergy()
        player.player.closeInventory()
        durability -= DoormanTabletAbility.DAMAGE_PER_USAGE
        game.applyForEveryAbility { component, player, item ->
            component.components.get(FnafUComponents.DOORMAN_TABLET_ABILITY)?.onPlayerClickButton(item, player)
        }
    }

    fun updateEnergy() {
        game.energy.updateConsumption()
    }

    override fun reset() {
        super.reset()
        doors.forEach { it.reset() }
    }
}