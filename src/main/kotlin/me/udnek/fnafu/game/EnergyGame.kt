package me.udnek.fnafu.game

import me.udnek.coreu.custom.entitylike.block.BlockUtils
import me.udnek.coreu.custom.entitylike.block.CustomBlockPlaceContext
import me.udnek.coreu.custom.item.CustomItem
import me.udnek.coreu.custom.sidebar.CustomSidebar
import me.udnek.coreu.mgu.Resettable
import me.udnek.coreu.mgu.game.MGUGameType
import me.udnek.coreu.rpgu.component.RPGUActiveAbilityItem
import me.udnek.coreu.rpgu.component.RPGUComponents
import me.udnek.coreu.util.Utils
import me.udnek.fnafu.FnafU
import me.udnek.fnafu.block.Blocks
import me.udnek.fnafu.block.SystemStationBlock
import me.udnek.fnafu.component.FnafUComponents
import me.udnek.fnafu.effect.Effects
import me.udnek.fnafu.event.EnergyEndedUpEvent
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
import me.udnek.fnafu.sound.Sounds
import me.udnek.fnafu.misc.getFnafU
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
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scoreboard.Team
import java.util.concurrent.ThreadLocalRandom

class EnergyGame(map: FnafUMap) : FnafUAbstractGame(map), Resettable {
    companion object {
        const val GAME_DURATION: Int = 5 * 60 * 20
        const val ANIMATRONIC_WAITING_DURATION: Long = 8 * 20
        const val MAX_LIVES: Int = 5

        const val DOOR_STUN_RADIUS: Double = 1.0
        const val DOOR_STUN_TIME: Int = 1 * 20
        const val DOOR_SLOWNESS_RADIUS: Double = 3.0
        const val DOOR_SLOWNESS_TIME: Int = 3 * 20

        // TODO MAKE ATTRIBUTE
        const val ANIMATRONIC_OPEN_DOOR_DAMAGE: Float = 0.7f
        const val SURVIVOR_TOGGLE_DOOR_DAMAGE: Float = 0.05f
    }

    var kitSetupTask: BukkitRunnable? = null
    var animatronicWaitingTask: BukkitRunnable? = null
    val time: Time = Time(GAME_DURATION)
    override val energy: Energy = Energy(this)

    override var survivorLives: Int = MAX_LIVES
        set(value) {
            field = value
            updateSidebar()
        }
    override val systems: Systems = Systems(DoorSystem(this, map.doors), CameraSystem(this), VentilationSystem(this))

    private var timeBar: BossBar? = null
    private var energyBar: BossBar? = null

    private var teamSurvivors: Team? = null
    private var teamAnimatronics: Team? = null

    override val sidebar = CustomSidebar(id.asString(), Component.empty())

    init {
        map.cameras.forEach { systems.camera.addCamera(it) }
        systems.camera.setOrigin(map.origin)
    }

    private fun isEveryNTicks(n: Int): Boolean = time.ticks % n == 0

    override fun tick() {
        time.tick()
        if (time.isEnded || !super.isRunning()) {
            winner = Winner.SURVIVORS
            stop()
            return
        }

        if (isEveryNTicks(5)) updateEnergyBar()
        if (isEveryNTicks(Energy.TICKRATE)) {
            energy.updateConsumption()
            energy.tick()
            if (!energy.endedUpAlreadyChecked && energy.isEndedUp) {
                energy.endedUpAlreadyChecked = true
                EnergyEndedUpEvent(this).callEvent()
            }
        }
        if (isEveryNTicks(10)){
            systems.tick()
        }
        if (isEveryNTicks(5)) updateTimeBar()
        if (isEveryNTicks(15)) {
            for (animatronic in playerContainer.animatronics) {
                val movement = animatronic.data.getOrCreateDefault(FnafUComponents.MOVEMENT_TRACKER_DATA)
                if (!animatronic.player.isSneaking && movement.hasMoved(animatronic.player.location)) {
                    for (survivor in playerContainer.survivors) {
                        Sounds.ANIMATRONIC_STEP.play(animatronic.player.location, survivor.player)
                    }
                }
                movement.lastLocation = animatronic.player.location

            }
        }
        if (isEveryNTicks(10)){
            for (survivor in playerContainer.aliveSurvivors) {
                if (!survivor.player.isSprinting) continue
                val random = ThreadLocalRandom.current()
                repeat(10) {
                    val from = survivor.player.location.add(random.nextDouble(-2.0, 2.0), 0.0, random.nextDouble(-2.0, 2.0))
                    val to = from.clone().add(0.0, 0.2, 0.0)
                    Particle.TRAIL.builder().location(Utils.rayTraceBlockUnder(from) ?: from).offset(0.0, 0.05, 0.0)
                        .data(Particle.Trail(Utils.rayTraceBlockUnder(to) ?: to, Color.fromRGB(255, random.nextInt(0, 128), 0), 100)).spawn()
                }
            }
        }
    }

    override fun updateSidebar() {
        sidebar.lines = systems.all.associate { system -> system.getSidebarLine() }
        sidebar.setLine(0, Component.translatable("sidebar.fnafu.live_count", Component.text(survivorLives)))
        sidebar.updateForAll()
    }

