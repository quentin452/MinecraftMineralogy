package com.mcmoddev.mineralogy.blocks;

import static com.mcmoddev.mineralogy.Mineralogy.MODID;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import cpw.mods.fml.common.registry.GameRegistry;

public class Gypsum extends Rock {

    private final Random prng = new Random();
    private static final String ITEM_NAME = "gypsum";

    public Gypsum() {
        super(false, (float) 0.75, (float) 1, 0, Block.soundTypeStone);
        this.setBlockName(MODID + "_" + ITEM_NAME);
    }

    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {
        ArrayList<ItemStack> ret = new ArrayList<>();
        Item dustGypsum = GameRegistry.findItem(MODID, "dustGypsum");

        int quantity = quantityDropped(0, fortune, world.rand);

        for (int i = 0; i < quantity; i++) {

            if (dustGypsum != null) {
                ret.add(new ItemStack(dustGypsum, 1 + world.rand.nextInt(3)));
            }

        }
        return ret;
    }
}
