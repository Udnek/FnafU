package me.udnek.fnafu.game

import me.udnek.coreu.custom.component.CustomComponent
import me.udnek.coreu.custom.component.CustomComponentMap
import me.udnek.coreu.mgu.command.MGUCommandContext
import me.udnek.coreu.mgu.command.MGUCommandType
import me.udnek.coreu.mgu.command.MGUCommandType.ExecutionResult
import me.udnek.coreu.mgu.command.MGUCommandType.ExecutionResult.Type
import me.udnek.coreu.mgu.game.MGUAbstractGame
import me.udnek.coreu.mgu.map.MGUMap
import me.udnek.coreu.mgu.player.MGUPlayer
import me.udnek.coreu.nms.Nms
import me.udnek.fnafu.FnafU
import me.udnek.fnafu.map.LocationType
import me.udnek.fnafu.player.FnafUPlayer
import me.udnek.fnafu.player.PlayerContainer
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import org.bukkit.Color
import org.bukkit.Location
import org.bukkit.Particle
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.scheduler.BukkitRunnable
import org.jetbrains.annotations.MustBeInvokedByOverriders


abstract class FnafUAbstractGame() : MGUAbstractGame(), FnafUGame {

    override val playerContainer: PlayerContainer = PlayerContainer()
    private var task: BukkitRunnable? = null
    protected var winner: Winner = Winner.NONE
    override var stage: FnafUGame.Stage = FnafUGame.Stage.WAITING
    override fun isRunning(): Boolean { return super<FnafUGame>.isRunning() }

    override fun getMap(): MGUMap = map

    @MustBeInvokedByOverriders
    open fun start(){
        stage = FnafUGame.Stage.RUNNING
        task = object : BukkitRunnable() {
            override fun run() { tick() }
        }
        task!!.runTaskTimer(FnafU.instance, 0, 1)
    }
    @MustBeInvokedByOverriders
    override fun start(context: MGUCommandContext): ExecutionResult {
        start()
        return ExecutionResult.SUCCESS
    }

    @MustBeInvokedByOverriders
    override fun stop(context: MGUCommandContext): ExecutionResult {
        stop()
        return ExecutionResult.SUCCESS
    }
    @MustBeInvokedByOverriders
    override fun stop(){
        if (task != null) task?.cancel() ?: throw RuntimeException("task is null")
        stage = FnafUGame.Stage.WAITING
    }


    override fun testCommandArgs(context: MGUCommandContext): Boolean {
        if (context.commandType != MGUCommandType.JOIN) return super.testCommandArgs(context)
        if (context.args.size < 3) return false
        return context.args[2] == "survivors" || context.args[2] == "animatronics"
    }

    override fun getCommandOptions(context: MGUCommandContext): MutableList<String> {
        when (context.commandType) {
            MGUCommandType.DEBUG -> return mutableListOf("10")
            MGUCommandType.JOIN -> return mutableListOf("survivors", "animatronics")
            MGUCommandType.EXECUTE -> {
                if (context.args.size == 3) return mutableListOf("setEnergy", "setTime", "breakSys")
                if (context.args[2].equals("breakSys", true)) {
                    return systems.all.map { s -> s.javaClass.simpleName.toString() }.toMutableList()
                }
                return mutableListOf("3")
            }
            else -> return super.getCommandOptions(context)
        }
    }

    override fun execute(context: MGUCommandContext): ExecutionResult {
        if (context.args.size < 4) return ExecutionResult(Type.FAIL, "not enough args")
        if (context.args[2].equals("setEnergy", true)) {
            val value = context.args[3].toFloatOrNull() ?: return ExecutionResult(Type.FAIL, "incorrect float: ${context.args[3]}")
            energy.energy = value
            return ExecutionResult.SUCCESS
        } else if (context.args[2].equals("setTime", true)){
            val value = context.args[3].toIntOrNull() ?: return ExecutionResult(Type.FAIL, "incorrect int: ${context.args[3]}")
            time.ticks = time.maxTime - value
            return ExecutionResult.SUCCESS
        } else if (context.args[2].equals("breakSys", true)){
            val value = context.args.getOrNull(3) ?: return ExecutionResult(Type.FAIL, "incorrect sys: ${context.args[3]}")
            systems.all.forEach {
                if (it.javaClass.simpleName.equals(value, true)) {
                    it.destroy()
                    return ExecutionResult.SUCCESS
                }
            }
            return ExecutionResult(Type.FAIL, "incorrect sys: ${context.args[3]}")
        } else{
            return ExecutionResult(Type.FAIL, "unknown arg: ${context.args[2]}")
        }
    }

