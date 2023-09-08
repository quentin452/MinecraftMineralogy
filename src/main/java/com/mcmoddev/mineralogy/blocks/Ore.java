package com.mcmoddev.mineralogy.blocks;

import java.util.Random;

import net.minecraft.block.BlockOre;
import net.minecraft.item.Item;
import net.minecraft.world.IBlockAccess;

import com.mcmoddev.mineralogy.Mineralogy;

public class Ore extends BlockOre {

    private final Item dropItem;
    private final int dropAdduct;
    private final int dropRange;

    public Ore(String name, Item oreDrop, int minNumberDropped, int maxNumberDropped, int pickLevel) {
        super();
        this.setStepSound(soundTypeStone); // sound for stone
        this.setBlockName(Mineralogy.MODID + "_" + name);
        this.setHardness((float) 1.5); // dirt is 0.5, grass is 0.6, stone is 1.5,iron ore is 3, obsidian is 50
        this.setResistance((float) 5); // dirt is 0, iron ore is 5, stone is 10, obsidian is 2000
        this.setHarvestLevel("pickaxe", pickLevel);

        dropItem = oreDrop;
        dropAdduct = minNumberDropped;
        dropRange = (maxNumberDropped - minNumberDropped) + 1;
    }

    @Override
    public int getExpDrop(IBlockAccess worldIn, int meta, int fortune) {
        return 0; // XP comes from smelting
    }

    @Override
    public int quantityDropped(Random random) {
        return random.nextInt(dropRange) + dropAdduct;
    }

    @Override
    public int quantityDroppedWithBonus(int fortune, Random random) {
        return this.quantityDropped(random);
    }

    @Override
    public Item getItemDropped(int meta, Random random, int fortune) {
        return dropItem;
    }

    @Override
    public int damageDropped(int meta) {
        return 0;
    }
}
