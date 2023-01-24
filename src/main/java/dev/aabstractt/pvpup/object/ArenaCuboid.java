package dev.aabstractt.pvpup.object;

import lombok.Getter;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.*;

public class ArenaCuboid implements Iterable<Block>, Cloneable, ConfigurationSerializable {

    private final String worldName;
    private int x1;
    private int y1;
    private int z1;
    private int x2;
    private int y2;
    private int z2;

    @Getter private Location l1 = null;
    @Getter private Location l2 = null;

    /**
     * Construct a Cuboid given two Location objects which represent any two corners of the Cuboid.
     * Note: The 2 locations must be on the same world.
     *
     * @param l1 - One of the corners
     * @param l2 - The other corner
     */
    public ArenaCuboid(Location l1, Location l2) {
        if(!l1.getWorld().equals(l2.getWorld()))
            throw new IllegalArgumentException("Locations must be on the same world");

        this.worldName = l1.getWorld().getName();
        this.x1 = Math.min(l1.getBlockX(), l2.getBlockX());
        this.y1 = Math.min(l1.getBlockY(), l2.getBlockY());
        this.z1 = Math.min(l1.getBlockZ(), l2.getBlockZ());
        this.x2 = Math.max(l1.getBlockX(), l2.getBlockX());
        this.y2 = Math.max(l1.getBlockY(), l2.getBlockY());
        this.z2 = Math.max(l1.getBlockZ(), l2.getBlockZ());
        this.l1 = l1;
        this.l2 = l2;
    }

    public boolean isInCuboid(Location location) {
        return this.contains(location);
    }


    public boolean isInCuboid(Player p) {
        return this.isInCuboid(p.getLocation());
    }

    /**
     * Construct a Cuboid in the given World and xyz co-ordinates
     *
     * @param world - The Cuboid's world
     * @param x1    - X co-ordinate of corner 1
     * @param y1    - Y co-ordinate of corner 1
     * @param z1    - Z co-ordinate of corner 1
     * @param x2    - X co-ordinate of corner 2
     * @param y2    - Y co-ordinate of corner 2
     * @param z2    - Z co-ordinate of corner 2
     */
    public ArenaCuboid(
            World world,
            int x1,
            int y1,
            int z1,
            int x2,
            int y2,
            int z2
    ) {
        this.worldName = world.getName();
        this.x1 = Math.min(x1, x2);
        this.x2 = Math.max(x1, x2);
        this.y1 = Math.min(y1, y2);
        this.y2 = Math.max(y1, y2);
        this.z1 = Math.min(z1, z2);
        this.z2 = Math.max(z1, z2);
    }

    /**
     * Construct a Cuboid in the given world name and xyz co-ordinates.
     *
     * @param worldName - The Cuboid's world name
     * @param x1        - X co-ordinate of corner 1
     * @param y1        - Y co-ordinate of corner 1
     * @param z1        - Z co-ordinate of corner 1
     * @param x2        - X co-ordinate of corner 2
     * @param y2        - Y co-ordinate of corner 2
     * @param z2        - Z co-ordinate of corner 2
     */
    private ArenaCuboid(
            String worldName,
            int x1,
            int y1,
            int z1,
            int x2,
            int y2,
            int z2
    ) {
        this.worldName = worldName;
        this.x1 = Math.min(x1, x2);
        this.x2 = Math.max(x1, x2);
        this.y1 = Math.min(y1, y2);
        this.y2 = Math.max(y1, y2);
        this.z1 = Math.min(z1, z2);
        this.z2 = Math.max(z1, z2);
    }

    /**
     * Construct a Cuboid using a map with the following keys: worldName, x1, x2, y1, y2, z1, z2
     *
     * @param map - The map of keys.
     */
    public ArenaCuboid(Map<String, Object> map) {
        this.worldName = (String) map.get("worldName");
        this.x1 = (Integer) map.get("x1");
        this.x2 = (Integer) map.get("x2");
        this.y1 = (Integer) map.get("y1");
        this.y2 = (Integer) map.get("y2");
        this.z1 = (Integer) map.get("z1");
        this.z2 = (Integer) map.get("z2");
    }

