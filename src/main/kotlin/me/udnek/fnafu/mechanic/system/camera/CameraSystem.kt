package me.udnek.fnafu.mechanic.system.camera

import com.google.common.base.Preconditions
import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.datacomponent.item.Equippable
import me.udnek.coreu.custom.item.CustomItem
import me.udnek.coreu.mgu.Originable
import me.udnek.coreu.rpgu.component.RPGUComponents
import me.udnek.fnafu.FnafU
import me.udnek.fnafu.component.FnafUComponents
import me.udnek.fnafu.game.FnafUGame
import me.udnek.fnafu.mechanic.system.AbstractSystem
import me.udnek.fnafu.misc.getCustom
import me.udnek.fnafu.player.FnafUPlayer
import me.udnek.fnafu.sound.Sounds
import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component
import org.bukkit.Location
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.EntityType
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.scheduler.BukkitRunnable
import kotlin.math.abs

open class CameraSystem : Originable, AbstractSystem {

    constructor(game: FnafUGame) : super(game) {
        cameras = ArrayList()
        playerSpectatingCameras = HashMap<FnafUPlayer, Camera>()

        game.map.cameras.forEach { addCamera(it) }
        setOrigin(game.map.origin)
    }

    override val sidebarPosition: Int = 2
    val cameras: MutableList<Camera>
    private val playerSpectatingCameras: HashMap<FnafUPlayer, Camera>
    override var repairIconSlot: Int = 34
    override var sidebarLine: Component = Component.translatable("system.fnafu.camera")

    override fun tick() {
        if (game.energy.isEndedUp || isBroken) exitAll()
    }

    fun getSpectatingCamera(player: FnafUPlayer): Camera? { return playerSpectatingCameras[player] }

    private fun setSpectatingCamera(player: FnafUPlayer, camera: Camera?, tablet: ItemStack?) {
        if (camera == null) playerSpectatingCameras.remove(player)
        else {
            playerSpectatingCameras[player] = camera
            val data = player.data.getOrCreateDefault(FnafUComponents.SPECTATE_CAMERA_DATA)
            data.lastCamera = camera
            data.tablet = tablet
        }
    }

    fun spectateCamera(player: FnafUPlayer, id: String, cameraTablet: ItemStack) {
        val camera = getCamera(id) ?: throw RuntimeException("camera's id is wrong: $id")
        spectateCamera(player, camera, cameraTablet)
    }

    fun spectateCamera(player: FnafUPlayer, camera: Camera, cameraTablet: ItemStack) {
        val spectateData = player.data.getOrCreateDefault(FnafUComponents.SPECTATE_ENTITY_DATA)
        val cameraData = player.data.getOrCreateDefault(FnafUComponents.SPECTATE_CAMERA_DATA)

        cameraData.cameraMenu!!.updateCameras(cameras, camera, cameraTablet)
        cameraOverlay(player, cameraTablet)

        val spectatingCamera = getSpectatingCamera(player)
        if (spectatingCamera != null) {
            spectateData.spectatingEntity!!.remove()
        }
        setSpectatingCamera(player, camera, cameraTablet)

        val cameraEntity = camera.location.first.world.spawnEntity(camera.location.first, EntityType.ARMOR_STAND) as ArmorStand
        cameraEntity.isInvisible = true
        cameraEntity.setGravity(false)
        cameraEntity.isMarker = true
        spectateData.spectate(player, cameraEntity)

        playCameraRotation(camera, cameraEntity)

        val isCut = cameraTablet.getCustom()?.components?.get(RPGUComponents.ACTIVE_ABILITY_ITEM)?.components?.getOrCreateDefault(FnafUComponents.CAMERA_TABLET_ABILITY)?.isCut ?: false
        if (!isCut){
            player.player.addPotionEffect(PotionEffect(PotionEffectType.NIGHT_VISION, PotionEffect.INFINITE_DURATION, 0, false, false, false))
        }
    }

    fun exitCamera(player: FnafUPlayer) {
        if (getSpectatingCamera(player) == null) return
        val spectateEntityAbility = player.data.getOrCreateDefault(FnafUComponents.SPECTATE_ENTITY_DATA)
        val spectateCameraAbility = player.data.getOrCreateDefault(FnafUComponents.SPECTATE_CAMERA_DATA)
        setSpectatingCamera(player, null, null)
        spectateEntityAbility.spectatingEntity!!.remove()
        spectateEntityAbility.spectateSelf(player)

        player.regiveInventory()
        player.player.closeInventory()
        player.player.removePotionEffect(PotionEffectType.NIGHT_VISION)

        val tabletAbility = spectateCameraAbility.tablet?.getCustom()
            ?.components?.get(RPGUComponents.ACTIVE_ABILITY_ITEM)
            ?.components?.get(FnafUComponents.CAMERA_TABLET_ABILITY)?: FnafUComponents.CAMERA_TABLET_ABILITY.default
        player.showNoise(tabletAbility.noiseColor)
        Sounds.CAMERA_TABLET_OPEN.stop(player.player)
    }

    private fun playCameraRotation(camera: Camera, cameraEntity: ArmorStand){
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

    private fun cameraOverlay(player: FnafUPlayer, item: ItemStack) {
        val inventory = player.player.inventory
        val component = CustomItem.get(item)
            ?.components?.get(RPGUComponents.ACTIVE_ABILITY_ITEM)
            ?.components?.get(FnafUComponents.CAMERA_TABLET_ABILITY) ?: return

        item.setData(DataComponentTypes.ITEM_NAME, Component.empty())
        object : BukkitRunnable() {
            override fun run() {
                for (slot in 0..8) inventory.setItem(slot, item)
            }
        }.runTaskLater(FnafU.instance, 1)

        val cameraOverlay = Equippable.equippable(EquipmentSlot.HEAD).cameraOverlay(Key.key("fnafu:item/system/camera/frame_overlay"))
        item.setData(DataComponentTypes.EQUIPPABLE, cameraOverlay)
        inventory.setItem(EquipmentSlot.HEAD, item)
        inventory.setItem(40, component.getOverlay())

        player.showNoise(component.noiseColor)
    }

    private fun addCamera(camera: Camera) {
        Preconditions.checkArgument(
            getCamera(camera.id) == null,
            "Camera with id '" + camera.id + " is already exists!"
        )
        cameras.add(camera)
        camera.number = cameras.size - 1
    }

    fun openMenu(player: FnafUPlayer, cameraTablet: ItemStack) {
        val menu = CameraMenu(cameras, game.map.mapImage, cameraTablet)
        player.data.getOrCreateDefault(FnafUComponents.SPECTATE_CAMERA_DATA).cameraMenu = menu
        menu.open(player.player)
    }

    fun getCamera(id: String): Camera? {
        // TODO DO SOMETHING ABOUT STRING ID
        return cameras.find { camera -> camera.id == id }
    }

    fun exitAll(){
        while (!playerSpectatingCameras.isEmpty()){
            exitCamera(playerSpectatingCameras.keys.first())
        }
    }

    override fun reset() {
        super.reset()
        exitAll()
    }

    override fun setOrigin(origin: Location) {
        for (camera in cameras) camera.setOrigin(origin)
    }

    override fun destroy() {
        super.destroy()
        exitAll()
    }

}
