package me.udnek.fnafu.game

import me.udnek.coreu.custom.item.CustomItem
import me.udnek.coreu.custom.sidebar.CustomSidebar
import me.udnek.coreu.mgu.game.MGUGameType
import me.udnek.coreu.mgu.player.MGUPlayer
import me.udnek.coreu.rpgu.component.RPGUActiveAbilityItem
import me.udnek.coreu.rpgu.component.RPGUComponents
import me.udnek.coreu.util.Utils
import me.udnek.fnafu.FnafU
import me.udnek.fnafu.component.FnafUComponents
import me.udnek.fnafu.effect.Effects
import me.udnek.fnafu.map.FnafUMap
import me.udnek.fnafu.map.LocationType
import me.udnek.fnafu.map.location.LocationSingle
import me.udnek.fnafu.mechanic.Energy
import me.udnek.fnafu.mechanic.KitMenu
import me.udnek.fnafu.mechanic.Time
import me.udnek.fnafu.mechanic.system.Systems
import me.udnek.fnafu.mechanic.system.camera.CameraSystem
import me.udnek.fnafu.mechanic.system.door.ButtonDoorPair
import me.udnek.fnafu.mechanic.system.door.DoorSystem
import me.udnek.fnafu.mechanic.system.ventilation.VentilationSystem
import me.udnek.fnafu.player.FnafUPlayer
import me.udnek.fnafu.util.Resettable
import me.udnek.fnafu.util.Sounds
import net.kyori.adventure.bossbar.BossBar
import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.*
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.block.BlockFace
import org.bukkit.block.data.Directional
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scoreboard.Team

class EnergyGame(map: FnafUMap) : FnafUAbstractGame(map), Resettable {
    companion object {
        const val GAME_DURATION: Int = 5 * 60 * 20
        const val KIT_SETUP_DURATION: Long = 4 * 20
        const val ANIMATRONIC_WAITING_DURATION: Long = 8 * 20
        const val MAX_LIVES: Int = 5

        const val DOOR_STUN_RADIUS: Double = 1.0
        const val DOOR_STUN_TIME: Int = 1 * 20
        const val DOOR_SLOWNESS_RADIUS: Double = 3.0
        const val DOOR_SLOWNESS_TIME: Int = 3 * 20
    }

    var kitSetupTask: BukkitRunnable? = null
    var animatronicWaitingTask: BukkitRunnable? = null
    val time: Time = Time(GAME_DURATION)
    override val energy: Energy = Energy(this)

    override var survivorLives = MAX_LIVES
    override val systems: Systems = Systems(DoorSystem(this, map.doors), CameraSystem(this), VentilationSystem(this))

    private var timeBar: BossBar? = null
    private var energyBar: BossBar? = null

    private var teamSurvivors: Team? = null
    private var teamAnimatronics: Team? = null

    override val scoreboard = CustomSidebar(id.asString(), Component.translatable("sidebar.fnafu.systems"))

    init {
        map.cameras.forEach { systems.camera.addCamera(it) }
        systems.camera.setOrigin(map.origin)
    }

    private fun isEveryNTicks(n: Int): Boolean = time.ticks % n == 0

    override fun tick() {
        time.tick()
        if (time.isEnded || !super<FnafUAbstractGame>.isRunning()) {
            winner = Winner.SURVIVORS
            stop()
            return
        }

        if (isEveryNTicks(20)) updateEnergyBar()
        if (isEveryNTicks(10)) {
            systems.tick()
            energy.tick()
        }
        if (isEveryNTicks(5)) updateTimeBar()
        if (isEveryNTicks(15)) {
            for (animatronic in playerContainer.getAnimatronics(false)) {
                val movement = animatronic.data.getOrCreateDefault(FnafUComponents.MOVEMENT_TRACKER_DATA)
                if (!animatronic.player.isSneaking && movement.hasMoved(animatronic.player.location)) {
                    for (survivor in playerContainer.getSurvivors(false)) {
                        Sounds.ANIMATRONIC_STEP.play(animatronic.player.location, survivor.player)
                    }
                }
                movement.lastLocation = animatronic.player.location

            }
        }
    }