    override fun join(player: Player, context: MGUCommandContext): ExecutionResult {
        if (context.args[2] == "survivors") return join(player, FnafUPlayer.Type.SURVIVOR)
        return join(player, FnafUPlayer.Type.ANIMATRONIC)
    }

    fun join(player: Player, type: FnafUPlayer.Type): ExecutionResult{
        return if (playerContainer.add(FnafUPlayer(player, type, this)))
                ExecutionResult.SUCCESS else ExecutionResult(Type.FAIL, "can not add")
    }

    override fun leave(mguPlayer: MGUPlayer, context: MGUCommandContext): ExecutionResult {
        return if (playerContainer.remove(mguPlayer.player)) {
            mguPlayer.unregister()
            ExecutionResult.SUCCESS
        } else {
            ExecutionResult(Type.FAIL, "can not remove")
        }
    }

    override fun getDebug(context: MGUCommandContext): MutableList<Component> {
        val time = context.args.getOrNull(2)?.toIntOrNull()
        if (time != null) showDebugLocations(time * 20)
        val debug = super.getDebug(context)
        debug.add(Component.text("winner: $winner"))
        debug.add(Component.text("task: $task"))
        debug.add(Component.text("task running: ${!(task?.isCancelled?:true)}"))
        return debug
    }

    private fun showDebugPoint(location: Location, color: Color, duration: Int){
        Particle.TRAIL.builder()
            .location(location)
            .offset(0.0, 0.0, 0.0)
            .data(Particle.Trail(location, color, duration))
            .spawn()
    }

    fun showDebugLocations(duration: Int) {


        LocationType.entries.forEach { locationType ->
            map.getLocation(locationType)?.all?.forEach {
                Nms.get().showDebugBlock(it, Color.PURPLE.asRGB(), duration, "loc " + locationType.name)
            }
        }
        systems.camera.cameras.forEach {
            Nms.get().showDebugBlock(it.location.first, Color.WHITE.asRGB(), duration, "cam ${it.id} (${it.number})")
        }
        systems.door.doors.forEachIndexed { index, doorButtonPair ->
            doorButtonPair.button.locationData.all.forEach {
                Nms.get().showDebugBlock(it, Color.RED.asRGB(), duration, "button $index")
            }
            Nms.get().showDebugBlock(doorButtonPair.door.debugLocation, Color.ORANGE.asRGB(), duration, "door $index")
            showDebugPoint(doorButtonPair.door.stunCenter, Color.RED, duration)
        }
        systems.ventilation.vents.forEachIndexed { index, vent ->
            Nms.get().showDebugBlock(vent.debugLocation, Color.BLUE.asRGB(), duration, "vent $index")
            showDebugPoint(vent.stunCenter, Color.RED, duration)
        }
        map.systemStations.forEach {
            Nms.get().showDebugBlock(it.first.first, Color.GREEN.asRGB(), duration, "systemStation ${it.second.name}")
        }
        Nms.get().showDebugBlock(map.minBound.first, Color.OLIVE.asRGB(), duration, "minBound")
        Nms.get().showDebugBlock(map.maxBound.first, Color.OLIVE.asRGB(), duration, "maxBound")

        map.mapLight.cachedLightPoses().keys.forEach {
            Nms.get().showDebugBlock(it, Color.YELLOW.asRGB(), duration, "light")
        }
        Nms.get().showDebugBlock(map.origin, Color.SILVER.asRGB(), duration, "origin")
    }

    override fun findNearbyPlayers(location: Location, radius: Double, playerType: FnafUPlayer.Type?): List<FnafUPlayer>{
        val nearbyEntities = location.world.getNearbyEntities(location, radius, radius, radius)
        val players: MutableList<FnafUPlayer> = ArrayList()
        for (nearbyEntity in nearbyEntities) {
            if (nearbyEntity !is Player) continue
            val fnafUPlayer = playerContainer.getPlayer(nearbyEntity) ?: continue
            if (playerType == null || fnafUPlayer.type == playerType) players.add(fnafUPlayer)
        }
        return players
    }

    override fun onPlayerLeave(event: PlayerQuitEvent, player: FnafUPlayer) {
        /*player.createNPC()
    */}

    override fun getPlayers(): MutableList<FnafUPlayer> = ArrayList(playerContainer.all)

    enum class Winner(val color: TextColor) {
        SURVIVORS(TextColor.color(0f, 1f, 0f)),
        ANIMATRONICS(TextColor.color(1f, 0f, 0f)),
        NONE(TextColor.color(0.7f, 0.7f, 0.7f))
    }

    private var components: CustomComponentMap<FnafUGame, CustomComponent<FnafUGame>> = CustomComponentMap()
    override fun getComponents(): CustomComponentMap<FnafUGame, CustomComponent<FnafUGame>> {
        return components
    }
}