    public void setFirstCorner(@NonNull Location location) {
        this.x1 = location.getBlockX();
        this.y1 = location.getBlockY();
        this.z1 = location.getBlockZ();
    }

    public void setSecondCorner(@NonNull Location location) {
        this.x2 = location.getBlockX();
        this.y2 = location.getBlockY();
        this.z2 = location.getBlockZ();
    }

    /**
     * Get the Location of the lower northeast corner of the Cuboid (minimum XYZ co-ordinates).
     *
     * @return Location of the lower northeast corner
     */
    public Location getLowerNE() {
        return new Location(this.getWorld(), this.x1, this.y1, this.z1);
    }

    /**
     * Get the Location of the upper southwest corner of the Cuboid (maximum XYZ co-ordinates).
     *
     * @return Location of the upper southwest corner
     */
    public Location getUpperSW() {
        return new Location(this.getWorld(), this.x2, this.y2, this.z2);
    }

    /**
     * Get the blocks in the Cuboid.
     *
     * @return The blocks in the Cuboid
     */
    public List<Block> getBlocks() {
        Iterator<Block> blockI = this.iterator();
        List<Block> copy = new ArrayList<>();
        while(blockI.hasNext())
            copy.add(blockI.next());
        return copy;
    }

    /**
     * Get the the centre of the Cuboid.
     *
     * @return Location at the centre of the Cuboid
     */
    public Location getCenter() {
        int x1 = this.getUpperX() + 1;
        int y1 = this.getUpperY() + 1;
        int z1 = this.getUpperZ() + 1;
        return new Location(this.getWorld(), this.getLowerX() + (x1 - this.getLowerX()) / 2.0, this.getLowerY() + (y1 - this.getLowerY()) / 2.0, this.getLowerZ() + (z1 - this.getLowerZ()) / 2.0);
    }

    /**
     * Get the Cuboid's world.
     *
     * @return The World object representing this Cuboid's world
     * @throws IllegalStateException if the world is not loaded
     */
    public World getWorld() {
        final World world = Bukkit.getWorld(this.worldName);

        if(world == null)
            throw new IllegalStateException("World '" + this.worldName + "' is not loaded");

        return world;
    }

    /**
     * Get the size of this Cuboid along the X axis
     *
     * @return Size of Cuboid along the X axis
     */
    public int getSizeX() {
        return (this.x2 - this.x1) + 1;
    }

    /**
     * Get the size of this Cuboid along the Y axis
     *
     * @return Size of Cuboid along the Y axis
     */
    public int getSizeY() {
        return (this.y2 - this.y1) + 1;
    }

    /**
     * Get the size of this Cuboid along the Z axis
     *
     * @return Size of Cuboid along the Z axis
     */
    public int getSizeZ() {
        return (this.z2 - this.z1) + 1;
    }

    /**
     * Get the minimum X co-ordinate of this Cuboid
     *
     * @return the minimum X co-ordinate
     */
    public int getLowerX() {
        return this.x1;
    }

    /**
     * Get the minimum Y co-ordinate of this Cuboid
     *
     * @return the minimum Y co-ordinate
     */
    public int getLowerY() {
        return this.y1;
    }

    /**
     * Get the minimum Z co-ordinate of this Cuboid
     *
     * @return the minimum Z co-ordinate
     */
    public int getLowerZ() {
        return this.z1;
    }

    /**
     * Get the maximum X co-ordinate of this Cuboid
     *
     * @return the maximum X co-ordinate
     */
    public int getUpperX() {
        return this.x2;
    }

    /**
     * Get the maximum Y co-ordinate of this Cuboid
     *
     * @return the maximum Y co-ordinate
     */
    public int getUpperY() {
        return this.y2;
    }

    /**
     * Get the maximum Z co-ordinate of this Cuboid
     *
     * @return the maximum Z co-ordinate
     */
    public int getUpperZ() {
        return this.z2;
    }

