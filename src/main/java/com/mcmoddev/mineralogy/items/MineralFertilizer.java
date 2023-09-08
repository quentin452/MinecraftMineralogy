package com.mcmoddev.mineralogy.items;

import com.mcmoddev.mineralogy.Mineralogy;
import com.mcmoddev.mineralogy.blocks.Chert;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

public class MineralFertilizer extends Item {
    public MineralFertilizer() {
        super();
    }

    private final ItemStack phantomBonemeal = new ItemStack(Items.dye, 1, 15);

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side,
        float hitX, float hitY, float hitZ) {

        ItemStack heldItem = player.getCurrentEquippedItem();

        boolean success = ItemDye.applyBonemeal(heldItem, world, x, y, z, player);

        if (success) {

            phantomBonemeal.setItemDamage(15);
            phantomBonemeal.stackSize = 27;

            for (int dx = -2; dx <= 2; dx++) {
                for (int dy = -2; dy <= 2; dy++) {
                    for (int dz = -1; dz <= 1; dz++) {
                        if ((dx | dy | dz) == 0) continue;
                        ItemDye.applyBonemeal(phantomBonemeal, world, x + dx, y + dy, z + dz, player);
                    }
                }
            }

            return true;
        }

        return false;

    }
}
