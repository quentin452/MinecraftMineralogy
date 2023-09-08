package com.mcmoddev.mineralogy.blocks;

import java.util.ArrayList;

import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.mcmoddev.mineralogy.MineralogyConfig;

public class Rock extends net.minecraft.block.Block {

    public Rock(boolean isStoneEquivalent, float hardness, float blastResistance, int toolHardnessLevel,
        SoundType sound) {
        super(Material.rock);
        this.isStoneEquivalent = isStoneEquivalent;
        this.setHardness(hardness); // dirt is 0.5, grass is 0.6, stone is 1.5,iron ore is 3, obsidian is 50
        this.setResistance(blastResistance); // dirt is 0, iron ore is 5, stone is 10, obsidian is 2000
        this.setStepSound(sound); // sound for stone
        this.setHarvestLevel("pickaxe", toolHardnessLevel);
    }

    public final boolean isStoneEquivalent;

    @Override
    public boolean canReplace(World worldIn, int x, int y, int z, int side, ItemStack itemIn) {
        return isStoneEquivalent;
    }

    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {
        ArrayList<ItemStack> ret = new ArrayList<>();
        int count = quantityDropped(metadata, fortune, world.rand);

        for (int i = 0; i < count; i++) {
            Item item = getItemDropped(metadata, world.rand, fortune);

            if (MineralogyConfig.dropCobblestone()) {
                item = Item.getItemFromBlock(Blocks.cobblestone);
            }

            ret.add(new ItemStack(item, 1, damageDropped(metadata)));
        }

        return ret;
    }

}