    override fun start() {
        reset()
        initializeBars()
        initializeTeams()

        updateSidebar()

        chooseSystemStations()

        stage = FnafUGame.Stage.KIT
        for (player in players) {
            player.reset()
            player.saveSkin()

            player.player.exp = 0f
            player.player.level = 0
            player.player.gameMode = GameMode.ADVENTURE
            player.player.addPotionEffect(PotionEffect(PotionEffectType.SATURATION, PotionEffect.INFINITE_DURATION, 10, false, false, false))
            player.player.getAttribute(Attribute.JUMP_STRENGTH)!!.addModifier(
                AttributeModifier(NamespacedKey(FnafU.instance, "game_js"), -10.0, AttributeModifier.Operation.ADD_NUMBER))
            player.player.getAttribute(Attribute.MOVEMENT_SPEED)!!.addModifier(
                AttributeModifier(NamespacedKey(FnafU.instance, "game_ms"), -0.15, AttributeModifier.Operation.ADD_SCALAR))

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

        KitMenu.updateFor(players)

        kitSetupTask = object : BukkitRunnable() {
            override fun run() {
                if (players.count { !KitMenu.getKitStageData(it).isReady } != 0) return
                cancel()
                mainCycleStart()
            }
        }
        kitSetupTask!!.runTaskTimer(FnafU.instance, 20,20)
    }

    fun mainCycleStart(){
        super.start()
        time.reset()
        energy.reset()
        map.reset()
        energy.updateConsumption()
        winner = Winner.NONE
        survivorLives = MAX_LIVES

        showBossBarToAll(energyBar!!)
        updateEnergyBar()
        showBossBarToAll(timeBar!!)
        updateTimeBar()

        for (player in players) {
            player.kit = KitMenu.getKitStageData(player).chosenKit
        }

        for (player in playerContainer.all) {
            when (player.type) {
                FnafUPlayer.Type.SURVIVOR -> player.teleport(map.getLocation(LocationType.SPAWN_SURVIVOR)!!)
                FnafUPlayer.Type.ANIMATRONIC -> player.teleport(map.getLocation(LocationType.PRESPAWN_ANIMATRONIC)!!)
            }
            map.ambientSound.loop { it.play(player.player) }
            sidebar.show(player.player)
            player.player.inventory.clear()
            player.kit.setUp(player)
        }

        animatronicWaitingTask = object : BukkitRunnable() { override fun run() {
            playerContainer.animatronics.forEach {
                it.teleport(map.getLocation(LocationType.SPAWN_ANIMATRONIC)!!, NamedTextColor.RED)
            }
        } }
        animatronicWaitingTask!!.runTaskLater(FnafU.instance, ANIMATRONIC_WAITING_DURATION)
    }

    private fun chooseSystemStations() {
        val systemStations = ArrayList(map.systemStations)
        for (systemStation in systemStations) {
            val location = systemStation.first.first
            BlockUtils.safeSet(location.block, Material.AIR)
            BlockUtils.safeSet(location.add(0.0, -1.0, 0.0).block, Material.AIR)
        }
        for (i in 1 .. map.systemStationsAmount step 2) {
            val systemStation: Pair<LocationSingle, BlockFace> = systemStations.random()
            placeSystemStation(systemStations, systemStation)
            if (i >= map.systemStationsAmount) break
            val farthestSystemStation = systemStations.maxBy { pair -> pair.first.first.distance(systemStation.first.first) }
            placeSystemStation(systemStations, farthestSystemStation)
        }
    }

    private fun placeSystemStation(systemStations: ArrayList<Pair<LocationSingle, BlockFace>>, pair: Pair<LocationSingle, BlockFace>) {
        val location = pair.first.first
        val world = location.world

        world.setBlockData(location.add(0.0, -1.0, 0.0), Material.BARRIER.createBlockData())
        Blocks.SYSTEM_STATION.place(location.add(0.0, 1.0, 0.0), CustomBlockPlaceContext.EMPTY)
        (Blocks.SYSTEM_STATION as SystemStationBlock).setFacing(location.block, pair.second)

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

        map.ambientSound.stop(players)

        for (fnafUPlayer in players) {
            fnafUPlayer.clearSkin()
            fnafUPlayer.player.closeInventory()
            sidebar.hide(fnafUPlayer.player)
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

        victim.damage(damager.player.getFnafU()?.kit?.jumpScareSound ?: return)
    }

    override fun checkForEndConditions() {
        if (survivorLives == 0 && playerContainer.aliveSurvivorsAmount == 0){
            winner = Winner.ANIMATRONICS
            stop()
        }
    }


    override fun onPlayerClicksDoorButton(event: PlayerInteractEvent, player: FnafUPlayer, button: ButtonDoorPair) {
        if (energy.isEndedUp) return
        if (systems.door.isBroken) return
        val door = button.door
        if (player.type == FnafUPlayer.Type.ANIMATRONIC){
            if (door.isClosed) {
                door.open()
                systems.door.durability -= ANIMATRONIC_OPEN_DOOR_DAMAGE
            }
        } else{
            door.toggle()
            stunAnimatronicsAround(door.stunCenter)
            systems.door.durability -= SURVIVOR_TOGGLE_DOOR_DAMAGE
        }

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
