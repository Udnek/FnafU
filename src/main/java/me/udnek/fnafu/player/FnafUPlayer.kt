package me.udnek.fnafu.player;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedDataValue;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import me.udnek.fnafu.FnafU;
import me.udnek.fnafu.ability.AbilitiesHolder;
import me.udnek.fnafu.game.Game;
import me.udnek.fnafu.kit.Kit;
import me.udnek.fnafu.map.location.LocationData;
import me.udnek.fnafu.utils.Resettable;
import me.udnek.itemscoreu.custominventory.CustomInventory;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.Title;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Team;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public abstract class FnafUPlayer implements Resettable {

    private final Player player;
    private final Game game;
    protected Entity spectatingEntity;
    protected Kit kit;

/*    protected Player getPlayer() {
        return player;
    }*/

    public FnafUPlayer(Player player, Game game){
        this.player = player;
        this.game = game;
    }
    public abstract PlayerType getType();

    public boolean isThisPlayer(Player thisPlayer){
        return player == thisPlayer;
    }

    public abstract AbilitiesHolder<? extends FnafUPlayer> getAbilitiesHolder();

    public void setKit(Kit kit){ this.kit = kit;}
    public Kit getKit() { return kit;}

    public Game getGame() {return game;}
    public void openMenu(CustomInventory customInventory){ customInventory.open(player); }
    public void give(ItemStack itemStack, int slot){
        player.getInventory().setItem(slot, itemStack);
    }
    public void give(ItemStack itemStack){
        player.getInventory().addItem(itemStack);
    }

    public void cooldownMaterial(Material material, int ticks){
        player.setCooldown(material, ticks);
    }
    public int getCooldownMaterial(Material material){
        return player.getCooldown(material);
    }

    public void sendMessage(Component component){
        player.sendMessage(component);
    }
    public void sendMessage(String message){
        player.sendMessage(message);
    }

    @Override
    public String toString() {return "["+getType()+"] "+player.getName();}


    public void setGameMode(GameMode gameMode){
        player.setGameMode(gameMode);
    }

    public void teleport(LocationData locationData){teleport(locationData.getRandom());}
    public void teleport(Location location){ player.teleport(location);}
    public Location getLocation(){return player.getLocation();}

    public void snowBossBar(BossBar bossBar){
        bossBar.addViewer(player);
    }
    public void showTitle(Component title, Component subTitle, int fadeIn, int stay, int fadeOut){
        Title titleData = Title.title(
                title,
                Component.empty(),
                Title.Times.times(
                        Duration.ofMillis(fadeIn * 50L),
                        Duration.ofMillis(stay * 50L),
                        Duration.ofMillis(fadeOut * 50L)));

        player.showTitle(titleData);
    }

    public void addPotionEffect(PotionEffect potionEffect){
        player.addPotionEffect(potionEffect);
    }

    public void addBossBar(BossBar bossBar){
        bossBar.addViewer(player);
    }
    public void removeBossBar(BossBar bossBar){
        bossBar.removeViewer(player);
    }
    public void addToTeam(Team team){
        team.addPlayer(player);
    }

    public void playSound(Location location, String sound, float range){
        player.playSound(location, sound, range/16f, 1f);
    }
    public void playSound(Location location, Sound sound, float range){
        player.playSound(location, sound, range/16f, 1f);
    }

    public void spectateEntity(Entity entity){
        ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
        PacketContainer packet = protocolManager.createPacket(PacketType.Play.Server.CAMERA);
        packet.getIntegers().write(0, entity.getEntityId());
        protocolManager.sendServerPacket(player, packet);
        spectatingEntity = entity;
    }
    public void spectateSelf(){
        spectateEntity(player);
        spectatingEntity = null;
    }

    public Entity getSpectatingEntity(){ return  spectatingEntity;}

/*        ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();

        PacketContainer packet = protocolManager.createPacket(PacketType.Play.Server.NAMED_SOUND_EFFECT);
        packet.getModifier().writeDefaults();
        packet.getSoundEffects().write(0, Sound.BLOCK_ANVIL_USE);
        packet.getIntegers()
                .write(0, (int) (location.getX()*8))
                .write(1, (int) (location.getY()*8))
                .write(2, (int) (location.getZ()*8));
        packet.getFloat()
                .write(0, range/16f)
                .write(1, pitch);

*//*
        LogUtils.log(String.valueOf(packet.getBytes().getFields().size()));
        LogUtils.log(String.valueOf(packet.getDoubles().getFields().size()));
        LogUtils.log(String.valueOf(packet.getLongs().getFields().size()));
        LogUtils.log(String.valueOf(packet.getStrings().getFields().size()));
        LogUtils.log(String.valueOf(packet.getOptionalStructures().getFields().size()));
        LogUtils.log(String.valueOf(packet.getShorts().getFields().size()));
*//*
        //LogUtils.log(packet.getModifier().getField(0).getType().toString());
*//*        for (int i = 0; i < packet.getModifier().size(); i++) {
            LogUtils.log(packet.getModifier().read(i));
        }*//*
        LogUtils.log(packet.getSoundEffects().write(0, Sound.va));

        protocolManager.sendServerPacket(player, packet);*/



    public void showAuraToPlayerLike(List<? extends FnafUPlayer> toPlayers, int duration, NamedTextColor color){
        ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();


        PacketContainer packet = protocolManager.createPacket(PacketType.Play.Server.SPAWN_ENTITY);
        packet.getModifier().writeDefaults();
        packet.getEntityTypeModifier().write(0, EntityType.PLAYER);
        packet.getUUIDs().write(0, UUID.randomUUID());
        packet.getIntegers().write(1, 1);
        Location location = player.getLocation();
        packet.getDoubles().write(0, location.getX());
        packet.getDoubles().write(1, location.getY());
        packet.getDoubles().write(2, location.getZ());

        PacketContainer packet2 = protocolManager.createPacket(PacketType.Play.Server.ENTITY_METADATA); // metadata packet
        packet2.getIntegers().write(0, packet.getIntegers().read(0)); //Set entity id from packet above

        WrappedDataWatcher watcher = WrappedDataWatcher.getEntityWatcher(player).deepClone();
        //watcher.setObject(0, WrappedDataWatcher.Registry.get(Byte.class), (byte) (0x40));
        final List<WrappedDataValue> wrappedDataValueList = new ArrayList<>();
        watcher.getWatchableObjects().stream().filter(Objects::nonNull).forEach(entry -> {
            final WrappedDataWatcher.WrappedDataWatcherObject dataWatcherObject = entry.getWatcherObject();
            wrappedDataValueList.add(new WrappedDataValue(dataWatcherObject.getIndex(), dataWatcherObject.getSerializer(), entry.getRawValue()));
        });
        packet2.getDataValueCollectionModifier().write(0, wrappedDataValueList);

        protocolManager.sendServerPacket(player, packet);
        protocolManager.sendServerPacket(player, packet2);
    }


    public void showAuraTo(List<? extends FnafUPlayer> toPlayers, int duration, Color color) {


        ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();

        PacketContainer packet = protocolManager.createPacket(PacketType.Play.Server.SPAWN_ENTITY);
        packet.getModifier().writeDefaults();
        packet.getEntityTypeModifier().write(0, EntityType.ITEM_DISPLAY);
        packet.getUUIDs().write(0, UUID.randomUUID());
        packet.getIntegers().write(1, 1);
        Location location = player.getLocation();
        packet.getDoubles().write(0, location.getX());
        packet.getDoubles().write(1, location.getY());
        packet.getDoubles().write(2, location.getZ());

        PacketContainer packet2 = protocolManager.createPacket(PacketType.Play.Server.ENTITY_METADATA); // metadata packet
        packet2.getIntegers().write(0, packet.getIntegers().read(0)); //Set entity id from packet above
        ItemDisplay realEntity = (ItemDisplay) Bukkit.getWorld("world").spawnEntity(location, EntityType.ITEM_DISPLAY);
        realEntity.setItemStack(new ItemStack(Material.DIAMOND_SWORD));
        realEntity.setGlowColorOverride(color);
        realEntity.setBillboard(Display.Billboard.CENTER);


        WrappedDataWatcher watcher = WrappedDataWatcher.getEntityWatcher(realEntity).deepClone();
        realEntity.remove();
        watcher.setObject(0, WrappedDataWatcher.Registry.get(Byte.class), (byte) (0x40)); //Set status to glowing, found on protocol page
        final List<WrappedDataValue> wrappedDataValueList = new ArrayList<>();
        watcher.getWatchableObjects().stream().filter(Objects::nonNull).forEach(entry -> {
            final WrappedDataWatcher.WrappedDataWatcherObject dataWatcherObject = entry.getWatcherObject();
            wrappedDataValueList.add(new WrappedDataValue(dataWatcherObject.getIndex(), dataWatcherObject.getSerializer(), entry.getRawValue()));
        });
        packet2.getDataValueCollectionModifier().write(0, wrappedDataValueList);

        PacketContainer packet3 = protocolManager.createPacket(PacketType.Play.Server.MOUNT);
        packet3.getIntegers()
                .write(0, player.getEntityId());
        packet3.getIntegerArrays().write(0, new int[]{packet.getIntegers().read(0)});

        for (FnafUPlayer toPlayer : toPlayers) {
            protocolManager.sendServerPacket(toPlayer.player, packet);
            protocolManager.sendServerPacket(toPlayer.player, packet2);
            protocolManager.sendServerPacket(toPlayer.player, packet3);
        }


        new BukkitRunnable() {
            @Override
            public void run() {
                player.sendMessage("TODO REMOVE ENTITY");

                PacketContainer removePacket = protocolManager.createPacket(PacketType.Play.Server.ENTITY_DESTROY);
                removePacket.getModifier().write(0, new IntArrayList(new int[]{packet.getIntegers().read(0)}));

                for (FnafUPlayer toPlayer : toPlayers) {
                    protocolManager.sendServerPacket(toPlayer.player, removePacket);
                }
            }
        }.runTaskLater(FnafU.getInstance(), duration);



/*        WrappedDataWatcher watcher = WrappedDataWatcher.getEntityWatcher(getPlayer());
        WrappedDataWatcher.Serializer serializer = WrappedDataWatcher.Registry.get(Byte.class);


        //watcher.setObject(0, serializer, 0x40);


        packet.getWatchableCollectionModifier().write(0, ((((byte) watcher.getObject(serializer)) & ()) | (Entity_Glowing)));*/

/*        PacketContainer packet = new PacketContainer(PacketType.Play.Server.ENTITY_EFFECT);

        packet.getEffectTypes().write(0, PotionEffectType.BLINDNESS);
        packet.getIntegers()
                .write(0, getPlayer().getEntityId())
                .write(1, 100);
        packet.getBytes()
                .write(0, (byte) 0);*/


/*        for (FnafUPlayer fnafUPlayer : toPlayers) {
            //fnafUPlayer.getPlayer().
            protocolManager.sendServerPacket(fnafUPlayer.getPlayer(), packet);
        }*/

    }

    @Override
    public void reset() {
        player.getInventory().clear();
    }
}








