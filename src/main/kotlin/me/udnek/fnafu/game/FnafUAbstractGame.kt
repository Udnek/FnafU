package me.udnek.fnafu.game

import me.udnek.fnafu.FnafU
import me.udnek.fnafu.map.FnafUMap
import me.udnek.fnafu.player.FnafUPlayer
import me.udnek.fnafu.player.PlayerContainer
import me.udnek.itemscoreu.custom.minigame.command.MGUCommandContext
import me.udnek.itemscoreu.custom.minigame.command.MGUCommandType
import me.udnek.itemscoreu.custom.minigame.game.MGUAbstractGame
import me.udnek.itemscoreu.custom.minigame.map.MGUMap
import me.udnek.itemscoreu.custom.minigame.player.MGUPlayer
import me.udnek.itemscoreu.customcomponent.CustomComponent
import me.udnek.itemscoreu.customcomponent.CustomComponentMap
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import org.jetbrains.annotations.MustBeInvokedByOverriders


abstract class FnafUAbstractGame(override var map: FnafUMap) : MGUAbstractGame(), FnafUGame {

    val playerContainer: PlayerContainer = PlayerContainer()
    private var task: BukkitRunnable? = null
    protected var winner: Winner

    override fun getMap(): MGUMap = map

    @MustBeInvokedByOverriders
    open fun start(){
        isRunning = true
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
        if (task == null) return MGUCommandType.ExecutionResult(MGUCommandType.ExecutionResult.Type.FAIL, "task is null")
        if (task!!.isCancelled) return MGUCommandType.ExecutionResult(MGUCommandType.ExecutionResult.Type.FAIL, "task is cancelled")
        stop()
        return MGUCommandType.ExecutionResult.SUCCESS
    }
    @MustBeInvokedByOverriders
    open fun stop(){
        task!!.cancel()
        isRunning = false
    }


    override fun testCommandArgs(context: MGUCommandContext): Boolean {
        if (context.commandType != MGUCommandType.JOIN) return super.testCommandArgs(context)
        if (context.args.size < 3) return false
        return context.args[2] == "survivors" || context.args[2] == "animatronics"
    }

    override fun getCommandOptions(context: MGUCommandContext): MutableList<String> {
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

    override fun getDebug(): MutableList<Component> {
        val debug = super.getDebug()
        debug.add(Component.text("winner: $winner"))
        debug.add(Component.text("task: $task"))
        debug.add(Component.text("task running: ${!(task?.isCancelled?:true)}"))
        return debug
    }

    override fun findNearbyPlayers(location: Location, radius: Float): List<FnafUPlayer> {
        val nearbyEntities =
            location.world.getNearbyEntities(location, radius.toDouble(), radius.toDouble(), radius.toDouble())
        val players: MutableList<FnafUPlayer> = ArrayList()
        for (nearbyEntity in nearbyEntities) {
            if (nearbyEntity !is Player) continue
            val fnafUPlayer = playerContainer.getPlayer(nearbyEntity)
            if (fnafUPlayer != null) players.add(fnafUPlayer)
        }
        return players
    }

    override fun getPlayers(): MutableList<FnafUPlayer> = ArrayList(playerContainer.all)

    enum class Winner(val color: TextColor) {
        SURVIVORS(TextColor.color(0f, 1f, 0f)),
        ANIMATRONICS(TextColor.color(1f, 0f, 0f)),
        NONE(TextColor.color(0.7f, 0.7f, 0.7f))
    }

    init {
        this.winner = Winner.NONE
    }

    private var components: CustomComponentMap<FnafUGame, CustomComponent<FnafUGame>> = CustomComponentMap()
    override fun getComponents(): CustomComponentMap<FnafUGame, CustomComponent<FnafUGame>> {
        return components
    }
}















