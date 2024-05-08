package me.udnek.fnafu.player.type;

import me.udnek.fnafu.game.Game;
import me.udnek.fnafu.player.FnafUPlayer;
import me.udnek.fnafu.player.HealthState;
import me.udnek.fnafu.player.PlayerType;
import org.bukkit.entity.Player;

public final class Survivor extends FnafUPlayer {

    private HealthState healthState = HealthState.HEALTHY;

    public Survivor(Player player, Game game) {
        super(player, game);
    }

    public HealthState getHealthState(){ return healthState; }
    public void injure() {healthState = healthState.getNextState();}

    @Override
    public PlayerType getType() {
        return PlayerType.SURVIVOR;
    }
}
