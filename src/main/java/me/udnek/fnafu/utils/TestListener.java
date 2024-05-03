package me.udnek.fnafu.utils;

import me.udnek.fnafu.player.type.Survivor;
import me.udnek.itemscoreu.utils.LogUtils;
import me.udnek.itemscoreu.utils.SelfRegisteringListener;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Color;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Collections;

public class TestListener extends SelfRegisteringListener {
    public TestListener(JavaPlugin plugin) {
        super(plugin);
    }

/*    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        LogUtils.log(Integer.toHexString(event.getPlayer().hashCode()));
        Survivor survivor = new Survivor(event.getPlayer());
        survivor.showAuraTo(new ArrayList<>(Collections.singleton(survivor)), 0, Color.BLUE);
    }

    @EventHandler
    public void onPlayerRightClicks(PlayerInteractEvent event){
        Survivor survivor = new Survivor(event.getPlayer());
        survivor.showAuraTo(new ArrayList<>(Collections.singleton(survivor)), 0, Color.BLUE);
    }

    @EventHandler
    public void onPlayerLeaves(PlayerQuitEvent event){
        LogUtils.log(Integer.toHexString(event.getPlayer().hashCode()));
    }*/

}
