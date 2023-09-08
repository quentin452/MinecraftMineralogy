package com.mcmoddev.mineralogy.blocks;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.mcmoddev.mineralogy.Mineralogy;

import cpw.mods.fml.common.registry.GameRegistry;

public class RockSalt extends Rock {

    public RockSalt() {
        super(false, 1.5F, 10F, 1, Block.soundTypeStone);
        this.setBlockName(Mineralogy.MODID + "_rock_salt");
        this.setHardness(1.5F);
        this.setResistance(10.0F);
        this.setStepSound(Block.soundTypeStone);
        this.setBlockTextureName(Mineralogy.MODID + ":rock_salt");
    }

    @Override
    public boolean canSilkHarvest(World world, EntityPlayer player, int x, int y, int z, int metadata) {
        return true;
    }

    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {
        Item dustRockSalt = GameRegistry.findItem(Mineralogy.MODID, "dustRock_salt");
        if (dustRockSalt != null) {
            dropBlockAsItem(world, x, y, z, new ItemStack(dustRockSalt, 4, 0));
        }
        return null;
    }
}