    override fun start() {
        reset()
        initializeBars()
        initializeTeams()

        scoreboard.lines = systems.all.associate { system -> system.getSidebarView() }

        updateSurvivorLives()
        chooseSystemStations()

        for (player in playerContainer.getPlayers(false)) {
            player.reset()

            player.player.exp = 0f
            player.player.level = 0
            player.player.gameMode = GameMode.ADVENTURE
            player.player.addPotionEffect(PotionEffect(PotionEffectType.SATURATION, PotionEffect.INFINITE_DURATION, 10, false, false, false))
            player.player.getAttribute(Attribute.JUMP_STRENGTH)!!.addModifier(
                AttributeModifier(NamespacedKey(FnafU.instance, "game_js"), -10.0, AttributeModifier.Operation.ADD_NUMBER))

            when (player.type) {
                FnafUPlayer.Type.SURVIVOR -> {
                    player.teleport(map.getLocation(LocationType.PICK_STAGE_SPAWN_SURVIVOR)!!)
                    teamSurvivors!!.addPlayer(player.player)
                }
                FnafUPlayer.Type.ANIMATRONIC -> {
                    player.teleport(map.getLocation(LocationType.PICK_STAGE_SPAWN_ANIMATRONIC)!!)
                    teamAnimatronics!!.addPlayer(player.player)
                }
            }
            KitMenu().open(player.player)
        }

        stage = FnafUGame.Stage.KIT

        kitSetupTask = object : BukkitRunnable() { override fun run() { mainCycleStart() } }
        kitSetupTask!!.runTaskLater(FnafU.instance, KIT_SETUP_DURATION)
    }

