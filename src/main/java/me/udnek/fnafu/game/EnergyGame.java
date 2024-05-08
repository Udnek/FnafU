package me.udnek.fnafu.game;

import me.udnek.fnafu.kit.playable.CameraKit;
import me.udnek.fnafu.mechanic.camera.Camera;
import me.udnek.fnafu.mechanic.Energy;
import me.udnek.fnafu.mechanic.Time;
import me.udnek.fnafu.mechanic.door.Door;
import me.udnek.fnafu.item.Items;
import me.udnek.fnafu.map.LocationType;
import me.udnek.fnafu.map.type.Fnaf1PizzeriaMap;
import me.udnek.fnafu.player.FnafUPlayer;
import me.udnek.fnafu.player.type.Animatronic;
import me.udnek.fnafu.player.type.Survivor;
import me.udnek.itemscoreu.customitem.CustomItem;
import me.udnek.itemscoreu.utils.CustomItemUtils;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.data.Powerable;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.NameTagVisibility;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class EnergyGame extends AbstractGame {
    public static final int GAME_DURATION = 60*20;

    private final Fnaf1PizzeriaMap map;

    private GameWinner winner = GameWinner.NONE;

    private final Time time;
    private final Energy energy;

    private BossBar timeBar;
    private BossBar energyBar;

    private Team teamSurvivors;
    private Team teamAnimatronics;

    private final String id;

    public EnergyGame(Fnaf1PizzeriaMap map) {
        this.map = map;
        time = new Time(GAME_DURATION);
        energy = new Energy(map);
        id = this.getNameId();// + "_" + UUID.randomUUID().toString().substring(0, 5);
    }

    private boolean isEveryNTicks(int n){
        return time.get() % n == 0;
    }

    @Override
    protected void tick(){
        time.tick();
        if (time.isEnded()) {
            winner = GameWinner.SURVIVORS;
            stopGame();
        };
        if (isEveryNTicks(10)) energy.tick();
        if (isEveryNTicks(20)) updateEnergyBar();
        if (isEveryNTicks(5)) updateTimeBar();
    }


    @Override
    protected void start() {
        time.reset();
        energy.reset();
        winner = GameWinner.NONE;

        energyBar = BossBar.bossBar(Component.text(""), BossBar.MAX_PROGRESS, BossBar.Color.YELLOW, BossBar.Overlay.PROGRESS);
        showBossBarToAll(energyBar);
        timeBar = BossBar.bossBar(Component.text(""), BossBar.MAX_PROGRESS, BossBar.Color.WHITE, BossBar.Overlay.PROGRESS);
        showBossBarToAll(timeBar);

        initTeams();

        for (Survivor survivor : playerContainer.getSurvivors(false)) {
            survivor.teleport(map.getLocation(LocationType.SPAWN_SURVIVOR));
            survivor.addToTeam(teamSurvivors);
            survivor.setUpKit(new CameraKit());
            survivor.showAuraTo(playerContainer.getAll(), 0, Color.RED);
        }
        for (Animatronic animatronic : playerContainer.getAnimatronics(false)) {
            animatronic.teleport(map.getLocation(LocationType.SPAWN_ANIMATRONIC));
            animatronic.addToTeam(teamAnimatronics);
            // TODO: 5/8/2024 SET KIT
            animatronic.showAuraTo(playerContainer.getAll(), 0, Color.GREEN);
        }

        teamSurvivors.setAllowFriendlyFire(false);
        teamSurvivors.setNameTagVisibility(NameTagVisibility.HIDE_FOR_OTHER_TEAMS);
        teamSurvivors.setCanSeeFriendlyInvisibles(true);
        teamSurvivors.color(NamedTextColor.GREEN);
        teamSurvivors.prefix(Component.text("[S] ").color(TextColor.color(0f, 1f, 0f)));

        teamAnimatronics.setAllowFriendlyFire(false);
        teamAnimatronics.setNameTagVisibility(NameTagVisibility.HIDE_FOR_OTHER_TEAMS);
        teamAnimatronics.setCanSeeFriendlyInvisibles(true);
        teamAnimatronics.color(NamedTextColor.RED);
        teamAnimatronics.prefix(Component.text("[A] ").color(TextColor.color(1f, 0f, 0f)));


    }

    private void initTeams(){
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        String survivorsTeamName = "survivors_"+getId();
        String animatronicsTeamName = "animatronics_"+getId();
        teamSurvivors = scoreboard.getTeam(survivorsTeamName);
        if (teamSurvivors == null) teamSurvivors = scoreboard.registerNewTeam(survivorsTeamName);
        teamAnimatronics = scoreboard.getTeam(animatronicsTeamName);
        if (teamAnimatronics == null) teamAnimatronics = scoreboard.registerNewTeam(animatronicsTeamName);

    }

    @Override
    protected void stop() {
        teamAnimatronics.unregister();
        teamSurvivors.unregister();
        removeBossBar(energyBar);
        removeBossBar(timeBar);

        for (FnafUPlayer fnafUPlayer : playerContainer.getAll()) {
            fnafUPlayer.showTitle(Component.text(winner.toString()).color(winner.color), Component.empty(), 10, 40, 10);
        }
        
        
    }

    private void removeBossBar(BossBar bossBar){
        for (FnafUPlayer fnafUPlayer : playerContainer.getAll()) {
            fnafUPlayer.removeBossBar(bossBar);
        }
    }
    private void showBossBarToAll(BossBar bossBar){
        for (FnafUPlayer fnafUPlayer : playerContainer.getAll()) {
            fnafUPlayer.addBossBar(bossBar);
        }
    }

    private void updateEnergyBar(){
        energyBar.name(Component.text("Energy: "+energy.getEnergy() + " Usage: "+energy.getUsage()+ " (Consumption: "+energy.getConsumption()+")"));
    }
    private void updateTimeBar(){
        timeBar.name(Component.text(time.get()/20f + "/" + GAME_DURATION/20));
    }

    @Override
    public void onPlayerAttacksEntity(EntityDamageByEntityEvent event) {
        Player damager = (Player) event.getDamager();

        Survivor survivorDamager = playerContainer.getSurvivor(damager);
        if (survivorDamager != null){
            event.setCancelled(true);
            survivorDamager.sendMessage("CANNOT");
            return;
        }
        if (!(event.getEntity() instanceof Player)) return;
        Animatronic animatronic = playerContainer.getAnimatronic(damager);
        Survivor victim = playerContainer.getSurvivor((Player) event.getEntity());
        animatronicDamageSurvivor(animatronic, victim);
    }

    public void animatronicDamageSurvivor(Animatronic animatronic, Survivor survivor){
        survivor.injure();
        animatronic.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 40, 3));
        survivor.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 40, 1));
        survivor.sendMessage("INJURED! " + survivor.getHealthState());
        if (playerContainer.getAliveSurvivorsAmount() == 0) {
            winner = GameWinner.ANIMATRONICS;
            stopGame();
        }
    }

    @Override
    public void onPlayerInteracts(PlayerInteractEvent event) {
        if (!event.getAction().isRightClick()) return;
        Block clickedBlock = event.getClickedBlock();
        if (clickedBlock == null) return;

        if (clickedBlock.getType() == Material.BIRCH_BUTTON) {
            Powerable blockData = (Powerable) clickedBlock.getBlockData();
            if (!blockData.isPowered()) {
                Door door = map.getDoorByButtonLocation(clickedBlock.getLocation());
                if (door != null) {
                    door.toggle();
                    energy.updateConsumption();
                    playerContainer.getSurvivors(false).get(0).playSound(door.getLocation().toCenterLocation(), Sound.BLOCK_ANVIL_USE.getKey().asString(), 16f);
                    return;
                }

                //event.setCancelled(true);
            }
/*        }else {
            map.getCameraSystem().getCameraMenu().open(event.getPlayer());
        }*/
        }
    }


    @Override
    public void onPlayerClicksInCameraMenu(InventoryClickEvent event) {
        ItemStack currentItem = event.getCurrentItem();
        if (currentItem == null) return;
        CustomItem customItem = CustomItemUtils.getFromItemStack(currentItem);
        if (customItem.isSameIds(Items.cameraButton)){
            String cameraId = Items.cameraButton.getCameraId(currentItem);
            Camera camera = map.getCameraSystem().getCamera(cameraId);
            FnafUPlayer player = playerContainer.getPlayer((Player) event.getWhoClicked());
            map.getCameraSystem().spectateCamera(player, camera);
        }
    }

    @Override
    public void onPlayerClosesCameraMenu(InventoryCloseEvent event) {
        FnafUPlayer fnafUPlayer = playerContainer.getPlayer((Player) event.getPlayer());
        map.getCameraSystem().exitCamera(fnafUPlayer);
    }

    @Override
    public void onPlayerOpensCameraMenu(InventoryOpenEvent event) {
        FnafUPlayer fnafUPlayer = playerContainer.getPlayer((Player) event.getPlayer());
        map.getCameraSystem().spectateCamera(fnafUPlayer, map.getCameraSystem().getCamera("main"));
    }


    @Override
    public Fnaf1PizzeriaMap getMap() {
        return map;
    }

    @Override
    public String getNameId() {return map.getNameId();}
    @Override
    public String getId() {return id;}
}
