package me.udnek.fnafu.game

import me.udnek.coreu.custom.component.CustomComponent
import me.udnek.coreu.custom.component.CustomComponentMap
import me.udnek.coreu.mgu.command.MGUCommandContext
import me.udnek.coreu.mgu.command.MGUCommandType
import me.udnek.coreu.mgu.game.MGUAbstractGame
import me.udnek.coreu.mgu.map.MGUMap
import me.udnek.coreu.mgu.player.MGUPlayer
import me.udnek.coreu.nms.Nms
import me.udnek.fnafu.FnafU
import me.udnek.fnafu.map.FnafUMap
import me.udnek.fnafu.map.LocationType
import me.udnek.fnafu.player.FnafUPlayer
import me.udnek.fnafu.player.PlayerContainer
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import org.bukkit.Color
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.scheduler.BukkitRunnable
import org.jetbrains.annotations.MustBeInvokedByOverriders


abstract class FnafUAbstractGame(override var map: FnafUMap) : MGUAbstractGame(), FnafUGame {

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
    override fun start(context: MGUCommandContext): MGUCommandType.ExecutionResult {
        start()
        return MGUCommandType.ExecutionResult.SUCCESS
    }

    @MustBeInvokedByOverriders
    override fun stop(context: MGUCommandContext): MGUCommandType.ExecutionResult {
        stop()
        return MGUCommandType.ExecutionResult.SUCCESS
    }
    @MustBeInvokedByOverriders
    open fun stop(){
        if (task != null) task?.cancel() ?: throw RuntimeException("task is null")
        stage = FnafUGame.Stage.WAITING
    }


    override fun testCommandArgs(context: MGUCommandContext): Boolean {
        if (context.commandType != MGUCommandType.JOIN) return super.testCommandArgs(context)
        if (context.args.size < 3) return false
        return context.args[2] == "survivors" || context.args[2] == "animatronics"
    }

    override fun getCommandOptions(context: MGUCommandContext): MutableList<String> {
        if (context.commandType == MGUCommandType.DEBUG) return mutableListOf("10")
        if (context.commandType == MGUCommandType.JOIN) return mutableListOf("survivors", "animatronics")
        return super.getCommandOptions(context)
    }

    override fun join(player: Player, context: MGUCommandContext): MGUCommandType.ExecutionResult {
        if (context.args[2] == "survivors") return join(player, FnafUPlayer.Type.SURVIVOR)
        return join(player, FnafUPlayer.Type.ANIMATRONIC)
    }

    fun join(player: Player, type: FnafUPlayer.Type): MGUCommandType.ExecutionResult{
        return if (playerContainer.add(FnafUPlayer(player, type, this)))
                MGUCommandType.ExecutionResult.SUCCESS else MGUCommandType.ExecutionResult(MGUCommandType.ExecutionResult.Type.FAIL, "can not add")
    }

    override fun leave(mguPlayer: MGUPlayer, context: MGUCommandContext): MGUCommandType.ExecutionResult {
        return if (playerContainer.remove(mguPlayer.player)) {
            mguPlayer.unregister()
            MGUCommandType.ExecutionResult.SUCCESS
        } else {
            MGUCommandType.ExecutionResult(MGUCommandType.ExecutionResult.Type.FAIL, "can not remove")
        }
    }

    override fun getDebug(context: MGUCommandContext): MutableList<Component> {
        val time = context.args.getOrNull(2)?.toIntOrNull()
        if (time != null) getDebugLocation(time * 20)
        val debug = super.getDebug(context)
        debug.add(Component.text("winner: $winner"))
        debug.add(Component.text("task: $task"))
        debug.add(Component.text("task running: ${!(task?.isCancelled?:true)}"))
        return debug
    }

    fun getDebugLocation(time: Int) {
        LocationType.entries.forEach { locationType ->
            map.getLocation(locationType)?.all?.forEach {
                Nms.get().showDebugBlock(it, Color.PURPLE.asRGB(), time, "loc " + locationType.name)
            }
        }
        systems.camera.cameras.forEach {
            Nms.get().showDebugBlock(it.location.first, Color.WHITE.asRGB(), time, "cam ${it.id} (${it.number})")
        }
        systems.door.doors.forEachIndexed { index, door ->
            Nms.get().showDebugBlock(door.door.getLocation(), Color.ORANGE.asRGB(), time, "door $index")
            Nms.get().showDebugBlock(door.button.location, Color.RED.asRGB(), time, "button $index")
        }
        map.systemStations.forEach {
            Nms.get().showDebugBlock(it.first.first, Color.GREEN.asRGB(), time, "systemStation " + it.second.name)
        }
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















