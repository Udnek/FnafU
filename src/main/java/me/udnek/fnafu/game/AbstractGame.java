package me.udnek.fnafu.game;

import me.udnek.fnafu.FnafU;
import me.udnek.fnafu.ability.Abilities;
import me.udnek.fnafu.player.FnafUPlayer;
import me.udnek.fnafu.player.PlayerContainer;
import me.udnek.fnafu.player.PlayerType;
import me.udnek.fnafu.player.type.Animatronic;
import me.udnek.fnafu.player.type.Survivor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractGame implements Game, EventHandler{

    private GameState gameState = GameState.WAITING;
    protected final PlayerContainer playerContainer = new PlayerContainer();
    private BukkitRunnable gameTask;

    @Override
    public GameState getGameState() { return this.gameState; }


    @Override
    public boolean startGame(){
        if (gameState != GameState.WAITING) return false;

        gameState = GameState.RUNNING;

        gameTask = new BukkitRunnable() {
            @Override
            public void run() {tick();}
        };
        start();
        gameTask.runTaskTimer(FnafU.getInstance(), 0, 1);

        return true;
    }

    @Override
    public boolean stopGame(){
        if (gameTask == null) return false;
        if (gameTask.isCancelled()) return false;
        gameTask.cancel();
        gameState = GameState.WAITING;
        stop();
        return true;
    }

    protected abstract void tick();
    protected abstract void start();
    protected abstract void stop();

    @Override
    public boolean join(Player player, PlayerType playerType){
        if (playerType == PlayerType.SURVIVOR){
            if (playerContainer.containsSurvivor(player)) return false;
            playerContainer.add(new Survivor(player, this));
            return true;
        }
        if (playerContainer.containsAnimatronic(player)) return false;
        playerContainer.add(new Animatronic(player, this));
        return true;

    }

    @Override
    public boolean leave(Player player){
        return playerContainer.remove(player);
    }

    @Override
    public List<String> getDebugData(){
        List<String> debug = new ArrayList<>();
        debug.add("");
        debug.add("Game debug data:");
        debug.add("id: " + getId());
        debug.add("survs: " + playerContainer.getSurvivors(false).toString());
        debug.add("anims: " + playerContainer.getAnimatronics(false).toString());
        debug.add("state: " + gameState);
        debug.add("gameTask: " + gameTask);
        debug.add("gameTask running: " + (gameTask != null && !gameTask.isCancelled()));
        debug.add("");

        return debug;
    }

    @Override
    public EventHandler getEventHandler() {
        return this;
    }

    @Override
    public void onPlayerClicksInInventory(InventoryClickEvent event) {
        if (!getGameState().isActive()) return;
        event.setCancelled(true);
    }

    @Override
    public void onPlayerDropsItem(PlayerDropItemEvent event) {
        if (!getGameState().isActive()) return;
        event.setCancelled(true);
    }

    @Override
    public void onPlayerActivatesAbility(PlayerInteractEvent event, Abilities rawAbility) {
        if (!getGameState().isActive()) return;
        FnafUPlayer fnafUPlayer = playerContainer.getPlayer(event.getPlayer());
        fnafUPlayer.getAbilitiesHolder().activate(rawAbility);
    }
}















