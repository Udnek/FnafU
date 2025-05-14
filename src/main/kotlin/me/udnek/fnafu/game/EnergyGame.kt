package me.udnek.fnafu.game

import me.udnek.fnafu.FnafU
import me.udnek.fnafu.map.FnafUMap
import me.udnek.fnafu.map.LocationType
import me.udnek.fnafu.map.location.LocationList
import me.udnek.fnafu.mechanic.*
import me.udnek.fnafu.mechanic.camera.CameraSystem
import me.udnek.fnafu.mechanic.door.ButtonDoorPair
import me.udnek.fnafu.mechanic.system.Systems
import me.udnek.fnafu.player.FnafUPlayer
import me.udnek.itemscoreu.custom.minigame.game.MGUGameType
import me.udnek.itemscoreu.custom.minigame.player.MGUPlayer
import me.udnek.itemscoreu.custom.sidebar.CustomSidebar
import me.udnek.itemscoreu.util.Utils
import net.kyori.adventure.bossbar.BossBar
import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Bukkit
import org.bukkit.NamespacedKey
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scoreboard.Team

class EnergyGame(map: FnafUMap) : FnafUAbstractGame(map) {
    companion object {
        const val GAME_DURATION: Int = 60 * 20
        const val KIT_SETUP_DURATION: Long = 10 * 20
    }

    var survivorLives = 5
    var kitSetupTask: BukkitRunnable? = null
    val time: Time = Time(GAME_DURATION)
    val energy: Energy = Energy(this)

    override var audioSystem = AudioSystem(this)
    override var cameraSystem = CameraSystem(this)
    override var ventilationSystem = VentilationSystem(this)
    override var systems = Systems(audioSystem, cameraSystem, ventilationSystem)

    private var timeBar: BossBar? = null
    private var energyBar: BossBar? = null

    private var teamSurvivors: Team? = null
    private var teamAnimatronics: Team? = null

    val scoreboard = CustomSidebar(id.asString(), Component.translatable("sidebar.fnafu.systems"))

    init {
        map.cameras.forEach { cameraSystem.addCamera(it) }
        cameraSystem.setOrigin(map.origin)
        cameraSystem.setMapImage(map.cameraImage)
    }

    private fun isEveryNTicks(n: Int): Boolean {
        return time.ticks % n == 0
    }

    override fun tick() {
        time.tick()
        if (time.isEnded || !isRunning) {
            winner = Winner.SURVIVORS
            stop()
            return
        }

        if (isEveryNTicks(20)) updateEnergyBar()
        if (isEveryNTicks(10)) energy.tick()
        if (isEveryNTicks(5)) updateTimeBar()
    }

    override fun start() {
        initializeTeams()
        val tSurvs = teamSurvivors!!
        val tAnims = teamAnimatronics!!

        baseSettingsForTeams(tSurvs)
        tSurvs.color(NamedTextColor.GREEN)
        tSurvs.prefix(Component.text("[S] ").color(TextColor.color(0f, 1f, 0f)))

        baseSettingsForTeams(tAnims)
        tAnims.color(NamedTextColor.RED)
        tAnims.prefix(Component.text("[A] ").color(TextColor.color(1f, 0f, 0f)))

        for (player in playerContainer.getPlayers(false)) {
            player.reset()

            player.player.addPotionEffect(PotionEffect(PotionEffectType.SATURATION, PotionEffect.INFINITE_DURATION, 10, false, false, false))
            player.player.getAttribute(Attribute.JUMP_STRENGTH)!!.addModifier(
                AttributeModifier(NamespacedKey(FnafU.instance, "game_js"), -10.0, AttributeModifier.Operation.ADD_NUMBER))

            when (player.type) {
                FnafUPlayer.Type.SURVIVOR -> {
                    player.teleport(map.getLocation(LocationType.SPAWN_SURVIVOR)!!)
                    tSurvs.addPlayer(player.player)
                }
                FnafUPlayer.Type.ANIMATRONIC -> {
                    player.teleport(map.getLocation(LocationType.SPAWN_ANIMATRONIC)!!)
                    tAnims.addPlayer(player.player)
                }
            }
            KitMenu().open(player.player)
        }

        isRunning = true

        kitSetupTask = object : BukkitRunnable() { override fun run() { gameStart() } }
        kitSetupTask!!.runTaskLater(FnafU.instance, KIT_SETUP_DURATION)
    }

    fun gameStart(){
        super.start()
        time.reset()
        energy.reset()
        winner = Winner.NONE

        scoreboard.lines = mapOf(audioSystem.getSidebarView(), ventilationSystem.getSidebarView(), cameraSystem.getSidebarView())
        updateSurvivorLives()

        initializeBars()

        for (player in playerContainer.getPlayers(false)) {
            when (player.type) {
                FnafUPlayer.Type.SURVIVOR -> { player.teleport(map.getLocation(LocationType.SPAWN_SURVIVOR)!!) }
                FnafUPlayer.Type.ANIMATRONIC -> { player.teleport(map.getLocation(LocationType.SPAWN_ANIMATRONIC)!!) }
            }
            scoreboard.show(player.player)
            player.setUp()
        }
    }

