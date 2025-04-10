package me.udnek.fnafu.game

import me.udnek.fnafu.FnafU
import me.udnek.fnafu.player.FnafUPlayer
import me.udnek.fnafu.player.PlayerContainer
import me.udnek.itemscoreu.customminigame.command.MGUCommandContext
import me.udnek.itemscoreu.customminigame.command.MGUCommandType
import me.udnek.itemscoreu.customminigame.game.MGUAbstractGame
import me.udnek.itemscoreu.customminigame.player.MGUPlayer
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import org.jetbrains.annotations.MustBeInvokedByOverriders


abstract class FnafUAbstractGame : MGUAbstractGame(), FnafUGame {

    val playerContainer: PlayerContainer = PlayerContainer()
    private var task: BukkitRunnable? = null
    protected var winner = Winner.NONE

    @MustBeInvokedByOverriders
    open fun start(){
        state = State.RUNNING
        task = object : BukkitRunnable() {
            override fun run() { tick() }
        }
        (task as BukkitRunnable).runTaskTimer(FnafU.instance, 0, 1)
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
        state = State.WAITING
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
        if (context.args[2] == "survivors") return join(player, FnafUPlayer.PlayerType.SURVIVOR)
        return join(player, FnafUPlayer.PlayerType.ANIMATRONIC)
    }

    fun join(player: Player, playerType: FnafUPlayer.PlayerType): MGUCommandType.ExecutionResult{
        return when (playerType) {
            FnafUPlayer.PlayerType.SURVIVOR -> {
                if (playerContainer.addSurvivor(FnafUPlayer(player, FnafUPlayer.PlayerType.SURVIVOR, this)))
                    MGUCommandType.ExecutionResult.SUCCESS else MGUCommandType.ExecutionResult(MGUCommandType.ExecutionResult.Type.FAIL, "can not add")
            }
            else -> {
                if (playerContainer.addSurvivor(FnafUPlayer(player, FnafUPlayer.PlayerType.ANIMATRONIC, this)))
                    MGUCommandType.ExecutionResult.SUCCESS else MGUCommandType.ExecutionResult(MGUCommandType.ExecutionResult.Type.FAIL, "can not add")
            }
        }
    }

    override fun leave(mguPlayer: MGUPlayer, context: MGUCommandContext): MGUCommandType.ExecutionResult {
        return if (playerContainer.remove(mguPlayer.player)) MGUCommandType.ExecutionResult.SUCCESS else MGUCommandType.ExecutionResult(MGUCommandType.ExecutionResult.Type.FAIL, "can not remove")
    }

    override fun getDebug(): MutableList<Component> {
        val debug = super.getDebug()
        debug.add(Component.text("survs: ${playerContainer.getSurvivors(false)}"))
        debug.add(Component.text("anims: ${playerContainer.getAnimatronics(false)}"))
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
}















