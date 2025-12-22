package me.udnek.fnafu.component.survivor.tablet

import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.datacomponent.item.CustomModelData
import me.udnek.coreu.custom.component.CustomComponent
import me.udnek.coreu.custom.component.CustomComponentType
import me.udnek.coreu.custom.equipmentslot.universal.UniversalInventorySlot
import me.udnek.coreu.custom.item.CustomItem
import me.udnek.coreu.rpgu.component.RPGUActiveItem
import me.udnek.coreu.rpgu.component.RPGUComponents
import me.udnek.coreu.rpgu.component.ability.property.AttributeBasedProperty
import me.udnek.coreu.rpgu.lore.ability.AbilityLorePart
import me.udnek.coreu.util.Utils
import me.udnek.fnafu.component.FnafUActiveAbility
import me.udnek.fnafu.component.FnafUComponents
import me.udnek.fnafu.item.Items
import me.udnek.fnafu.misc.play
import me.udnek.fnafu.misc.toApache
import me.udnek.fnafu.player.FnafUPlayer
import me.udnek.fnafu.sound.Sounds
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor
import org.apache.commons.lang3.tuple.Pair
import org.bukkit.Color
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack

open class CameraTabletAbility(val guiColor: Color, val noiseColor: TextColor, val isCut: Boolean, val nightVision: Boolean, val damagePerUsage: Float) : FnafUActiveAbility() {

    init {
        components.set(AttributeBasedProperty(0.5 * 20, RPGUComponents.ABILITY_COOLDOWN_TIME))
    }

    companion object {
        val CUT: CameraTabletAbility = CameraTabletAbility( Color.WHITE, NamedTextColor.WHITE, true, false, 0.06f)
        val FULL: CameraTabletAbility = CameraTabletAbility(Color.GREEN, NamedTextColor.GREEN, false, true,.03f)
    }

    override fun addPropertyLines(lorePart: AbilityLorePart) {
        super.addPropertyLines(lorePart)
        lorePart.addAbilityStat(
            Component.translatable("ability.fnafu.camera_tablet.damage_per_usage", listOf(
                Component.text(Utils.roundToTwoDigits(damagePerUsage*100.0)))))
    }

    open fun getOverlay() : ItemStack {
        val item = Items.CAMERA_OVERLAY.item
        item.setData(DataComponentTypes.CUSTOM_MODEL_DATA, CustomModelData.customModelData().addColor(guiColor))
        return item
    }

    override fun getEngAndRuDescription(): Pair<List<String?>?, List<String?>?> {
        return (listOf("Opens camera system") to listOf("Открывает систему камер")).toApache()
    }

    override fun getType(): CustomComponentType<in RPGUActiveItem, out CustomComponent<in RPGUActiveItem>?> {
        return FnafUComponents.CAMERA_TABLET_ABILITY
    }

    override fun action(
        item: CustomItem,
        player: FnafUPlayer,
        slot: UniversalInventorySlot,
        event: PlayerInteractEvent
    ): ActionResult {
        val cameras = player.game.systems.camera
        if (cameras.isBroken || player.game.energy.isEndedUp) return ActionResult.NO_COOLDOWN
        cameras.durability -= damagePerUsage
        if (cameras.isBroken || player.game.energy.isEndedUp) {
            return ActionResult.NO_COOLDOWN
        }
        Sounds.CAMERA_TABLET_OPEN.play(player)
        cameras.openMenu(player, event.item!!)
        val lastCamera = player.data.getOrCreateDefault(FnafUComponents.SPECTATE_CAMERA_DATA).lastCamera
        cameras.spectateCamera(player, lastCamera ?: cameras.cameras.firstOrNull{it.isInCutMenu}!!, event.item!!)
        return ActionResult.FULL_COOLDOWN
    }

}