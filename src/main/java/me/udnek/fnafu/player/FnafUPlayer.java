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

    protected Player getPlayer() {
        return player;
    }

    public FnafUPlayer(Player player, Game game){
        this.player = player;
        this.game = game;
    }
    public abstract PlayerType getType();

    public abstract AbilitiesHolder<? extends FnafUPlayer> getAbilitiesHolder();

    public void setKit(Kit kit){ this.kit = kit;}
    public Kit getKit() { return kit;}

    public Game getGame() {return game;}
    public void openMenu(CustomInventory customInventory){ customInventory.open(getPlayer()); }
    public void give(ItemStack itemStack, int slot){
        getPlayer().getInventory().setItem(slot, itemStack);
    }
    public void give(ItemStack itemStack){
        getPlayer().getInventory().addItem(itemStack);
    }

    public void cooldownMaterial(Material material, int ticks){
        getPlayer().setCooldown(material, ticks);
    }
    public int getCooldownMaterial(Material material){
        return getPlayer().getCooldown(material);
    }

    public void sendMessage(Component component){
        getPlayer().sendMessage(component);
    }
    public void sendMessage(String message){
        getPlayer().sendMessage(message);
    }

    @Override
    public String toString() {return "["+getType()+"] "+getPlayer().getName();}


    public void setGameMode(GameMode gameMode){
        getPlayer().setGameMode(gameMode);
    }

    public void teleport(LocationData locationData){teleport(locationData.getRandom());}
    public void teleport(Location location){ getPlayer().teleport(location);}
    public Location getLocation(){return getPlayer().getLocation();}

    public void snowBossBar(BossBar bossBar){
        bossBar.addViewer(getPlayer());
    }
    public void showTitle(Component title, Component subTitle, int fadeIn, int stay, int fadeOut){
        Title titleData = Title.title(
                title,
                Component.empty(),
                Title.Times.times(
                        Duration.ofMillis(fadeIn * 50L),
                        Duration.ofMillis(stay * 50L),
                        Duration.ofMillis(fadeOut * 50L)));

        getPlayer().showTitle(titleData);
    }

    public void addPotionEffect(PotionEffect potionEffect){
        getPlayer().addPotionEffect(potionEffect);
    }

    public void addBossBar(BossBar bossBar){
        bossBar.addViewer(getPlayer());
    }
    public void removeBossBar(BossBar bossBar){
        bossBar.removeViewer(getPlayer());
    }
    public void addToTeam(Team team){
        team.addPlayer(getPlayer());
    }

    public void playSound(Location location, String sound, float range){
        getPlayer().playSound(location, sound, range/16f, 1f);
    }
    public void playSound(Location location, Sound sound, float range){
        getPlayer().playSound(location, sound, range/16f, 1f);
    }

    public void spectateEntity(Entity entity){
        ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
        PacketContainer packet = protocolManager.createPacket(PacketType.Play.Server.CAMERA);
        packet.getIntegers().write(0, entity.getEntityId());
        protocolManager.sendServerPacket(getPlayer(), packet);
        spectatingEntity = entity;
    }
    public void spectateSelf(){
        spectateEntity(getPlayer());
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

        protocolManager.sendServerPacket(getPlayer(), packet);*/



    public void showAuraToPlayerLike(List<? extends FnafUPlayer> toPlayers, int duration, NamedTextColor color){
        ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();


        PacketContainer packet = protocolManager.createPacket(PacketType.Play.Server.SPAWN_ENTITY);
        packet.getModifier().writeDefaults();
        packet.getEntityTypeModifier().write(0, EntityType.PLAYER);
        packet.getUUIDs().write(0, UUID.randomUUID());
        packet.getIntegers().write(1, 1);
        Location location = getPlayer().getLocation();
        packet.getDoubles().write(0, location.getX());
        packet.getDoubles().write(1, location.getY());
        packet.getDoubles().write(2, location.getZ());

        PacketContainer packet2 = protocolManager.createPacket(PacketType.Play.Server.ENTITY_METADATA); // metadata packet
        packet2.getIntegers().write(0, packet.getIntegers().read(0)); //Set entity id from packet above

        WrappedDataWatcher watcher = WrappedDataWatcher.getEntityWatcher(getPlayer()).deepClone();
        //watcher.setObject(0, WrappedDataWatcher.Registry.get(Byte.class), (byte) (0x40));
        final List<WrappedDataValue> wrappedDataValueList = new ArrayList<>();
        watcher.getWatchableObjects().stream().filter(Objects::nonNull).forEach(entry -> {
            final WrappedDataWatcher.WrappedDataWatcherObject dataWatcherObject = entry.getWatcherObject();
            wrappedDataValueList.add(new WrappedDataValue(dataWatcherObject.getIndex(), dataWatcherObject.getSerializer(), entry.getRawValue()));
        });
        packet2.getDataValueCollectionModifier().write(0, wrappedDataValueList);

        protocolManager.sendServerPacket(getPlayer(), packet);
        protocolManager.sendServerPacket(getPlayer(), packet2);
    }


    public void showAuraTo(List<? extends FnafUPlayer> toPlayers, int duration, Color color) {


        ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();

        PacketContainer packet = protocolManager.createPacket(PacketType.Play.Server.SPAWN_ENTITY);
        packet.getModifier().writeDefaults();
        packet.getEntityTypeModifier().write(0, EntityType.ITEM_DISPLAY);
        packet.getUUIDs().write(0, UUID.randomUUID());
        packet.getIntegers().write(1, 1);
        Location location = getPlayer().getLocation();
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
                .write(0, getPlayer().getEntityId());
        packet3.getIntegerArrays().write(0, new int[]{packet.getIntegers().read(0)});

        for (FnafUPlayer toPlayer : toPlayers) {
            protocolManager.sendServerPacket(toPlayer.getPlayer(), packet);
            protocolManager.sendServerPacket(toPlayer.getPlayer(), packet2);
            protocolManager.sendServerPacket(toPlayer.getPlayer(), packet3);
        }


        new BukkitRunnable() {
            @Override
            public void run() {
                getPlayer().sendMessage("TODO REMOVE ENTITY");

                PacketContainer removePacket = protocolManager.createPacket(PacketType.Play.Server.ENTITY_DESTROY);
                removePacket.getModifier().write(0, new IntArrayList(new int[]{packet.getIntegers().read(0)}));

                for (FnafUPlayer toPlayer : toPlayers) {
                    protocolManager.sendServerPacket(toPlayer.getPlayer(), removePacket);
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
        getPlayer().getInventory().clear();
    }
}








