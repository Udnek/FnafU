package me.udnek.fnafu.mechanic.camera

import com.google.common.base.Preconditions
import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.datacomponent.item.Equippable
import me.udnek.fnafu.FnafU
import me.udnek.fnafu.component.Abilities
import me.udnek.fnafu.component.Components
import me.udnek.fnafu.game.EnergyGame
import me.udnek.fnafu.mechanic.system.System
import me.udnek.fnafu.mechanic.system.SystemMenu
import me.udnek.fnafu.player.FnafUPlayer
import me.udnek.fnafu.util.Resettable
import me.udnek.itemscoreu.custom.minigame.Originable
import me.udnek.itemscoreu.customitem.CustomItem
import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component
import org.bukkit.Location
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.EntityType
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable
import kotlin.math.abs

open class CameraSystem : Resettable, Originable, System {

    final override val game: EnergyGame
    override val sidebarPosition: Int = 2
    private val cameras: MutableList<Camera> = ArrayList()
    private val playerSpectatingCameras = HashMap<FnafUPlayer, Camera>()
    private lateinit var cameraMenu: CameraMenu
    private var mapImage: Component = Component.text("NOT SET")

    constructor(game: EnergyGame) : super(25, "sidebar.fnafu.camera_system") {
        this.game = game
    }

    fun setMapImage(image: Component) { this.mapImage = image }

    fun getSpectatingCamera(player: FnafUPlayer): Camera? { return playerSpectatingCameras[player] }

    fun spectateCamera(player: FnafUPlayer, id: String, cameraTablet: ItemStack) {
        val camera = getCamera(id) ?: throw RuntimeException("camera's id is wrong: $id")
        spectateCamera(player, camera, cameraTablet)
    }

    fun spectateCamera(player: FnafUPlayer, camera: Camera, cameraTablet: ItemStack) {
        val ability = player.abilities.getOrCreateDefault(Abilities.SPECTATE_ENTITY)
        cameraMenu.updateCameras(cameras, camera, cameraTablet)
        switchCameraOverlay(player, cameraTablet)

        val spectatingCamera = getSpectatingCamera(player)
        if (spectatingCamera != null) {
            ability.spectatingEntity!!.remove()
        }
        setPlayerSpectatingCamera(player, camera)

//        player.player.gameMode = GameMode.SPECTATOR
//
//        object : BukkitRunnable() {
//            override fun run() {
//                player.player.gameMode = GameMode.SURVIVAL
//            }
//        }.runTaskLater(FnafU.instance, 10)

        val cameraEntity =
            camera.location.first.world.spawnEntity(camera.location.first, EntityType.ARMOR_STAND) as ArmorStand
        ability.spectate(player, cameraEntity)
        cameraMovement(camera, cameraEntity)
    }

    fun exitCamera(player: FnafUPlayer) {
        if (getSpectatingCamera(player) == null) return
        val spectateEntityAbility = player.abilities.getOrCreateDefault(Abilities.SPECTATE_ENTITY)
        spectateEntityAbility.spectatingEntity!!.remove()
        spectateEntityAbility.spectateSelf(player)
        setPlayerSpectatingCamera(player, null)
        player.player.closeInventory()
        player.kit.regive(player)
    }

    private fun cameraMovement(camera: Camera, cameraEntity: ArmorStand){
        cameraEntity.setGravity(false)
        cameraEntity.isMarker = true

        if (camera.rotationAngle == 0f) return
        object : BukkitRunnable() {
            var rotateCounter: Float = 0f
            var rotatePerTick: Float = 1f
            var rotationDelay: Int = 0
            val rotationAngle: Float = camera.rotationAngle

            override fun run() {
                if (!cameraEntity.isValid) {
                    cancel()
                    return
                }

                if (rotationDelay > 0) {
                    rotationDelay -= 1
                    return
                }

                cameraEntity.setRotation(cameraEntity.yaw + rotatePerTick, cameraEntity.pitch)
                rotateCounter += rotatePerTick

                if (abs(rotateCounter.toDouble()) >= rotationAngle) {
                    rotatePerTick *= -1f
                    rotationDelay = 20
                }
            }
        }.runTaskTimer(FnafU.instance, 20, 1)
    }

    private fun switchCameraOverlay(player: FnafUPlayer, item: ItemStack) {
        val inventory = player.player.inventory
        val component = CustomItem.get(item)?.components?.getOrDefault(Components.TABLET_COMPONENT) ?: return

        item.setData(DataComponentTypes.ITEM_NAME, Component.empty())
        object : BukkitRunnable() {
            override fun run() {
                for (slot in 0..8) inventory.setItem(slot, item)
            }
        }.runTaskLater(FnafU.instance, 1)

        val cameraOverlay = Equippable.equippable(EquipmentSlot.HEAD).cameraOverlay(Key.key("fnafu:item/camera/frame_overlay"))
        item.setData(DataComponentTypes.EQUIPPABLE, cameraOverlay)
        inventory.setItem(EquipmentSlot.HEAD, item)
        inventory.setItem(40, component.createGui())

        player.showNoise(component.noiseColor)
    }

    private fun setPlayerSpectatingCamera(player: FnafUPlayer, camera: Camera?) {
        if (camera == null) playerSpectatingCameras.remove(player)
        else playerSpectatingCameras[player] = camera
    }

    fun addCamera(camera: Camera): CameraSystem {
        Preconditions.checkArgument(
            getCamera(camera.id) == null,
            "Camera with id '" + camera.id + " is already exists!"
        )
        cameras.add(camera)
        camera.number = cameras.size - 1
        return this
    }

    fun openMenu(player: FnafUPlayer, cameraTablet: ItemStack) {
        cameraMenu = CameraMenu(cameras, mapImage, cameraTablet)
        cameraMenu.open(player.player)
    }

    fun getCamera(id: String): Camera? {
        for (camera in cameras) {
            if (camera.id == id) {
                return camera
            }
        }
        // TODO DO SOMETHING ABOUT STRING ID
        return null
    }

    override fun reset() {
        playerSpectatingCameras.keys.forEach { exitCamera(it) }
        playerSpectatingCameras.clear()
    }

    override fun setOrigin(origin: Location) {
        for (camera in cameras) camera.setOrigin(origin)
    }

    override fun destroy(systemMenu: SystemMenu) {
        reset()
        super.destroy(systemMenu)
    }

}
