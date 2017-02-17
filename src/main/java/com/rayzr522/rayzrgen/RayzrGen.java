package com.rayzr522.rayzrgen;

import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author Rayzr
 */
public class RayzrGen extends JavaPlugin {

    @Override
    public ChunkGenerator getDefaultWorldGenerator(String worldName, String id) {
        return new CustomChunkGenerator();
    }

}
