package me.udnek.fnafu.mechanic.system.ventilation

import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.datacomponent.item.BundleContents
import io.papermc.paper.datacomponent.item.TooltipDisplay
import me.udnek.fnafu.FnafU
import me.udnek.fnafu.component.FnafUComponents
import me.udnek.fnafu.game.FnafUGame
import me.udnek.fnafu.item.Items
import me.udnek.fnafu.mechanic.system.AbstractSystem
import me.udnek.fnafu.mechanic.system.Systems
import me.udnek.fnafu.mechanic.system.SystemsMenu
import me.udnek.fnafu.player.FnafUPlayer
import net.kyori.adventure.bossbar.BossBar
import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.title.Title
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.scheduler.BukkitRunnable
import java.time.Duration

class VentilationSystem : AbstractSystem {

    companion object {
        const val FIRST_STAGE_TIME: Int = 20 * 20
        const val FIRST_STAGE_EFFECT_LEVEL: Int = 0

        const val SECOND_STAGE_TIME: Int = 40 * 20
        const val SECOND_STAGE_EFFECT_LEVEL: Int = 1

        const val BLACKOUT_TICKRATE: Int = 6*20
        const val BLACKOUT_STAY_TIME: Int = 0
        const val BLACKOUT_FADE_TIME: Int = 20

        const val CLOSED_VENT_DPS: Float = 0.05f
        const val BASE_DPS: Float = 0.01f
    }

    private val brokenBossBar: BossBar = BossBar.bossBar(
        Component.empty(),
        BossBar.MIN_PROGRESS,
        BossBar.Color.RED,
        BossBar.Overlay.PROGRESS).also {
            it.addFlag(BossBar.Flag.DARKEN_SCREEN)
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
        menu = VentilationMenu(game.map.mapImage, vents)
    }

    fun getBrokenEffect(level: Int) : PotionEffect {
        return PotionEffect(PotionEffectType.SLOWNESS, Systems.TICKRATE + 1, level, false, false, true)
    }
    fun applyBrokenEffect(){
        game.playerContainer.aliveSurvivors.forEach { brokenBossBar.addViewer(it.player) }

        var lvl: Int? = null
        if (timeBroken >= SECOND_STAGE_TIME) {
            lvl = SECOND_STAGE_EFFECT_LEVEL
        } else if (timeBroken >= FIRST_STAGE_TIME) {
            lvl = FIRST_STAGE_EFFECT_LEVEL
        }
        if (lvl == null) return
        val effect = getBrokenEffect(lvl)
        game.playerContainer.aliveSurvivors.forEach { it.player.addPotionEffect(effect) }
    }

    override fun repaired(systemsMenu: SystemsMenu) {
        super.repaired(systemsMenu)
        game.playerContainer.survivors.forEach { brokenBossBar.removeViewer(it.player) }
    }

    override fun tick() {
        if (isBroken) {
            applyBrokenEffect()
            if (timeBroken % BLACKOUT_TICKRATE == 0) blackout()
            timeBroken += Systems.TICKRATE
            return
        } else {
            timeBroken = 0
            durability -= (Systems.TICKRATE / 20f) * (if (closedVent == null) BASE_DPS else CLOSED_VENT_DPS)
        }

    }

    private fun blackout(){
        game.playerContainer.aliveSurvivors.forEach {
            it.player.showTitle(
                Title.title(
                    Component.text("2").font(Key.key("fnafu:ventilationman")).color(NamedTextColor.WHITE),
                    Component.empty(),
                    Title.Times.times(
                        Duration.ofMillis((BLACKOUT_FADE_TIME / 20f * 1000f).toLong()),
                        Duration.ofMillis((BLACKOUT_STAY_TIME / 20f * 1000f).toLong()),
                        Duration.ofMillis((BLACKOUT_FADE_TIME / 20f * 1000f).toLong())
                    )
                )
            )
        }
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