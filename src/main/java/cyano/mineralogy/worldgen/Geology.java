package cyano.mineralogy.worldgen;

import cyano.mineralogy.Mineralogy;
import cyano.mineralogy.worldgen.math.PerlinNoise2D;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;

import java.util.List;
import java.util.Random;

public class Geology {

    private final PerlinNoise2D geomeNoiseLayer;
    private final PerlinNoise2D rockNoiseLayer;

    private final short[] whiteNoiseArray;

    public Geology(long seed, double geomeSize, double rockLayerSize) {
        int rockLayerUndertones = 4;
        int undertoneMultiplier = 1 << (rockLayerUndertones - 1);
        geomeNoiseLayer = new PerlinNoise2D(~seed, 128, (float) geomeSize, 2);
        rockNoiseLayer = new PerlinNoise2D(seed, (float) (4 * undertoneMultiplier), (float) (rockLayerSize * undertoneMultiplier), rockLayerUndertones);

        Random random = new Random(seed);
        whiteNoiseArray = new short[256];
        for (int i = 0; i < whiteNoiseArray.length; i++) {
            whiteNoiseArray[i] = (short) random.nextInt(0x7FFF);
        }
    }

    public Block getStoneAt(int x, int y, int z) {
        double[] xs = { x };
        double[] zs = { z };
        float geome = geomeNoiseLayer.valueAt(xs, zs)[0];
        float rockValue = rockNoiseLayer.valueAt(xs, zs)[0];

        int rv = (int) rockValue + y;

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

        for (int dx = 0; dx < 16; dx++) {
            int x = xOffset | dx;
            for (int dz = 0; dz < 16; dz++) {
                int z = zOffset | dz;
                int indexBase = (dx * 16 + dz) * height;
                int y = height - 1;

                while (y > 0 && blockBuffer[indexBase + y] == Blocks.air) {
                    y--;
                }

                double[] xs = { x };
                double[] zs = { z };
                float[] rockValues = rockNoiseLayer.valueAt(xs, zs);
                float[] geomeValues = geomeNoiseLayer.valueAt(xs, zs);

                int baseRockVal = (int) rockValues[0];
                int gbase = (int) geomeValues[0];

                for (; y > 0; y--) {
                    int i = indexBase + y;
                    if (blockBuffer[i] == Blocks.stone) {
                        int geome = gbase + y;
                        if (geome < -32) {
                            blockBuffer[i] = pickBlockFromList(baseRockVal + y, Mineralogy.igneousStones);
                        } else if (geome < 32) {
                            blockBuffer[i] = pickBlockFromList(baseRockVal + y + 3, Mineralogy.metamorphicStones);
                        } else {
                            blockBuffer[i] = pickBlockFromList(baseRockVal + y + 5, Mineralogy.sedimentaryStones);
                        }
                    }
                }
            }
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
            double geome = (double)gbase + y;
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
