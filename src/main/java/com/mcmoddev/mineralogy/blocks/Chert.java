package com.mcmoddev.mineralogy.blocks;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class Chert extends Rock {

    public Chert() {
        super(false, 1.5F, 10.0F, 1, Block.soundTypeStone);
    }

    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {
        ArrayList<ItemStack> ret = new ArrayList<>();
        int quantity = quantityDropped(metadata, fortune, world.rand);

        for (int i = 0; i < quantity; i++) {
            Item item = getItemDropped(metadata, world.rand, fortune);
            int j = damageDropped(metadata);

            if (item != null) {
                ret.add(new ItemStack(item, 1, j));
            }
        }

        if (fortune > 0 && world.rand.nextInt(10) == 0) {
            ret.add(new ItemStack(Items.flint, 1 + Math.max(0, fortune)));
        }

        return ret;
    }
}
