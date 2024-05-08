package me.udnek.fnafu.player.type;

import me.udnek.fnafu.game.Game;
import me.udnek.fnafu.player.FnafUPlayer;
import me.udnek.fnafu.player.PlayerType;
import org.bukkit.entity.Player;

public final class Animatronic extends FnafUPlayer {
    public Animatronic(Player player, Game game) {
        super(player, game);
    }

    @Override
    public PlayerType getType() {
        return PlayerType.ANIMATRONIC;
    }
}