    fun mainCycleStart(){
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
                FnafUPlayer.Type.SURVIVOR -> player.teleport(map.getLocation(LocationType.SPAWN_SURVIVOR)!!)
                FnafUPlayer.Type.ANIMATRONIC -> player.teleport(map.getLocation(LocationType.PRESPAWN_ANIMATRONIC)!!)
            }
            map.ambientSound.play(player.player)
            scoreboard.show(player.player)
            player.setUp()
        }

        animatronicWaitingTask = object : BukkitRunnable() { override fun run() {
            playerContainer.getAnimatronics(false).forEach { it.teleport(map.getLocation(LocationType.SPAWN_ANIMATRONIC)!!, NamedTextColor.RED) }
        } }
        animatronicWaitingTask!!.runTaskLater(FnafU.instance, ANIMATRONIC_WAITING_DURATION)
    }

    private fun chooseSystemStations() {
        val systemStations = ArrayList(map.systemStations)
        for (systemStation in systemStations) {
            val location = systemStation.first.first
            location.block.type = Material.AIR
            location.add(0.0, -1.0, 0.0).block.type = Material.AIR
        }
        for (i in 1 .. map.systemStationsAmount step 2) {
            val systemStation: Pair<LocationSingle, BlockFace> = systemStations.random()
            placeSystemStation(systemStations, systemStation)
            if (i >= map.systemStationsAmount) break
            val farthestSystemStation = systemStations.maxByOrNull { pair -> pair.first.first.distance(systemStation.first.first) }!!
            placeSystemStation(systemStations, farthestSystemStation)
        }
    }

    private fun placeSystemStation(systemStations: ArrayList<Pair<LocationSingle, BlockFace>>, pair: Pair<LocationSingle, BlockFace>) {
        val location = pair.first.first
        val world = location.world
        val station = Material.BLUE_GLAZED_TERRACOTTA.createBlockData() as Directional
        station.facing = pair.second

        world.setBlockData(location, station)
        world.setBlockData(location.add(0.0, -1.0, 0.0), Material.BARRIER.createBlockData())
        systemStations.remove(pair)
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

        baseSettingsForTeams(teamSurvivors!!)
        teamSurvivors!!.color(NamedTextColor.GREEN)
        teamSurvivors!!.prefix(Component.text("[S] ").color(TextColor.color(0f, 1f, 0f)))

        baseSettingsForTeams(teamAnimatronics!!)
        teamAnimatronics!!.color(NamedTextColor.RED)
        teamAnimatronics!!.prefix(Component.text("[A] ").color(TextColor.color(1f, 0f, 0f)))
    }

    private fun initializeBars() {
        energyBar = BossBar.bossBar(Component.text(""), BossBar.MAX_PROGRESS, BossBar.Color.WHITE, BossBar.Overlay.PROGRESS)
        energyBar!!.addFlag(BossBar.Flag.CREATE_WORLD_FOG)
        timeBar = BossBar.bossBar(Component.text(""), BossBar.MIN_PROGRESS, BossBar.Color.YELLOW, BossBar.Overlay.PROGRESS)
    }

    override fun stop() {
        super.stop()
        reset()

        for (fnafUPlayer in players) {
            fnafUPlayer.clearSkin()
            map.ambientSound.stop(fnafUPlayer.player, SoundCategory.AMBIENT)
            fnafUPlayer.player.closeInventory()
            scoreboard.hide(fnafUPlayer.player)
            fnafUPlayer.showTitle(Component.text(winner.toString()).color(winner.color), Component.empty(), 10, 40, 10)
            fnafUPlayer.reset()
        }
    }

    override fun reset() {

        try {
            teamAnimatronics?.unregister()
            teamSurvivors?.unregister()
        } catch (_: Exception){}

        removeBossBar(energyBar)
        removeBossBar(timeBar)

        kitSetupTask?.cancel()
        animatronicWaitingTask?.cancel()
        energyBar = null
        timeBar = null

        map.reset()
        energy.reset()
        systems.reset()

        survivorLives = MAX_LIVES
    }

    override fun onPlayerDamagePlayer(event: EntityDamageByEntityEvent, damager: FnafUPlayer, victim: FnafUPlayer){
        if (damager.type == FnafUPlayer.Type.SURVIVOR) {
            event.isCancelled = true
            return
        }

        victim.damage()
    }

    override fun checkForEndConditions() {
        if (survivorLives == 0 && playerContainer.aliveSurvivorsAmount == 0){
            winner = Winner.ANIMATRONICS
            stop()
        }
    }


    override fun onPlayerClicksDoorButton(event: PlayerInteractEvent, player: MGUPlayer, button: ButtonDoorPair) {
        val door = button.door
        stunAnimatronicsAround(door.stunCenter)
        door.toggle()
        energy.updateConsumption()
        systems.door.updateDoorMenu()
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

    private fun removeBossBar(bossBar: BossBar?) {
        for (fnafUPlayer in players) bossBar?.removeViewer(fnafUPlayer.player)
    }

    private fun showBossBarToAll(bossBar: BossBar?) {
        for (fnafUPlayer in players) bossBar?.addViewer(fnafUPlayer.player)
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

    override fun getTeam(fnafUPlayer: FnafUPlayer): Team? {
        if (teamSurvivors?.hasPlayer(fnafUPlayer.player) ?: false) return teamSurvivors!!
        if (teamAnimatronics?.hasPlayer(fnafUPlayer.player) ?: false) return teamAnimatronics!!
        return null
    }

    override fun applyForEveryAbility(function: (component: RPGUActiveAbilityItem, player: FnafUPlayer, item: CustomItem) -> Unit) {
        this.playerContainer.forEach { player ->
            player.abilityItems.forEach { item ->
                function.invoke(item.components.getOrDefault(RPGUComponents.ACTIVE_ABILITY_ITEM), player, item)
            }
        }
    }
}
