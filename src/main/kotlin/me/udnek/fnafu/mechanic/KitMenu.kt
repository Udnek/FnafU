package me.udnek.fnafu.mechanic

import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.datacomponent.item.ResolvableProfile
import me.udnek.coreu.custom.inventory.ConstructableCustomInventory
import me.udnek.coreu.custom.inventory.CustomInventory
import me.udnek.coreu.util.ComponentU
import me.udnek.fnafu.FnafU
import me.udnek.fnafu.component.FnafUComponents
import me.udnek.fnafu.component.KitStageData
import me.udnek.fnafu.component.kit.Kit
import me.udnek.fnafu.game.FnafUGame
import me.udnek.fnafu.item.Items
import me.udnek.fnafu.misc.getCustom
import me.udnek.fnafu.player.FnafUPlayer
import me.udnek.fnafu.misc.getFnafU
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable

class KitMenu : ConstructableCustomInventory() {

    companion object{

        fun updateFor(players: List<FnafUPlayer>){
            for (receiver in players) {
                var survCursor = -9
                var animCursor = 9*6
                val menu = CustomInventory.get(receiver.player.openInventory.topInventory) as? KitMenu ?: continue
                val downMenu = receiver.player.inventory
                menu.inventory.clear()
                downMenu.clear()
                for (currentPlayer in players) {
                    // CHOOSING POSITION
                    val cursor: Int
                    if (currentPlayer.type == FnafUPlayer.Type.SURVIVOR) {
                        survCursor += 9
                        cursor = survCursor
                    }
                    else {
                        animCursor -= 9
                        cursor = animCursor
                    }
                    // READY OR NOT
                    val kitStageData = getKitStageData(currentPlayer)
                    menu.setItem(
                        cursor, if (kitStageData.isReady) Material.LIME_STAINED_GLASS_PANE else Material.RED_STAINED_GLASS_PANE
                    )
                    // PROFILE ICON
                    val profileIcon = ItemStack(Material.PLAYER_HEAD)
                    profileIcon.setData(DataComponentTypes.PROFILE, ResolvableProfile.resolvableProfile(currentPlayer.player.playerProfile))
                    menu.setItem(cursor+1, profileIcon)
                    // ITEMS
                    if (receiver.type == currentPlayer.type){
                        val kitItems = ArrayList(kitStageData.chosenKit.permanentItems)
                        kitItems.addAll(kitStageData.chosenKit.inventoryItems)
                        kitItems.forEachIndexed { i, item ->
                            if (i >= 6) return@forEachIndexed
                            menu.setItem(cursor + 2 + i, item)
                        }
                    } else {
                        for (i in 2..6){
                            menu.setItem(cursor + i, Material.BLACK_STAINED_GLASS_PANE)
                        }
                    }
                }

                // KITS
                var i = 0
                val kits = getAllowedKits(receiver)
                main@for (y in listOf(9, 18, 27)){
                    for (x in 0..5){
                        downMenu.setItem(y+x, kits[i++].displayItem)
                        if (i >= kits.size) break@main
                    }
                }

                val button = if (getKitStageData(receiver).isReady) Items.CANCEL_BUTTON.item else Items.READY_BUTTON.item
                for (i in 2..6) {
                    downMenu.setItem(i, button)
                }
            }
        }

        fun getKitOf(itemStack: ItemStack) : Kit? {
            return Kit.REGISTRY.getAll().firstOrNull { it.displayItem.getCustom() == itemStack.getCustom() }
        }

        fun getKitStageData(player: FnafUPlayer): KitStageData {
            return player.data.getOrCreateDefault(FnafUComponents.KIT_STAGE_DATA)
        }

        fun getAllowedKits(player: FnafUPlayer) : List<Kit> {
            return Kit.REGISTRY.getAll().filter { it.playerType == player.type }
        }
    }


    override fun onPlayerOpensInventory(event: InventoryOpenEvent) {
        (event.player as Player).getFnafU()?.let { player ->
            val kitStageData = getKitStageData(player)
            if (kitStageData.chosenKit.playerType == player.type) return
            kitStageData.chosenKit = getAllowedKits(player).first()
        }
    }

    override fun onPlayerClicksItem(event: InventoryClickEvent) {
        event.isCancelled = true
        val player = (event.whoClicked as Player).getFnafU() ?: return
        when (event.currentItem?.getCustom()){
            Items.READY_BUTTON -> {
                getKitStageData(player).isReady = true
            }
            Items.CANCEL_BUTTON -> {
                getKitStageData(player).isReady = false
            }
            else -> {
                getKitStageData(player).chosenKit = getKitOf(event.currentItem?: return) ?: return
            }
        }
        updateFor(player.game.playerContainer.all)
    }


    override fun onPlayerClosesInventory(event: InventoryCloseEvent) {
        (event.player as Player).getFnafU()?.let {
            it.kit = getKitStageData(it).chosenKit
            if (it.game.stage == FnafUGame.Stage.KIT) {
                object : BukkitRunnable() {
                    override fun run() { open(event.player as Player) }
                }.runTaskLater(FnafU.instance, 1)
            }
        }
    }

    override fun getTitle(): Component =
        ComponentU.textWithNoSpaceSpaceFont(-8,  Component.translatable("container.fnafu.kit.image").font(NamespacedKey(FnafU.instance, "kit")), 171)
            .append(Component.translatable("container.fnafu.kit")).color(NamedTextColor.WHITE)
    override fun getInventorySize(): Int = 9 * 6
    override fun shouldAutoUpdateItems(): Boolean = false
}