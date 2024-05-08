package me.udnek.fnafu.game;

import me.udnek.fnafu.map.FnafUMap;
import me.udnek.fnafu.player.FnafUPlayer;
import me.udnek.fnafu.player.PlayerType;
import me.udnek.fnafu.utils.NameId;
import org.bukkit.entity.Player;

import java.util.List;

public interface Game extends NameId{
    boolean startGame();
    boolean stopGame();
    GameState getGameState();
    List<String> getDebugData();
    boolean leave(Player player);
    boolean join(Player player, PlayerType playerType);
    String getId();
    FnafUMap getMap();
    EventHandler getEventHandler();
}
