/**
 * 
 */
package com.rayzr522.rayzrgen;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.util.noise.NoiseGenerator;
import org.bukkit.util.noise.PerlinNoiseGenerator;

/**
 * @author Rayzr
 *
 */
public class CustomChunkGenerator extends ChunkGenerator {

    private NoiseGenerator generator;

    private NoiseGenerator getGenerator(World world) {
        if (generator == null)
            generator = new PerlinNoiseGenerator(world);
        return generator;
    }

    /**
     *
     * @param x
     *            X co-ordinate of the block to be set in the array
     * @param y
     *            Y co-ordinate of the block to be set in the array
     * @param z
     *            Z co-ordinate of the block to be set in the array
     * @param chunk
     *            An array containing the Block id's of all the blocks in the chunk. The first offset
     *            is the block section number. There are 16 block sections, stacked vertically, each of which
     *            16 by 16 by 16 blocks.
     * @param material
     *            The material to set the block to.
     */
    @SuppressWarnings("deprecation")
    private void setBlock(int x, int y, int z, byte[][] chunk, Material material) {
        if (y < 256 && y >= 0 && x <= 16 && x >= 0 && z <= 16 && z >= 0) {
            if (chunk[y >> 4] == null)
                chunk[y >> 4] = new byte[16 * 16 * 16];
            chunk[y >> 4][((y & 0xF) << 8) | (z << 4) | x] = (byte) material.getId();
        }
    }

    /**
     * @param world The world the chunk belongs to
     * @param rand Don't use this, make a new random object using the world seed (world.getSeed())
     * @param biome Use this to set/get the current biome
     * @param cx The X position of the current chunk.
     * @param cz The Z position of the current chunk.
     * 
     */
    private int getHeight(NoiseGenerator gen, double base, double x, double z, double scale, double variance) {
        return (int) Math.floor(base + gen.noise(x * scale, z * scale) * variance);
    }

    @Override
    public byte[][] generateBlockSections(World world, Random rand, int cx, int cz, BiomeGrid biome) {
        byte[][] chunk = new byte[world.getMaxHeight() / 16][];
        NoiseGenerator gen = getGenerator(world);

        for (int x = 0; x < 16; x++) { // loop through all of the blocks in the chunk that are lower than maxHeight
            for (int z = 0; z < 16; z++) {

                int base = 60;
                int maxHeight = (int) getHeight(gen, base + getVariance() / 1.7, cx + x * 0.0625, cz + z * 0.0625, 1 / 2.0, getVariance());

                for (int y = 0; y < maxHeight; y++) {
                    if (y < maxHeight - 4) {
                        setBlock(x, y, z, chunk, Material.STONE);
                    } else if (y < maxHeight - 1) {
                        setBlock(x, y, z, chunk, Material.DIRT);
                    } else {
                        setBlock(x, y, z, chunk, Material.GRASS);
                    }
                }

                for (int y = base - 1; y >= maxHeight; y--) {
                    setBlock(x, y, z, chunk, Material.STATIONARY_WATER);
                }

                setBlock(x, 0, z, chunk, Material.BEDROCK);
            }
        }

        return chunk;
    }

    private double getVariance() {
        return 21.0;
    }

    /**
     * Returns a list of all of the block populators (that do "little" features)
     * to be called after the chunk generator
     */
    @Override
    public List<BlockPopulator> getDefaultPopulators(World world) {
        ArrayList<BlockPopulator> pops = new ArrayList<BlockPopulator>();

        return pops;
    }

}
