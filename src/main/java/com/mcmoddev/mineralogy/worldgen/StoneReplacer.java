package com.mcmoddev.mineralogy.worldgen;

import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;

import com.mcmoddev.mineralogy.MineralogyConfig;

import cpw.mods.fml.common.IWorldGenerator;

public class StoneReplacer implements IWorldGenerator {

    private Geology geom = null;

    public StoneReplacer() {
        //
    }

    private final Lock glock = new ReentrantLock();

    /** is thread-safe */
    final Geology getGeology(World w) {
        if (geom == null) {
            glock.lock();
            try {
                if (geom == null) {
                    geom = new Geology(w.getSeed(), MineralogyConfig.geomeSize(), MineralogyConfig.rockLayerNoise());
                }
            } finally {
                glock.unlock();
            }
        }
        return geom;
    }

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator,
        IChunkProvider chunkProvider) {
        if (world.provider.dimensionId == 0 && MineralogyConfig.placeMineralogyRock()) {
            getGeology(world).replaceStoneInChunk(chunkX, chunkZ, world);
        }
    }
}
