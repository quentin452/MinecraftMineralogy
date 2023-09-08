package com.mcmoddev.mineralogy.patching;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;

/**
 * Created by Chris on 5/26/2016.
 */
public class UpdateBlock extends Block {

    private final Block updateTo;

    public UpdateBlock(Block replacement) {
        super(Material.rock);
        this.setTickRandomly(true);
        this.updateTo = replacement;
    }

    @Override
    public void updateTick(World worldIn, int x, int y, int z, Random random) {
        final int minX;
        final int minY;
        final int minZ;
        minX = x & ~0x0F;
        minY = y & ~0x0F;
        minZ = z & ~0x0F;
        final int maxX = minX | 0x0F;
        final int maxY = minY | 0x0F;
        final int maxZ = minZ | 0x0F;
        for (int posY = minY; posY <= maxY; posY++) {
            for (int posZ = minZ; posZ <= maxZ; posZ++) {
                for (int posX = minX; posX <= maxX; posX++) {
                    // replace blocks
                    ChunkCoordinates target = new ChunkCoordinates(posX, posY, posZ);
                    Block targetBlock = worldIn.getBlock(target.posX, target.posY, target.posZ);
                    if (targetBlock == this) {
                        worldIn.setBlock(target.posX, target.posY, target.posZ, updateTo);
                    }
                }
            }
        }
    }
}
