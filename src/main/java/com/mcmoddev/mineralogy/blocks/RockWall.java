package com.mcmoddev.mineralogy.blocks;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 *
 * @author Jasmine Iwanek
 *
 */
public class RockWall extends net.minecraft.block.BlockWall {

    public RockWall(Block materialBlock, float hardness, float blastResistance, int toolHardnessLevel,
        SoundType sound) {
        super(materialBlock);
        this.setStepSound(sound);
        this.blockHardness = hardness;
        this.blockResistance = blastResistance;
        this.setHarvestLevel("pickaxe", toolHardnessLevel);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item itemIn, CreativeTabs tab, List list) {
        list.add(new ItemStack(itemIn, 1, 0));
    }
}
