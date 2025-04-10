package me.udnek.fnafu.mechanic.door;

import me.udnek.fnafu.FnafU;
import me.udnek.fnafu.map.Originable;
import me.udnek.fnafu.map.location.LocationSingle;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Wall;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class Door implements Originable {

    public static final int CLOSE_DELAY = 2;
    public static final int OPEN_DELAY = 8;

    protected final LocationSingle location;
    private final Direction direction;
    private boolean closed = false;

    public Door(LocationSingle location, Direction direction){
        this.location = location;
        this.direction = direction;
    }

    public boolean isClosed() {
        return closed;
    }

    public void toggle(){
        if (closed) open(); else close();
    }

    private BlockData getLayerBlockData(int layer){
        switch (layer){
            case 0:
                return direction.modifyBlockState((Wall) Material.PRISMARINE_WALL.createBlockData());
            case 1:
                return direction.modifyBlockState((Wall) Material.NETHER_BRICK_WALL.createBlockData());
            case 2:
                return direction.modifyBlockState((Wall) Material.RED_NETHER_BRICK_WALL.createBlockData());
            default:
                return Material.AIR.createBlockData();
        }
    }

    public void close(){
        if (closed) return;
        closed = true;

        int xStep = direction == Direction.X ? 1 : 0;
        int zStep = direction == Direction.X ? 0 : 1;

        new BukkitRunnable() {
            int step = 0;
            Location doorLocation = location.getFirst();
            final World world = doorLocation.getWorld();

            @Override
            public void run() {
                doorLocation = location.getFirst();
                doorLocation.add(-xStep*2, 3, -zStep*2);

                for (int layer = 2-step; layer < 2-step+3; layer++) {
                    BlockData blockData = getLayerBlockData(layer);

                    doorLocation.add(0, -1, 0);
                    for (int i = 0; i < 3; i++) {
                        world.setBlockData(doorLocation.add(xStep, 0, zStep), blockData);
                    }
                    doorLocation.add(-xStep*3, 0, -zStep*3);
                }

                if (step == 2) cancel();
                step += 1;

            }
        }.runTaskTimer(FnafU.getInstance(), 0, CLOSE_DELAY);

    }

    public void open(){
        if (!closed) return;
        closed = false;

        int xStep = direction == Direction.X ? 1 : 0;
        int zStep = direction == Direction.X ? 0 : 1;

        new BukkitRunnable() {
            int step = 0;
            Location doorLocation = location.getFirst();
            final World world = doorLocation.getWorld();

            @Override
            public void run() {
                doorLocation = location.getFirst();
                doorLocation.add(-xStep*2, 3, -zStep*2);

                for (int layer = 1+step; layer < 1+step+3; layer++) {
                    BlockData blockData = getLayerBlockData(layer);

                    doorLocation.add(0, -1, 0);
                    for (int i = 0; i < 3; i++) {
                        world.setBlockData(doorLocation.add(xStep, 0, zStep), blockData);
                    }
                    doorLocation.add(-xStep*3, 0, -zStep*3);
                }

                if (step == 2) cancel();
                step += 1;

            }
        }.runTaskTimer(FnafU.getInstance(), 0, OPEN_DELAY);

    }

    @Override
    public void setOrigin(Location origin) {
        this.location.setOrigin(origin);
    }

    public Location getLocation() {
        return location.getFirst();
    }

    public enum Direction{
        X(new Vector(1, 0, 0)) {
            @Override
            public Wall modifyBlockState(Wall blockData) {
                blockData.setHeight(BlockFace.WEST, Wall.Height.TALL);
                blockData.setHeight(BlockFace.EAST, Wall.Height.TALL);
                blockData.setUp(false);
                return blockData;
            }
        },
        Z(new Vector(0, 0, 1)) {
            @Override
            public Wall modifyBlockState(Wall blockData) {
                blockData.setHeight(BlockFace.SOUTH, Wall.Height.TALL);
                blockData.setHeight(BlockFace.NORTH, Wall.Height.TALL);
                blockData.setUp(false);
                return blockData;
            }
        };

        private final Vector vector;
        Direction(Vector vector){
            this.vector = vector.normalize();
        }

        public Vector getAlignedVector() {return vector.clone();}
        public abstract Wall modifyBlockState(Wall blockData);

    }
}
