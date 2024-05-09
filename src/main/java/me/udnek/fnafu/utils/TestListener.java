package me.udnek.fnafu.utils;

import me.udnek.itemscoreu.utils.SelfRegisteringListener;
import org.bukkit.plugin.java.JavaPlugin;

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