    private fun baseSettingsForTeams(team: Team) {
        team.setAllowFriendlyFire(false)
        team.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.FOR_OTHER_TEAMS)
        team.setCanSeeFriendlyInvisibles(true)
    }

    private fun initializeTeams() {
        val scoreboard = Bukkit.getScoreboardManager().mainScoreboard
        val survivorsTeamName = "survs_${id.asString()}"
        val animatronicsTeamName = "anims_${id.asString()}"
        teamSurvivors = scoreboard.getTeam(survivorsTeamName) ?: scoreboard.registerNewTeam(survivorsTeamName)
        teamAnimatronics = scoreboard.getTeam(animatronicsTeamName) ?: scoreboard.registerNewTeam(animatronicsTeamName)
    }

    private fun initializeBars() {
        energyBar =
            BossBar.bossBar(Component.text(""), BossBar.MAX_PROGRESS, BossBar.Color.WHITE, BossBar.Overlay.PROGRESS)
        showBossBarToAll(energyBar!!)
        updateEnergyBar()
        timeBar =
            BossBar.bossBar(Component.text(""), BossBar.MIN_PROGRESS, BossBar.Color.YELLOW, BossBar.Overlay.PROGRESS)
        showBossBarToAll(timeBar!!)
        updateTimeBar()
    }

    override fun stop(){
        super.stop()
        teamAnimatronics!!.unregister()
        teamSurvivors!!.unregister()
        removeBossBar(energyBar!!)
        removeBossBar(timeBar!!)

        kitSetupTask!!.cancel()
        energyBar = null
        timeBar = null


        map.reset()
        systems.reset()
        ventilationSystem.reset()
        audioSystem.reset()

        for (fnafUPlayer in players) {
            scoreboard.hide(fnafUPlayer.player)
            fnafUPlayer.showTitle(Component.text(winner.toString()).color(winner.color), Component.empty(), 10, 40, 10)
            fnafUPlayer.reset()
        }

        survivorLives = 5
    }

    override fun onPlayerDamagePlayer(event: EntityDamageByEntityEvent, damager: FnafUPlayer, victim: FnafUPlayer){
        if (damager.type == FnafUPlayer.Type.SURVIVOR) {
            event.isCancelled = true
            return
        }

        if (survivorLives == 0) {
            victim.death()
            if (hasLivingSurvivors()) return
            winner = Winner.ANIMATRONICS
            stop()
            return
        }

        survivorLives -= 1
        victim.damage(map.getLocation(LocationType.RESPAWN_SURVIVOR)!! as LocationList)

        updateSurvivorLives()
        scoreboard.updateForAll()
    }

    fun hasLivingSurvivors(): Boolean {
        players.forEach { if (it.isAlive and (it.type == FnafUPlayer.Type.SURVIVOR)) return true }
        return false
    }

    override fun onPlayerClicksDoorButton(event: PlayerInteractEvent, player: MGUPlayer, button: ButtonDoorPair) {
        button.door.toggle()
        energy.updateConsumption()
    }

    override fun getType(): MGUGameType = GameTypes.ENERGY

    private fun removeBossBar(bossBar: BossBar) {
        for (fnafUPlayer in players) bossBar.removeViewer(fnafUPlayer.player)
    }

    private fun showBossBarToAll(bossBar: BossBar) {
        for (fnafUPlayer in players) bossBar.addViewer(fnafUPlayer.player)
    }

    private fun updateEnergyBar() {
        energyBar!!.name(Component.translatable("energy.fnafu.left")
            .append(Component.text(": ${Utils.roundToTwoDigits(energy.energy.toDouble())}% "))
            .append(Component.translatable("energy.fnafu.usage"))
            .decoration(TextDecoration.BOLD, true)
            .append(Component.translatable("energy.fnafu.image.${energy.usage}").font(Key.key("fnafu:energy")))
        )
        energyBar!!.progress(energy.energy / Energy.MAX_ENERGY)
    }

    private fun updateTimeBar() {
        timeBar!!.name(Component.translatable("time.fnafu.${(6 * time.ticks / GAME_DURATION )}")
            .append(Component.text(" (${(GAME_DURATION - time.ticks) / 20})"))
            .decoration(TextDecoration.BOLD, true))
    }

    private fun updateSurvivorLives() {
        scoreboard.setLine(0, Component.translatable("sidebar.fnafu.live_count", Component.text(survivorLives)))
    }
}
