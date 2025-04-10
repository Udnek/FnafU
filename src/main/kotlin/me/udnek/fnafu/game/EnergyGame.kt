package me.udnek.fnafu.game

import me.udnek.fnafu.component.Kit
import me.udnek.fnafu.map.FnafUMap
import me.udnek.fnafu.map.LocationType
import me.udnek.fnafu.mechanic.Energy
import me.udnek.fnafu.mechanic.Time
import me.udnek.fnafu.player.FnafUPlayer
import me.udnek.itemscoreu.customminigame.game.MGUGameType
import net.kyori.adventure.bossbar.BossBar
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor
import org.bukkit.Bukkit
import org.bukkit.Color
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.scoreboard.Team

class EnergyGame : FnafUAbstractGame {
    companion object {
        const val GAME_DURATION: Int = 60 * 20
    }

    override val map: FnafUMap

    val time: Time
    val energy: Energy

    private var timeBar: BossBar? = null
    private var energyBar: BossBar? = null

    private var teamSurvivors: Team? = null
    private var teamAnimatronics: Team? = null

    constructor(map: FnafUMap) {
        this.map = map
        time = Time(GAME_DURATION)
        energy = Energy(this)
    }

    private fun isEveryNTicks(n: Int): Boolean {
        return time.ticks % n == 0
    }

    override fun tick() {
        time.tick()
        if (time.isEnded || state == State.WAITING) {
            winner = Winner.SURVIVORS
            stop()
            return
        }

        if (isEveryNTicks(20)) updateEnergyBar()
        if (isEveryNTicks(10)) energy.tick()
        if (isEveryNTicks(5)) updateTimeBar()
    }

    override fun start() {
        super.start()
        time.reset()
        energy.reset()
        winner = Winner.NONE

        energyBar =
            BossBar.bossBar(Component.text(""), BossBar.MAX_PROGRESS, BossBar.Color.YELLOW, BossBar.Overlay.PROGRESS)
        showBossBarToAll(energyBar!!)
        timeBar =
            BossBar.bossBar(Component.text(""), BossBar.MAX_PROGRESS, BossBar.Color.WHITE, BossBar.Overlay.PROGRESS)
        showBossBarToAll(timeBar!!)

        initializeTeams()
        val tSurvs = teamSurvivors!!
        val tAnims = teamAnimatronics!!

        for (survivor in playerContainer.getSurvivors(false)) {
            survivor.reset()
            survivor.teleport(map.getLocation(LocationType.SPAWN_SURVIVOR)!!)
            tSurvs.addPlayer(survivor.player)
            survivor.kit = Kit.CAMERAMAN
            survivor.setUp()
            survivor.showAuraTo(playerContainer.all, 0, Color.RED)
        }
        for (animatronic in playerContainer.getAnimatronics(false)) {
            animatronic.reset()
            animatronic.teleport(map.getLocation(LocationType.SPAWN_ANIMATRONIC)!!)
            tAnims.addPlayer(animatronic.player)
            animatronic.kit = Kit.SPRINGTRAP
            animatronic.setUp()
            animatronic.showAuraTo(playerContainer.all, 0, Color.GREEN)
        }

        tSurvs.setAllowFriendlyFire(false)
        tSurvs.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.FOR_OWN_TEAM)
        tSurvs.setCanSeeFriendlyInvisibles(true)
        tSurvs.color(NamedTextColor.GREEN)
        tSurvs.prefix(Component.text("[S] ").color(TextColor.color(0f, 1f, 0f)))

        tAnims.setAllowFriendlyFire(false)
        tAnims.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.FOR_OWN_TEAM)
        tAnims.setCanSeeFriendlyInvisibles(true)
        tAnims.color(NamedTextColor.RED)
        tAnims.prefix(Component.text("[A] ").color(TextColor.color(1f, 0f, 0f)))
    }

    private fun initializeTeams() {
        val scoreboard = Bukkit.getScoreboardManager().mainScoreboard
        val survivorsTeamName = "survs_${id.asString()}"
        val animatronicsTeamName = "anims_${id.asString()}"
        teamSurvivors = scoreboard.getTeam(survivorsTeamName) ?: scoreboard.registerNewTeam(survivorsTeamName)
        teamAnimatronics = scoreboard.getTeam(animatronicsTeamName) ?: scoreboard.registerNewTeam(animatronicsTeamName)
    }

    override fun stop(){
        super.stop()
        teamAnimatronics!!.unregister()
        teamSurvivors!!.unregister()
        removeBossBar(energyBar!!)
        removeBossBar(timeBar!!)
        energyBar = null
        timeBar = null

        map.reset()

        for (fnafUPlayer in players) {
            fnafUPlayer.showTitle(Component.text(winner.toString()).color(winner.color), Component.empty(), 10, 40, 10)
            fnafUPlayer.reset()
        }
    }

    override fun onPlayerDamagePlayer(event: EntityDamageByEntityEvent, damager: FnafUPlayer, victim: FnafUPlayer){
        if (damager.type == FnafUPlayer.PlayerType.SURVIVOR) {
            event.isCancelled = true
            return
        }
    }

    override fun getType(): MGUGameType = GameTypes.ENERGY

    private fun removeBossBar(bossBar: BossBar) {
        for (fnafUPlayer in players) bossBar.removeViewer(fnafUPlayer.player)
    }

    private fun showBossBarToAll(bossBar: BossBar) {
        for (fnafUPlayer in players) bossBar.addViewer(fnafUPlayer.player)
    }

    private fun updateEnergyBar() {
        energyBar!!.name(Component.text("Energy: ${energy.energy} Usage: ${energy.usage} (Consumption: ${energy.consumption})"))
    }

    private fun updateTimeBar() {
        timeBar!!.name(Component.text("${time.ticks / 20f} / ${GAME_DURATION / 20}"))
    }
}
