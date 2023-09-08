package com.mcmoddev.mineralogy.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class Soil extends Block {

    public Soil(String translationKey) {
        super(Material.ground);
        this.setBlockName(translationKey);
        this.setStepSound(soundTypeGrass);
        this.setHarvestLevel("shovel", 0);
        this.setHardness(0.5f); // dirt is 0.5, grass is 0.6, stone is 1.5,iron ore is 3, obsidian is 50
        this.setResistance(0f); // dirt is 0, iron ore is 5, stone is 10, obsidian is 2000
    }

    /**
     * Determines if the current block is replaceable by Ore veins during world
     * generation.
     * 
     * The current world
     * 
     * @return True to allow this block to be replaced by a ore
     */
    @Override
    public boolean canReplace(World worldIn, int x, int y, int z, int side, ItemStack itemIn) {
        return true;
    }
}
