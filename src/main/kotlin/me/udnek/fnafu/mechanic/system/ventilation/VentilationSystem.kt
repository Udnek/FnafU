package me.udnek.fnafu.mechanic.system.ventilation

import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.datacomponent.item.BundleContents
import io.papermc.paper.datacomponent.item.TooltipDisplay
import me.udnek.fnafu.FnafU
import me.udnek.fnafu.component.FnafUComponents
import me.udnek.fnafu.game.FnafUGame
import me.udnek.fnafu.item.Items
import me.udnek.fnafu.mechanic.system.AbstractSystem
import me.udnek.fnafu.player.FnafUPlayer
import net.kyori.adventure.text.Component
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.scheduler.BukkitRunnable

class VentilationSystem : AbstractSystem {

    companion object {
        const val TIME_BETWEEN_TICK: Int = 10

        const val FIRST_STAGE_TIME: Int = 20 * 20
        const val FIRST_STAGE_EFFECT_LEVEL: Int = 0

        const val SECOND_STAGE_TIME: Int = 40 * 20
        const val SECOND_STAGE_EFFECT_LEVEL: Int = 1

        const val DAMAGE_PER_SECOND = 0.05f

        fun getEffect(level: Int) : PotionEffect =
            PotionEffect(PotionEffectType.SLOWNESS, TIME_BETWEEN_TICK + 1, level, false, true, true)
    }

    override val sidebarPosition: Int = 1
    val vents: List<Vent>
    private var timeBroken: Int = 0
    override var repairIconSlot: Int = 42
    override var sidebarLine: Component = Component.translatable("system.fnafu.ventilation")
    private val menu: VentilationMenu
    val closedVent: Vent?
        get() = vents.firstOrNull { it.isClosed }


    constructor(game: FnafUGame) : super(game) {
        vents = ArrayList(game.map.vents)
        menu = VentilationMenu(game.map.mapImage,vents)
    }

    override fun tick() {
        if (!isBroken) {
            timeBroken = 0
            return
        } else if (closedVent != null) durability -= DAMAGE_PER_SECOND
        if (timeBroken >= SECOND_STAGE_TIME) {
            game.playerContainer.aliveSurvivors.forEach { it.player.addPotionEffect(getEffect(SECOND_STAGE_EFFECT_LEVEL)) }
        } else if (timeBroken >= FIRST_STAGE_TIME) {
            game.playerContainer.aliveSurvivors.forEach { it.player.addPotionEffect(getEffect(FIRST_STAGE_EFFECT_LEVEL)) }
        }
        timeBroken += TIME_BETWEEN_TICK
    }

    fun openMenu(player: FnafUPlayer) {
        menu.open(player.player)
        object : BukkitRunnable() {
            override fun run() {
                val ventTablet = Items.VENTILATION_TABLET.item
                ventTablet.setData(DataComponentTypes.BUNDLE_CONTENTS, BundleContents.bundleContents().build())
                ventTablet.setData(DataComponentTypes.TOOLTIP_DISPLAY, TooltipDisplay.tooltipDisplay().hideTooltip(true))
                ventTablet.setData(DataComponentTypes.ITEM_NAME, Component.empty())
                for (i in 0..8) player.player.inventory.setItem(i, ventTablet)
                for (i in 9..17)  player.player.inventory.setItem(i, Items.EXIT_BUTTON.item)
            }
        }.runTaskLater(FnafU.instance, 1)
    }

    fun onPlayerClickButtonInMenu(vent: Vent, player: FnafUPlayer) {
        if (game.energy.isEndedUp) {
            player.player.closeInventory()
            return
        }
        if (vent.isClosed) {
            vent.toggle()
            updateVents(player)
            return
        }
        val oldVent = closedVent
        if (oldVent == null) {
            vent.toggle()
            updateVents(player)
            return
        }
        oldVent.toggle()
        vent.toggle()
        updateVents(player)
    }

    fun updateVents(player: FnafUPlayer) {
        game.updateEnergy()
        player.player.closeInventory()
        game.applyForEveryAbility { component, player, item ->
            component.components.get(FnafUComponents.VENTILATION_TABLET_ABILITY)?.onPlayerClickButton(item, player)
        }
    }

    fun onMenuExit(player: FnafUPlayer) {
        player.regiveInventory()
    }

    override fun reset() {
        super.reset()
        closedVent?.open()
    }
}