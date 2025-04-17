package me.udnek.fnafu.mechanic.camera

import com.google.common.base.Preconditions
import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.datacomponent.item.Equippable
import me.udnek.fnafu.FnafU
import me.udnek.fnafu.component.Abilities
import me.udnek.fnafu.player.FnafUPlayer
import me.udnek.fnafu.util.Resettable
import me.udnek.itemscoreu.customminigame.Originable
import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component
import org.bukkit.Location
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.EntityType
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.scheduler.BukkitRunnable
import kotlin.math.abs

class CameraSystem : Resettable, Originable {

    private val cameras: MutableList<Camera> = ArrayList()
    private val playerSpectatingCameras = HashMap<FnafUPlayer, Camera>()
    private lateinit var cameraMenu: CameraMenu
    private var mapImage: Component = Component.text("NOT SET")

    fun setMapImage(image: Component) { this.mapImage = image }

    fun getSpectatingCamera(player: FnafUPlayer): Camera? { return playerSpectatingCameras[player] }

    fun spectateCamera(player: FnafUPlayer, id: String){
        spectateCamera(player, getCamera(id)!!)
    }

    fun spectateCamera(player: FnafUPlayer, camera: Camera) {
        val ability = player.abilities.getOrCreateDefault(Abilities.SPECTATE_ENTITY)

        val item = ability.getItem(player)
        for (slot in 0..8) player.player.inventory.setItem(slot, item)

        val cameraOverlay = Equippable.equippable(EquipmentSlot.HEAD).cameraOverlay(Key.key("fnafu:item/camera/blur"))
        item.setData(DataComponentTypes.EQUIPPABLE, cameraOverlay)
        player.player.inventory.setItem(EquipmentSlot.HEAD, item)

        val spectatingCamera = getSpectatingCamera(player)
        if (spectatingCamera != null) {
            ability.spectatingEntity!!.remove()
        }
        setPlayerSpectatingCamera(player, camera)

        val cameraEntity =
            camera.location.first.world.spawnEntity(camera.location.first, EntityType.ARMOR_STAND) as ArmorStand
        cameraEntity.setGravity(false)
        cameraEntity.isMarker = true

        ability.spectate(player, cameraEntity)

//        player.player.gameMode = GameMode.SPECTATOR
//
//        object : BukkitRunnable() {
//            override fun run() {
//                player.player.gameMode = GameMode.SURVIVAL
//            }
//        }.runTaskLater(FnafU.instance, 10)


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

    fun exitCamera(player: FnafUPlayer) {
        if (getSpectatingCamera(player) == null) return
        val spectateEntityAbility = player.abilities.getOrCreateDefault(Abilities.SPECTATE_ENTITY)
        spectateEntityAbility.spectatingEntity!!.remove()
        spectateEntityAbility.spectateSelf(player)
        setPlayerSpectatingCamera(player, null)
        player.player.closeInventory()
        player.kit.regive(player)
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

    fun openMenu(player: FnafUPlayer) {
        cameraMenu.open(player.player)
    }

    fun getCamera(id: String): Camera? {
        for (camera in cameras) {
            if (camera.id == id) {
                return camera
            }
        }
        // TODO DO SOMETHING ABOUT STRING ID
        return null;
    }

    override fun reset() {
        for (player in playerSpectatingCameras.keys) exitCamera(player)
        playerSpectatingCameras.clear()
    }

    override fun setOrigin(origin: Location) {
        for (camera in cameras) camera.setOrigin(origin)
        cameraMenu = CameraMenu(cameras, mapImage)
    }
}
