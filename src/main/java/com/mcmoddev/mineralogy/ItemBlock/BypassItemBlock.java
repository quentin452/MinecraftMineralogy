package com.mcmoddev.mineralogy.ItemBlock;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.mcmoddev.mineralogy.blocks.RockSlab;

public class BypassItemBlock extends ItemBlock {

    public BypassItemBlock(Block block) {
        super(block);
        // TODO Auto-generated constructor stub
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side,
        float hitX, float hitY, float hitZ) {

        if (!world.isRemote) {

            Block block = world.getBlock(x, y, z);

            if (block instanceof RockSlab && player.isSneaking()) {
                // bypass use
                return true;
            }

        }

        return super.onItemUse(stack, player, world, x, y, z, side, hitX, hitY, hitZ);

    }
}
