package me.udnek.fnafu.manager;

import com.google.common.base.Preconditions;
import me.udnek.fnafu.game.AbstractGame;
import me.udnek.fnafu.game.EnergyGame;
import me.udnek.fnafu.game.Game;
import me.udnek.fnafu.map.FnafUMap;
import me.udnek.fnafu.map.type.Fnaf1PizzeriaMap;
import me.udnek.fnafu.player.PlayerType;
import org.bukkit.entity.Player;

import java.util.*;

public class GameManager {

    private static GameManager instance;
    private final HashMap<String, Game> games = new HashMap<>();
    private final HashMap<Player, String> activePlayers = new HashMap<>();

    private GameManager(){}
    public static GameManager getManager(){
        if (instance == null) {instance = new GameManager();}
        return instance;
    }

    public void registerMap(Fnaf1PizzeriaMap map){
        Preconditions.checkArgument(map != null, "Map cannot be null");
        AbstractGame game = new EnergyGame(map);
        games.put(game.getId(), game);
    }

    public boolean gameIdExists(String id){ return games.containsKey(id);}
    private Game getGame(String id){ return games.get(id); }
    public Game getGame(Player player){
        String gameId = getPlayerGameId(player);
        if (gameId == null) return null;
        return getGame(gameId);
    }
    public List<String> getAllIds(){ return new ArrayList<>(games.keySet());}

    public boolean isPlayerActive(Player player){
        return activePlayers.containsKey(player);
    }
    public String getPlayerGameId(Player player){
        return activePlayers.get(player);
    }
    private void addActivePlayer(Player player, String gameId){
        if (isPlayerActive(player)) return;
        activePlayers.put(player, gameId);
    }
    private void removeActivePlayer(Player player){
        activePlayers.remove(player);
    }

    public List<String> getDebug(String gameId){
        if (!gameIdExists(gameId)) return new ArrayList<>();
        return getGame(gameId).getDebugData();
    }

    public boolean join(String gameId, Player player, PlayerType playerType){
        if (!gameIdExists(gameId)) return false;
        if (isPlayerActive(player)) return false;
        boolean hasJoined = getGame(gameId).join(player, playerType);
        if (hasJoined) addActivePlayer(player, gameId);
        return hasJoined;
    }

    public boolean leave(Player player){
        if (!isPlayerActive(player)) return false;
        String gameId = getPlayerGameId(player);
        if (!gameIdExists(gameId)) return false;
        boolean hasLeft = getGame(gameId).leave(player);
        if (hasLeft) removeActivePlayer(player);
        return hasLeft;
    }

    public boolean startGame(String gameId){
        if (!gameIdExists(gameId)) return false;
        return getGame(gameId).startGame();
    }
    public boolean stopGame(String gameId){
        if (!gameIdExists(gameId)) return false;
        return getGame(gameId).stopGame();
    }

}
