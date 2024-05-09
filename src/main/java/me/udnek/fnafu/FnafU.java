package me.udnek.fnafu;

import me.udnek.fnafu.command.FnafUCommand;
import me.udnek.fnafu.game.GameListener;
import me.udnek.fnafu.manager.GameManager;
import me.udnek.fnafu.map.type.Fnaf1PizzeriaMap;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public final class FnafU extends JavaPlugin {

    private static FnafU instance;

    @Override
    public void onEnable() {
        instance = this;

        this.getCommand("fnafu").setExecutor(new FnafUCommand());

        new GameListener(getInstance());

        new BukkitRunnable() {
            @Override
            public void run() {
                GameManager.getManager().registerMap(new Fnaf1PizzeriaMap(new Location(Bukkit.getWorld("fnaf"), -224, 65, -3)));
            }
        }.runTask(this);


    }

    @Override
    public void onDisable() {
    }

    public static FnafU getInstance() {
        return instance;
    }
}
