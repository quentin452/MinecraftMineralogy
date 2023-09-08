package com.mcmoddev.mineralogy.blocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;

import com.mcmoddev.mineralogy.Mineralogy;
import com.mcmoddev.mineralogy.ioc.MinIoC;

public class Chalk extends Block {

    public Chalk() {
        super(Material.rock);
        this.setHardness(0.75F);
        this.setResistance(1.0F);
        this.setStepSound(Block.soundTypeStone);
        this.setBlockName(Mineralogy.MODID + "_chalk");
    }

    @Override
    public boolean canSilkHarvest() {
        return true;
    }

    @Override
    public Item getItemDropped(int meta, Random random, int fortune) {
        return Item.getItemFromBlock(
            MinIoC.getInstance()
                .resolve(Block.class, "dustChalk", Mineralogy.MODID));
    }
}
