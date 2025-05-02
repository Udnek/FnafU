package me.udnek.fnafu.mechanic.system

import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.datacomponent.item.BundleContents
import me.udnek.fnafu.FnafU
import me.udnek.fnafu.item.Items
import me.udnek.fnafu.mechanic.AudioSystem
import me.udnek.fnafu.mechanic.VentilationSystem
import me.udnek.fnafu.mechanic.camera.CameraSystem
import me.udnek.fnafu.player.FnafUPlayer
import me.udnek.fnafu.util.Resettable
import me.udnek.itemscoreu.custom.minigame.Originable
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable

class Systems : Resettable, Originable  {

    val cursorPosition: HashMap<Int, (player: FnafUPlayer) -> Unit> = hashMapOf(
        9 to {player ->  audioSystem.startFix(player, systemMenu)},
        18 to {player ->  cameraSystem.startFix(player, systemMenu)},
        27 to {player ->  ventilationSystem.startFix(player, systemMenu)},
        36 to {player ->  fixAll(player)})
    val cursorItem = Items.CURSOR.item

    val upButtons = listOf(9, 10, 18, 19, 27, 28)
    val downButtons = listOf(11, 12, 20, 21, 29, 30)
    val enterButtons = listOf(15, 16, 17, 23, 24, 25, 26, 32, 33, 34, 35)

    private val playerInsideSystem = ArrayList<FnafUPlayer>()
    private lateinit var systemMenu: SystemMenu
    private var audioSystem: AudioSystem
    private var cameraSystem: CameraSystem
    private var ventilationSystem: VentilationSystem

    constructor(audioSystem: AudioSystem, cameraSystem: CameraSystem, ventilationSystem: VentilationSystem){
        this.audioSystem = audioSystem
        this.cameraSystem = cameraSystem
        this.ventilationSystem = ventilationSystem
    }

    fun cursorUp(inventory: Inventory) {
        if (isAnyOfSystemRepair()) return

        val indexCursorItem = getIndexCursorItem(inventory)

        if (indexCursorItem == 0){
            inventory.setItem(cursorPosition.keys.toList()[3], cursorItem)
        }else {
            inventory.setItem(cursorPosition.keys.toList()[indexCursorItem - 1], cursorItem)
        }
        inventory.setItem(cursorPosition.keys.toList()[indexCursorItem], ItemStack(Material.AIR))
    }

    fun cursorDown(inventory: Inventory) {
        if (isAnyOfSystemRepair()) return

        val indexCursorItem = getIndexCursorItem(inventory)

        if (indexCursorItem == 3){
            inventory.setItem(cursorPosition.keys.toList()[0], cursorItem)
        }else {
            inventory.setItem(cursorPosition.keys.toList()[indexCursorItem + 1], cursorItem)
        }
        inventory.setItem(cursorPosition.keys.toList()[indexCursorItem], ItemStack(Material.AIR))
    }

    fun enter(player: FnafUPlayer, inventory: Inventory) {
        if (isAnyOfSystemRepair()) return
        cursorPosition[cursorPosition.keys.toList()[getIndexCursorItem(inventory)]]?.invoke(player)
    }

    fun getIndexCursorItem(inventory: Inventory): Int{
        return cursorPosition.keys.toList().indexOf(inventory.first(cursorItem))
    }

    fun fixAll(player: FnafUPlayer){
        if (isAnyOfSystemRepair() or !cameraSystem.isBroken() or !audioSystem.isBroken() or !ventilationSystem.isBroken()) return
        systemMenu.inventory.setItem(41, Items.REBOOT.item)
        audioSystem.repairingTask(player, systemMenu)
        cameraSystem.repairingTask(player, systemMenu)
        ventilationSystem.repairingTask(player, systemMenu)
    }

    fun isAnyOfSystemRepair(): Boolean{
        return audioSystem.isRepairing() or cameraSystem.isRepairing() or ventilationSystem.isRepairing()
    }

    fun exitSystem(inventory: Inventory, player: FnafUPlayer) {
        if (playerInsideSystem.isEmpty()) return
        inventory.setItem(cursorPosition.keys.toList()[0], cursorItem)
        inventory.setItem(getIndexCursorItem(inventory), ItemStack(Material.AIR))
        playerInsideSystem.remove(player)
        player.kit.regive(player)
    }

    fun openMenu(player: FnafUPlayer) {
        systemMenu = SystemMenu()
        val inventory = player.player.inventory

        for (i in upButtons) inventory.setItem(i, Items.UP_BUTTON.item)
        for (i in downButtons) inventory.setItem(i, Items.DOWN_BUTTON.item)
        for (i in enterButtons) inventory.setItem(i, Items.ENTER_BUTTON.item)

        val item = Items.SYSTEM_TABLET.item
        item.setData(DataComponentTypes.BUNDLE_CONTENTS, BundleContents.bundleContents())
        item.setData(DataComponentTypes.HIDE_TOOLTIP)
        object : BukkitRunnable() {
            override fun run() {
                for (i in 0..8) inventory.setItem(i, item)
            }
        }.runTaskLater(FnafU.instance, 1)


        if (audioSystem.isBroken()) audioSystem.destroy(systemMenu)
        if (cameraSystem.isBroken()) cameraSystem.destroy(systemMenu)
        if (ventilationSystem.isBroken()) ventilationSystem.destroy(systemMenu)

        playerInsideSystem.add(player)
        systemMenu.open(player.player)
    }

    override fun reset() {
        for (player in ArrayList<FnafUPlayer>(playerInsideSystem)) {
            exitSystem(player.player.openInventory.topInventory , player)
            player.player.closeInventory()
        }
    }

    override fun setOrigin(p0: Location) {}
}