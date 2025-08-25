package me.udnek.fnafu.mechanic.system.ventilation

import io.papermc.paper.datacomponent.DataComponentTypes
import me.udnek.coreu.custom.inventory.ConstructableCustomInventory
import me.udnek.coreu.util.ComponentU
import me.udnek.fnafu.item.Items
import me.udnek.fnafu.item.survivor.ventilation.VentilationTabletButton
import me.udnek.fnafu.misc.getCustom
import me.udnek.fnafu.misc.getFnafU
import me.udnek.fnafu.misc.play
import me.udnek.fnafu.sound.Sounds
import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent

class VentilationMenu : ConstructableCustomInventory {

    companion object {
        const val ACTIVE_COLOR = 1814966
        const val INACTIVE_COLOR = 744029
    }

    private val title: Component
    private val vents: List<Vent>
    private val tabletBackground =
        ComponentU.textWithNoSpaceSpaceFont(-8, Component.text("1").font(Key.key("fnafu:system/ventilation")).color(TextColor.color(1f, 1f, 1f)), 175)

    constructor(title: Component, vents: List<Vent>) : super() {
        this.title = tabletBackground.append(title.color(TextColor.color(ACTIVE_COLOR)))
        this.vents = vents
        updateVentIcons()
    }

    fun updateVentIcons() {
        vents.forEachIndexed { index, vent ->
            inventory.setItem(vent.tabletMenuPosition, VentilationTabletButton.getWithVent(vent, index))
        }
    }

    override fun onPlayerClicksItem(event: InventoryClickEvent) {
        event.isCancelled = true
        val player = (event.whoClicked as Player).getFnafU() ?: return
        when (event.currentItem?.getCustom()?: return) {
            Items.VENTILATION_BUTTON -> {
                val vent = vents[event.currentItem?.getData(DataComponentTypes.CUSTOM_MODEL_DATA)?.floats()[0]?.toInt() ?: return]
                player.let {
                    Sounds.BUTTON_CLICK.play(it)
                    it.game.systems.ventilation.onPlayerClickButtonInMenu(vent, it)
                }
                updateVentIcons()
            }
            Items.EXIT_BUTTON -> player.player.closeInventory()
        }

    }

    override fun onPlayerClosesInventory(event: InventoryCloseEvent) {
        (event.player as Player).getFnafU()?.let { it.game.systems.ventilation.onMenuExit(it) }
    }

    override fun getTitle(): Component = title
    override fun getInventorySize(): Int = 9 * 6
}