package com.mcmoddev.mineralogy.blocks;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class DoubleSlab extends net.minecraft.block.Block {

    private net.minecraft.block.Block _drops;

    public DoubleSlab(float hardness, float blastResistance, int toolHardnessLevel, net.minecraft.block.Block drops) {
        super(Material.rock);
        this.setHardness(hardness);
        this.setResistance(blastResistance);
        this.setStepSound(Block.soundTypeStone);
        this.setHarvestLevel("pickaxe", toolHardnessLevel);
        _drops = drops;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Item getItem(World worldIn, int x, int y, int z) {
        return Item.getItemFromBlock(_drops);
    }

    @Override
    public void harvestBlock(World worldIn, EntityPlayer player, int x, int y, int z, int meta) {
        player.addStat(StatList.mineBlockStatArray[getIdFromBlock(this)], 1);
        player.addExhaustion(0.025F);

        if (this.canSilkHarvest(worldIn, player, x, y, z, meta) && EnchantmentHelper.getSilkTouchModifier(player)) {
            ArrayList<ItemStack> items = new ArrayList<ItemStack>();
            ItemStack itemstack = this.createStackedBlock(meta);

            if (itemstack != null) {
                items.add(itemstack);
            }

            ForgeEventFactory.fireBlockHarvesting(items, worldIn, this, x, y, z, meta, 0, 1.0f, true, player);
            for (ItemStack is : items) {
                this.dropBlockAsItem(worldIn, x, y, z, is);
            }
        } else {
            harvesters.set(player);
            int i1 = EnchantmentHelper.getFortuneModifier(player);
            this.dropBlockAsItem(worldIn, x, y, z, meta, i1);
            harvesters.set(null);
        }
    }

    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {
        ArrayList<ItemStack> ret = new ArrayList<ItemStack>();

        int count = quantityDropped(metadata, fortune, world.rand);
        for (int i = 0; i < count; i++) {
            Item item = getItemDropped(metadata, world.rand, fortune);
            if (item != null) {
                ret.add(new ItemStack(_drops, 2, damageDropped(metadata)));
            }
        }
        return ret;
    }
}
