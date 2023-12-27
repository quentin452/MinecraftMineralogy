package cyano.mineralogy.worldgen;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import fr.iamacat.optimizationsandtweaks.utils.agrona.collections.Object2ObjectHashMap;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;

import cyano.mineralogy.Mineralogy;
import cyano.mineralogy.worldgen.math.PerlinNoise2D;

public class Geology {

    private final PerlinNoise2D geomeNoiseLayer;
    private final PerlinNoise2D rockNoiseLayer;

    private final short[] whiteNoiseArray;

    private final Map<Long, float[]> localGeomeCache = new Object2ObjectHashMap<>();
    private final Map<Long, float[]> localRockCache = new Object2ObjectHashMap<>();

    private final ExecutorService threadPool = Executors.newFixedThreadPool(
        Runtime.getRuntime()
            .availableProcessors());

    public Geology(long seed, double geomeSize, double rockLayerSize) {
        int rockLayerUndertones = 4;
        int undertoneMultiplier = 1 << (rockLayerUndertones - 1);
        geomeNoiseLayer = new PerlinNoise2D(~seed, 128, (float) geomeSize, 2);
        rockNoiseLayer = new PerlinNoise2D(
            seed,
            (float) (4 * undertoneMultiplier),
            (float) (rockLayerSize * undertoneMultiplier),
            rockLayerUndertones);

        Random random = new Random(seed);
        whiteNoiseArray = new short[256];
        for (int i = 0; i < whiteNoiseArray.length; i++) {
            whiteNoiseArray[i] = (short) random.nextInt(0x7FFF);
        }
    }

    public Block getStoneAt(int x, int y, int z) {
        long key = ((long) x << 32) | ((long) z & 0xFFFFFFFFL);
        float[] geomeValues = localGeomeCache.computeIfAbsent(key, k -> {
            double[] xs = { x };
            double[] zs = { z };
            return geomeNoiseLayer.valueAt(xs, zs);
        });

        float geome = geomeValues[0];

        float[] rockValues = localRockCache.computeIfAbsent(key, k -> {
            double[] xs = { x };
            double[] zs = { z };
            return rockNoiseLayer.valueAt(xs, zs);
        });

        int rv = (int) rockValues[0] + y;

        if (geome < -64) {
            return pickBlockFromList(rv, Mineralogy.igneousStones);
        } else if (geome < 64) {
            return pickBlockFromList(rv, Mineralogy.metamorphicStones);
        } else {
            return pickBlockFromList(rv, Mineralogy.sedimentaryStones);
        }
    }

    public void replaceStoneInChunk(int chunkX, int chunkZ, Block[] blockBuffer) {
        int height = blockBuffer.length / 256;
        int xOffset = chunkX << 4;
        int zOffset = chunkZ << 4;

        List<Callable<Void>> tasks = new ArrayList<>();

        for (int dx = 0; dx < 16; dx++) {
            int x = xOffset | dx;
            for (int dz = 0; dz < 16; dz++) {
                int z = zOffset | dz;
                int indexBase = (dx * 16 + dz) * height;
                final int[] y = { height - 1 };
                while (y[0] > 0 && blockBuffer[indexBase + y[0]] == Blocks.air) {
                    y[0]--;
                }

                Callable<Void> task = Executors.callable(() -> {
                    long key = ((long) x << 32) | ((long) z & 0xFFFFFFFFL);
                    float[] rockValues = localRockCache.computeIfAbsent(key, k -> {
                        double[] xs = { x };
                        double[] zs = { z };
                        return rockNoiseLayer.valueAt(xs, zs);
                    });

                    float[] geomeValues = localGeomeCache.computeIfAbsent(key, k -> {
                        double[] xs = { x };
                        double[] zs = { z };
                        return geomeNoiseLayer.valueAt(xs, zs);
                    });

                    int baseRockVal = (int) rockValues[0];
                    int gbase = (int) geomeValues[0];

                    for (; y[0] > 0; y[0]--) {
                        int i = indexBase + y[0];
                        if (blockBuffer[i] == Blocks.stone) {
                            int geome = gbase + y[0];
                            if (geome < -32) {
                                blockBuffer[i] = pickBlockFromList(baseRockVal + y[0], Mineralogy.igneousStones);
                            } else if (geome < 32) {
                                blockBuffer[i] = pickBlockFromList(
                                    baseRockVal + y[0] + 3,
                                    Mineralogy.metamorphicStones);
                            } else {
                                blockBuffer[i] = pickBlockFromList(
                                    baseRockVal + y[0] + 5,
                                    Mineralogy.sedimentaryStones);
                            }
                        }
                    }
                }, null);

                tasks.add(task);
            }
        }

        try {
            threadPool.invokeAll(tasks);
        } catch (InterruptedException e) {
            Thread.currentThread()
                .interrupt();
        }
    }

    public Block[] getStoneColumn(int x, int z, int height) {
        Block[] col = new Block[height];
        double[] xs = { x };
        double[] zs = { z };
        float[] rockValues = rockNoiseLayer.valueAt(xs, zs);
        float[] geomeValues = geomeNoiseLayer.valueAt(xs, zs);

        int baseRockVal = (int) rockValues[0];
        int gbase = (int) geomeValues[0];
        for (int y = 0; y < col.length; y++) {
            double geome = gbase + y;
            if (geome < -32) {
                // RockType.IGNEOUS;
                col[y] = pickBlockFromList(baseRockVal + y, Mineralogy.igneousStones);

            } else if (geome < 32) {
                // RockType.METAMORPHIC;
                col[y] = pickBlockFromList(baseRockVal + y + 3, Mineralogy.metamorphicStones);
            } else {
                // RockType.SEDIMENTARY;
                col[y] = pickBlockFromList(baseRockVal + y + 5, Mineralogy.sedimentaryStones);
            }
        }
        return col;
    }

    private Block pickBlockFromList(int value, List<Block> list) {
        return list.get(whiteNoiseArray[(value >> 3) & 0xFF] % list.size());
    }
}
