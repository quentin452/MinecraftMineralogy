package com.mcmoddev.mineralogy.worldgen;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.terraingen.OreGenEvent;

import cpw.mods.fml.common.IWorldGenerator;
import cpw.mods.fml.common.eventhandler.Event;

public class OreSpawner implements IWorldGenerator {

    private final float frequency;
    private final WorldGenMinable oreGen;
    private final int minY;
    private final int maxY;
    private final long hash; // used to make prng's different

    // private final Block ore;

    public OreSpawner(Block oreBlock, int minHeight, int maxHeight, float spawnFrequency, int spawnQuantity,
        long hash) {
        // oreGen = new WorldGenMinable(oreBlock, 0, spawnQuantity, Blocks.stone);
        oreGen = new WorldGenMinable(oreBlock, spawnQuantity);
        frequency = spawnFrequency;
        minY = minHeight;
        maxY = maxHeight;
        // ore = oreBlock;
        this.hash = hash;
    }

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator,
        IChunkProvider chunkProvider) {

        random.setSeed(random.nextLong() ^ hash);
        random.nextInt();
        final float r = random.nextFloat();

        for (float f = frequency; f > r; f -= 1) {
            int x = (chunkX << 4) + random.nextInt(16);
            int y = random.nextInt(maxY - minY) + minY;
            int z = (chunkZ << 4) + random.nextInt(16);

            OreGenEvent oreEvent = new OreGenEvent(world, random, x, z);
            MinecraftForge.ORE_GEN_BUS.post(oreEvent);

            if (oreEvent.getResult() == Event.Result.DENY) {
                // Canceled by other mod
                continue;
            }

            oreGen.generate(world, random, x, y, z);
        }
    }
}
