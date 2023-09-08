package com.mcmoddev.mineralogy.blocks;

import net.minecraft.block.Block;

public class RockStairs extends net.minecraft.block.BlockStairs {

    public RockStairs(Block materialBlock, float hardness, float blastResistance, int toolHardnessLevel,
        SoundType sound) {
        super(materialBlock, 2);
        this.setStepSound(sound); // sound for stone
        this.setHardness(hardness); // dirt is 0.5, grass is 0.6, stone is 1.5,iron ore is 3, obsidian is 50
        this.setResistance(blastResistance); // dirt is 0, iron ore is 5, stone is 10, obsidian is 2000
        this.setHarvestLevel("pickaxe", toolHardnessLevel);
        this.useNeighborBrightness = true;

    }
}
