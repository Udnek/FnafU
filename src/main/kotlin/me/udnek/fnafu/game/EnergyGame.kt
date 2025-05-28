package me.udnek.fnafu.game

import me.udnek.fnafu.FnafU
import me.udnek.fnafu.effect.Effects
import me.udnek.fnafu.map.FnafUMap
import me.udnek.fnafu.map.LocationType
import me.udnek.fnafu.mechanic.*
import me.udnek.fnafu.mechanic.camera.CameraSystem
import me.udnek.fnafu.mechanic.door.ButtonDoorPair
import me.udnek.fnafu.mechanic.system.Systems
import me.udnek.fnafu.player.FnafUPlayer
import me.udnek.fnafu.util.Sounds
import me.udnek.fnafu.util.toCenterFloor
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
import org.bukkit.Location
import org.bukkit.NamespacedKey
import org.bukkit.SoundCategory
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
        const val KIT_SETUP_DURATION: Long = 4 * 20
        const val MAX_LIVES: Int = 5

        const val DOOR_STUN_RADIUS: Double = 1.0
        const val DOOR_STUN_TIME: Int = 1 * 20
        const val DOOR_SLOWNESS_RADIUS: Double = 3.0
        const val DOOR_SLOWNESS_TIME: Int = 3 * 20
    }

    var kitSetupTask: BukkitRunnable? = null
    val time: Time = Time(GAME_DURATION)
    val energy: Energy = Energy(this)

    override var survivorLives = MAX_LIVES
    override val systems = Systems(AudioSystem(this), CameraSystem(this), VentilationSystem(this))

    private var timeBar: BossBar? = null
    private var energyBar: BossBar? = null

    private var teamSurvivors: Team? = null
    private var teamAnimatronics: Team? = null

    val scoreboard = CustomSidebar(id.asString(), Component.translatable("sidebar.fnafu.systems"))

    init {
        map.cameras.forEach { systems.camera.addCamera(it) }
        systems.camera.setOrigin(map.origin)
        systems.camera.setMapImage(map.cameraImage)
    }

    private fun isEveryNTicks(n: Int): Boolean {
        return time.ticks % n == 0
    }

    override fun tick() {
        time.tick()
        if (time.isEnded || !super<FnafUAbstractGame>.isRunning()) {
            winner = Winner.SURVIVORS
            stop()
            return
        }

        if (isEveryNTicks(20)) updateEnergyBar()
        if (isEveryNTicks(10)) energy.tick()
        if (isEveryNTicks(5)) updateTimeBar()
        if (isEveryNTicks(15)) {
            for (animatronic in playerContainer.getAnimatronics(false)) {
                if (animatronic.player.isSneaking && animatronic.player.walkSpeed != 0f) continue
                for (survivor in playerContainer.getSurvivors(false)) {
                    Sounds.ANIMATRONIC_STEP.play(animatronic.player.location, survivor.player)
                }
            }
        }
    }

    override fun start() {
        initializeBars()
        initializeTeams()
        val tSurvs = teamSurvivors!!
        val tAnims = teamAnimatronics!!

        baseSettingsForTeams(tSurvs)
        tSurvs.color(NamedTextColor.GREEN)
        tSurvs.prefix(Component.text("[S] ").color(TextColor.color(0f, 1f, 0f)))

        baseSettingsForTeams(tAnims)
        tAnims.color(NamedTextColor.RED)
        tAnims.prefix(Component.text("[A] ").color(TextColor.color(1f, 0f, 0f)))

        scoreboard.lines = mapOf(systems.audio.getSidebarView(), systems.ventilation.getSidebarView(), systems.camera.getSidebarView())
        updateSurvivorLives()

        for (player in playerContainer.getPlayers(false)) {
            player.reset()

            player.player.totalExperience = 0
            player.player.addPotionEffect(PotionEffect(PotionEffectType.SATURATION, PotionEffect.INFINITE_DURATION, 10, false, false, false))
            player.player.getAttribute(Attribute.JUMP_STRENGTH)!!.addModifier(
                AttributeModifier(NamespacedKey(FnafU.instance, "game_js"), -10.0, AttributeModifier.Operation.ADD_NUMBER))

            when (player.type) {
                FnafUPlayer.Type.SURVIVOR -> {
                    player.teleport(map.getLocation(LocationType.PICK_STAGE_SPAWN_SURVIVOR)!!)
                    tSurvs.addPlayer(player.player)
                }
                FnafUPlayer.Type.ANIMATRONIC -> {
                    player.teleport(map.getLocation(LocationType.PICK_STAGE_SPAWN_ANIMATRONIC)!!)
                    tAnims.addPlayer(player.player)
                }
            }
            KitMenu().open(player.player)
        }

        stage = FnafUGame.Stage.KIT

        kitSetupTask = object : BukkitRunnable() { override fun run() { gameStart() } }
        kitSetupTask!!.runTaskLater(FnafU.instance, KIT_SETUP_DURATION)
    }

    fun gameStart(){
        super.start()
        time.reset()
        energy.reset()
        map.reset()
        energy.updateConsumption()
        winner = Winner.NONE

        showBossBarToAll(energyBar!!)
        updateEnergyBar()
        showBossBarToAll(timeBar!!)
        updateTimeBar()

        for (player in playerContainer.getPlayers(false)) {
            when (player.type) {
                FnafUPlayer.Type.SURVIVOR -> { player.teleport(map.getLocation(LocationType.SPAWN_SURVIVOR)!!) }
                FnafUPlayer.Type.ANIMATRONIC -> { player.teleport(map.getLocation(LocationType.SPAWN_ANIMATRONIC)!!) }
            }
            map.ambientSound.play(player.player)
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
        energyBar = BossBar.bossBar(Component.text(""), BossBar.MAX_PROGRESS, BossBar.Color.WHITE, BossBar.Overlay.PROGRESS)
        energyBar!!.addFlag(BossBar.Flag.CREATE_WORLD_FOG)
        timeBar = BossBar.bossBar(Component.text(""), BossBar.MIN_PROGRESS, BossBar.Color.YELLOW, BossBar.Overlay.PROGRESS)
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
        energy.reset()
        systems.reset()

        for (fnafUPlayer in players) {
            map.ambientSound.stop(fnafUPlayer.player, SoundCategory.AMBIENT)
            fnafUPlayer.player.closeInventory()
            scoreboard.hide(fnafUPlayer.player)
            fnafUPlayer.showTitle(Component.text(winner.toString()).color(winner.color), Component.empty(), 10, 40, 10)
            fnafUPlayer.reset()
        }

        survivorLives = MAX_LIVES
    }

    override fun onPlayerDamagePlayer(event: EntityDamageByEntityEvent, damager: FnafUPlayer, victim: FnafUPlayer){
        if (damager.type == FnafUPlayer.Type.SURVIVOR) {
            event.isCancelled = true
            return
        }

        if (survivorLives == 0) {
            if (hasLivingSurvivors()) return
            winner = Winner.ANIMATRONICS
            stop()
            return
        }

        victim.damage()
        updateSurvivorLives()
    }

    fun hasLivingSurvivors(): Boolean {
        players.forEach { if (it.isAlive and (it.type == FnafUPlayer.Type.SURVIVOR)) return true }
        return false
    }

    override fun onPlayerClicksDoorButton(event: PlayerInteractEvent, player: MGUPlayer, button: ButtonDoorPair) {
        val door = button.door
        stunAnimatronicsAround(door.getLocation().toCenterFloor())
        door.toggle()
        energy.updateConsumption()
    }

    private fun stunAnimatronicsAround(location: Location){
        findNearbyPlayers(location, DOOR_SLOWNESS_RADIUS, FnafUPlayer.Type.ANIMATRONIC).forEach {
            it.player.addPotionEffect(PotionEffect(PotionEffectType.SLOWNESS, DOOR_SLOWNESS_TIME, 1, true, false, true))
        }
        findNearbyPlayers(location, DOOR_STUN_RADIUS, FnafUPlayer.Type.ANIMATRONIC).forEach {
            Effects.STUN_EFFECT.applyInvisible(it.player, DOOR_STUN_TIME, 0)
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
        energyBar!!.name(
            Component.translatable("energy.fnafu.bossbar",
                Component.text(Utils.roundToTwoDigits(energy.energy.toDouble())),
                Component.translatable("energy.fnafu.image.${energy.usage}").font(Key.key("fnafu:energy")))
            .decoration(TextDecoration.BOLD, true)
        )
        energyBar!!.progress(energy.energy / Energy.MAX_ENERGY)
    }

    private fun updateTimeBar() {
        timeBar!!.name(Component.translatable("time.fnafu.${(6 * time.ticks / GAME_DURATION )}")
            .append(Component.text(" (${(GAME_DURATION - time.ticks) / 20})"))
            .decoration(TextDecoration.BOLD, true))
    }

    override fun updateSurvivorLives() {
        scoreboard.setLine(0, Component.translatable("sidebar.fnafu.live_count", Component.text(survivorLives)))
        scoreboard.updateForAll()
    }
}
