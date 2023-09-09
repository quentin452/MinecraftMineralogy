package cyano.mineralogy.worldgen;

import net.minecraft.world.WorldProviderSurface;
import net.minecraft.world.WorldType;
import net.minecraft.world.chunk.IChunkProvider;

public class MineralogyWorldProvider extends WorldProviderSurface {

    public MineralogyWorldProvider() {}

    /**
     * Returns a new chunk provider which generates chunks for this world
     */
    // todo add a config to switch to multithreading and tweaks or vanilla
    @Override
    public IChunkProvider createChunkGenerator() {
        if (terrainType == WorldType.DEFAULT_1_1) {
            return super.createChunkGenerator();
        }
        return new MineralogyChunkGeneratorTwo(
            worldObj,
            worldObj.getSeed(),
            worldObj.getWorldInfo()
                .isMapFeaturesEnabled(),
            field_82913_c,
            terrainType);
    }
      /*  return new MineralogyChunkGenerator(
            worldObj,
            worldObj.getSeed(),
            worldObj.getWorldInfo()
                .isMapFeaturesEnabled(),
            field_82913_c,
            terrainType);
    }

       */
}