    /**
     * Get the Blocks at the eight corners of the Cuboid.
     *
     * @return array of Block objects representing the Cuboid corners
     */
    public Block[] corners() {
        Block[] res = new Block[8];
        World w = this.getWorld();
        res[0] = w.getBlockAt(this.x1, this.y1, this.z1);
        res[1] = w.getBlockAt(this.x1, this.y1, this.z2);
        res[2] = w.getBlockAt(this.x1, this.y2, this.z1);
        res[3] = w.getBlockAt(this.x1, this.y2, this.z2);
        res[4] = w.getBlockAt(this.x2, this.y1, this.z1);
        res[5] = w.getBlockAt(this.x2, this.y1, this.z2);
        res[6] = w.getBlockAt(this.x2, this.y2, this.z1);
        res[7] = w.getBlockAt(this.x2, this.y2, this.z2);
        return res;
    }

    /**
     * Given {@link Location}s v1 and v2 (which must be sorted), return the
     * {@link Location}s which represent all the points along
     * the edges of the {@link ArenaCuboid} formed by v1 and v2.
     *
     * @return the edges of this {@link ArenaCuboid}
     */
    public List<Location> edges() {
        return this.edges(-1, -1, -1, -1);
    }

    /**
     * Given {@link Vector}s v1 and v2 (which must be sorted), return the
     * {@link Vector}s which represent all the points along
     * the edges of the {@link ArenaCuboid} formed by v1 and v2.
     *
     * @return the edges of this {@link ArenaCuboid}
     */
    public List<Location> edges(int fixedMinX, int fixedMaxX, int fixedMinZ, int fixedMaxZ) {
        final int minX = this.x1;
        final int maxX = this.x2;
        final int minZ = this.z1;
        final int maxZ = this.z2;

        int capacity = ((maxX - minX) * 4) + ((maxZ - minZ) * 4);
        capacity += 4; // we do this because the second loop doesn't check '<=' but just '<'.

        List<Location> result = new ArrayList<>(capacity);
        if (capacity <= 0) return result;

        final int minY = this.y1;
        final int maxY = this.y2;

        for (int x = minX; x <= maxX; x++) {
            result.add(new Location(this.getWorld(), x, minY, minZ));
            result.add(new Location(this.getWorld(), x, minY, maxZ));
            result.add(new Location(this.getWorld(), x, maxY, minZ));
            result.add(new Location(this.getWorld(), x, maxY, maxZ));
        }

        for (int z = minZ; z <= maxZ; z++) {
            result.add(new Location(this.getWorld(), minX, minY, z));
            result.add(new Location(this.getWorld(), minX, maxY, z));
            result.add(new Location(this.getWorld(), maxX, minY, z));
            result.add(new Location(this.getWorld(), maxX, maxY, z));
        }

        return result;
    }

    /**
     * Return true if the point at (x,y,z) is contained within this Cuboid.
     *
     * @param x - The X co-ordinate
     * @param y - The Y co-ordinate
     * @param z - The Z co-ordinate
     * @return true if the given point is within this Cuboid, false otherwise
     */
    public boolean contains(int x, int y, int z) {
        return x >= this.x1 && x <= this.x2 && y >= this.y1 && y <= this.y2 && z >= this.z1 && z <= this.z2;
    }

    /**
     * Check if the given Block is contained within this Cuboid.
     *
     * @param b - The Block to check for
     * @return true if the Block is within this Cuboid, false otherwise
     */
    public boolean contains(Block b) {
        return this.contains(b.getLocation());
    }

    /**
     * Check if the given Location is contained within this Cuboid.
     *
     * @param l - The Location to check for
     * @return true if the Location is within this Cuboid, false otherwise
     */
    public boolean contains(Location l) {
        if(!this.worldName.equals(l.getWorld().getName())) {
            return false;
        }

        return this.contains(l.getBlockX(), l.getBlockY(), l.getBlockZ());
    }

