package com.mcmoddev.mineralogy.blocks;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.item.Item;

import com.mcmoddev.mineralogy.Mineralogy;

public class DryWall extends net.minecraft.block.BlockPane {

    private static final String ITEM_NAME = "drywall";

    public DryWall(String p_i45432_1_, String p_i45432_2_, Material p_i45432_3_, boolean p_i45432_4_) {
        super(p_i45432_1_, p_i45432_2_, p_i45432_3_, p_i45432_4_);
        this.setBlockName(Mineralogy.MODID + "_" + ITEM_NAME + " _" + getBlockColor());
        this.useNeighborBrightness = true;
    }

    @Override
    public Item getItemDropped(int meta, Random random, int fortune) {
        return Item.getItemFromBlock(this);
    }
}
