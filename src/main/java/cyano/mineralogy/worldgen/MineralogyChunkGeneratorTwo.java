package cyano.mineralogy.worldgen;

import java.util.List;
import java.util.Random;

import cyano.mineralogy.worldgen.math.fastrandom.FastRandom;
import fr.iamacat.multithreading.worldgen.ChunkProviderGenerateTwo;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.MapGenBase;
import net.minecraft.world.gen.MapGenCaves;
import net.minecraft.world.gen.MapGenRavine;
import net.minecraft.world.gen.structure.MapGenMineshaft;
import net.minecraft.world.gen.structure.MapGenScatteredFeature;
import net.minecraft.world.gen.structure.MapGenStronghold;
import net.minecraft.world.gen.structure.MapGenVillage;

import cyano.mineralogy.Mineralogy;

public class MineralogyChunkGeneratorTwo extends ChunkProviderGenerateTwo {

    final String generatorOptionsString;
    final WorldType worldType;
    final World worldObj;
    final Geology geome;
    final boolean mapFeaturesEnabled;
    final FastRandom rand;

    private MapGenBase caveGenerator = new MapGenCaves();
    private MapGenStronghold strongholdGenerator = new MapGenStronghold();
    private MapGenVillage villageGenerator = new MapGenVillage();
    private MapGenMineshaft mineshaftGenerator = new MapGenMineshaft();
    private MapGenScatteredFeature scatteredFeatureGenerator = new MapGenScatteredFeature();
    private MapGenBase ravineGenerator = new MapGenRavine();
    private BiomeGenBase[] biomesForGeneration;

    public MineralogyChunkGeneratorTwo(World world, long seed, boolean mapFeaturesEnabled, String generatorOptionsString,
                                       WorldType worldType) {
        super(world, seed, mapFeaturesEnabled);
        this.worldType = worldType;
        this.worldObj = world;
        this.generatorOptionsString = generatorOptionsString;
        this.mapFeaturesEnabled = mapFeaturesEnabled;
        this.rand = new FastRandom() {
            @Override
            public void setSeed(long seed) {

            }

            @Override
            public void nextBytes(byte[] bytes) {

            }

            @Override
            public int nextInt() {
                return 0;
            }

            @Override
            public int nextInt(int n) {
                return 0;
            }

            @Override
            public long nextLong() {
                return 0;
            }

            @Override
            public boolean nextBoolean() {
                return false;
            }

            @Override
            public float nextFloat() {
                return 0;
            }

            @Override
            public double nextDouble() {
                return 0;
            }

            @Override
            public double nextGaussian() {
                return 0;
            }
        };

        geome = new Geology(seed, Mineralogy.GEOME_SIZE, Mineralogy.ROCK_LAYER_SIZE);
    }

    @Override
    public Chunk provideChunk(int chunkX, int chunkZ) {
        this.rand.setSeed((long) chunkX * 341873128712L + (long) chunkZ * 132897987541L);
        Block[] blocks = new Block[65536];
        byte[] abyte = new byte[65536];
        this.func_147424_a(chunkX, chunkZ, blocks);
        this.biomesForGeneration = this.worldObj.getWorldChunkManager()
            .getBiomesForGeneration(this.biomesForGeneration, chunkX * 16, chunkZ * 16, 16, 16);
        this.replaceBlocksForBiome(chunkX, chunkZ, blocks, abyte, this.biomesForGeneration);
        this.caveGenerator.func_151539_a(this, this.worldObj, chunkX, chunkZ, blocks);
        this.ravineGenerator.func_151539_a(this, this.worldObj, chunkX, chunkZ, blocks);

        geome.replaceStoneInChunk(chunkX, chunkZ, blocks);

        if (this.mapFeaturesEnabled) {
            this.mineshaftGenerator.func_143025_a();
            this.villageGenerator.func_143025_a();
            this.strongholdGenerator.func_143025_a();
            this.scatteredFeatureGenerator.func_143025_a();
        }

        Chunk chunk = new Chunk(this.worldObj, blocks, abyte, chunkX, chunkZ);
        byte[] abyte1 = chunk.getBiomeArray();

        for (int k = 0; k < abyte1.length; ++k) {
            abyte1[k] = (byte) this.biomesForGeneration[k].biomeID;
        }

        chunk.generateSkylightMap();
        return chunk;
    }

    static Block pickBlockFromList(float value, List<Block> list) {
        float w = value - floor(value);
        int index = (int) (w * list.size());
        return list.get(index);
    }

    private static int floor(float x) {
        if (x < 0) {
            return (int) x - 1;
        } else {
            return (int) x;
        }
    }
}