    /**
     * Get a block relative to the lower NE point of the Cuboid.
     *
     * @param x - The X co-ordinate
     * @param y - The Y co-ordinate
     * @param z - The Z co-ordinate
     * @return The block at the given position
     */
    public Block getRelativeBlock(int x, int y, int z) {
        return this.getWorld().getBlockAt(this.x1 + x, this.y1 + y, this.z1 + z);
    }

    /**
     * Get a block relative to the lower NE point of the Cuboid in the given World.  This
     * version of getRelativeBlock() should be used if being called many times, to avoid
     * excessive calls to getWorld().
     *
     * @param w - The world
     * @param x - The X co-ordinate
     * @param y - The Y co-ordinate
     * @param z - The Z co-ordinate
     * @return The block at the given position
     */
    public Block getRelativeBlock(World w, int x, int y, int z) {
        return w.getBlockAt(this.x1 + x, y1 + y, this.z1 + z);
    }

    /**
     * Get a list of the chunks which are fully or partially contained in this cuboid.
     *
     * @return A list of Chunk objects
     */
    public List<Chunk> getChunks() {
        List<Chunk> res = new ArrayList<>();

        World w = this.getWorld();
        int x1 = this.getLowerX() & ~0xf;
        int x2 = this.getUpperX() & ~0xf;
        int z1 = this.getLowerZ() & ~0xf;
        int z2 = this.getUpperZ() & ~0xf;
        for(int x = x1; x <= x2; x += 16) {
            for(int z = z1; z <= z2; z += 16) {
                res.add(w.getChunkAt(x >> 4, z >> 4));
            }
        }
        return res;
    }

    public Iterator<Block> iterator() {
        return new CuboidIterator(this.getWorld(), this.x1, this.y1, this.z1, this.x2, this.y2, this.z2);
    }

    @Override
    public String toString() {
        return "Cuboid: " + this.worldName + "," + this.x1 + "," + this.y1 + "," + this.z1 + "=>" + this.x2 + "," + this.y2 + "," + this.z2;
    }

    public static @Nullable ArenaCuboid fromSection(@NonNull World world, @Nullable ConfigurationSection section) {
        if (section == null) return null;

        return new ArenaCuboid(
                world,
                section.getInt("min-x"),
                section.getInt("min-y"),
                section.getInt("min-z"),
                section.getInt("max-x"),
                section.getInt("max-y"),
                section.getInt("max-z")
        );
    }

    @Override
    public Map<String, Object> serialize() {
        final Map<String, Object> map = new HashMap<>();
        map.put("min-x", this.x1);
        map.put("min-y", this.y1);
        map.put("min-z", this.z1);
        map.put("max-x", this.x2);
        map.put("max-y", this.y2);
        map.put("max-z", this.z2);
        return map;
    }

    public class CuboidIterator implements Iterator<Block> {
        private World w;
        private int baseX, baseY, baseZ;
        private int x, y, z;
        private int sizeX, sizeY, sizeZ;

        public CuboidIterator(
                World w,
                int x1,
                int y1,
                int z1,
                int x2,
                int y2,
                int z2
        ) {
            this.w = w;
            this.baseX = x1;
            this.baseY = y1;
            this.baseZ = z1;
            this.sizeX = Math.abs(x2 - x1) + 1;
            this.sizeY = Math.abs(y2 - y1) + 1;
            this.sizeZ = Math.abs(z2 - z1) + 1;
            this.x = this.y = this.z = 0;
        }

        public boolean hasNext() {
            return this.x < this.sizeX && this.y < this.sizeY && this.z < this.sizeZ;
        }

        public Block next() {
            Block b = this.w.getBlockAt(this.baseX + this.x, this.baseY + this.y, this.baseZ + this.z);
            if(++x >= this.sizeX) {
                this.x = 0;
                if(++this.y >= this.sizeY) {
                    this.y = 0;
                    ++this.z;
                }
            }
            return b;
        }

        public void remove() {
        }
    }
}
