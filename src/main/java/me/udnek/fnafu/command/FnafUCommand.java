package me.udnek.fnafu.command;

import me.udnek.fnafu.manager.GameManager;
import me.udnek.fnafu.player.PlayerType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FnafUCommand implements CommandExecutor, TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        CommandType commandType = CommandType.getType(args);
        if (commandType == CommandType.NULL) return false;
        if (commandType.playerOnly && !(commandSender instanceof Player)) return false;
        if (!commandType.checkArgs(args)) return false;

        boolean result = false;

        String gameId = "";
        if (commandType == CommandType.LEAVE) {
            result = GameManager.getManager().leave((Player) commandSender);
        }
        else {
            gameId = args[1];
        }


        if (commandType == CommandType.JOIN){
            PlayerType playerType = args[2].equalsIgnoreCase("survivor") ? PlayerType.SURVIVOR : PlayerType.ANIMATRONIC;
            result = GameManager.getManager().join(gameId, (Player) commandSender, playerType);
        }


        else if (commandType.isStartOrStop()) {
            if (commandType == CommandType.START){
                result = GameManager.getManager().startGame(gameId);
            } else {
                result = GameManager.getManager().stopGame(gameId);
            }
        }
        else if (commandType == CommandType.DEBUG){
            result = true;
            for (String string : GameManager.getManager().getDebug(gameId)) {
                commandSender.sendMessage(string);
            }
        }

        commandSender.sendMessage(String.valueOf(result));

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (args.length == 1) return Arrays.asList(
                CommandType.START.name,
                CommandType.STOP.name,
                CommandType.JOIN.name,
                CommandType.LEAVE.name,
                CommandType.DEBUG.name);
        if (args.length == 2) return GameManager.getManager().getAllIds();
        if (args.length == 3) return Arrays.asList("survivor", "animatronic");
        return new ArrayList<>();
    }


}
