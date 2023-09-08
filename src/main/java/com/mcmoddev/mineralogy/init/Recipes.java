package com.mcmoddev.mineralogy.init;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import com.mcmoddev.mineralogy.Constants;
import com.mcmoddev.mineralogy.Mineralogy;
import com.mcmoddev.mineralogy.ioc.MinIoC;
import com.mcmoddev.mineralogy.util.BlockItemPair;

import cpw.mods.fml.common.registry.GameRegistry;

public class Recipes {

    private static boolean initDone = false;

    protected Recipes() {
        throw new IllegalAccessError("Not a instantiable class");
    }

    public static void Init() {
        if (initDone) {
            return;
        }

        MinIoC IoC = MinIoC.getInstance();

        Item mineralFertilizer = IoC.resolve(Item.class, Constants.FERTILIZER, Mineralogy.MODID);
        Item blockGypsum = IoC.resolve(BlockItemPair.class, Constants.BLOCK_GYPSUM, Mineralogy.MODID).PairedItem;
        Item blockChalk = IoC.resolve(BlockItemPair.class, Constants.BLOCK_CHALK, Mineralogy.MODID).PairedItem;
        Item blockRocksalt = IoC.resolve(BlockItemPair.class, Constants.BLOCK_ROCKSALT, Mineralogy.MODID).PairedItem;
        Item dustGypsum = IoC.resolve(Item.class, Constants.DUST_GYPSUM, Mineralogy.MODID);
        Item dustChalk = IoC.resolve(Item.class, Constants.DUST_CHALK, Mineralogy.MODID);
        Item dustRocksalt = IoC.resolve(Item.class, Constants.DUST_ROCKSALT, Mineralogy.MODID);

        Item blockRockSaltLamp = IoC.resolve(BlockItemPair.class, "rocksaltlamp", Mineralogy.MODID).PairedItem;
        Item blockRockSaltStreetLamp = IoC
            .resolve(BlockItemPair.class, "rocksaltstreetlamp", Mineralogy.MODID).PairedItem;

        ItemStack gunpowder = new ItemStack(Items.gunpowder, 4);
        Block nitrate = Block.getBlockFromName(Constants.DUST_NITRATE);
        Block sulfur = Block.getBlockFromName(Constants.DUST_SULFUR);
        GameRegistry
            .addShapelessRecipe(gunpowder, new ItemStack(Items.sugar), new ItemStack(nitrate), new ItemStack(sulfur));

        ItemStack fertilizer = new ItemStack(mineralFertilizer, 1);
        Block nitrate2 = Block.getBlockFromName(Constants.DUST_NITRATE);
        GameRegistry.addShapelessRecipe(fertilizer, nitrate2, "dustPhosphorous");

        ItemStack cobble = new ItemStack(Blocks.cobblestone, 4);
        Block stone = Block.getBlockFromName("stone");
        Block gravel = Block.getBlockFromName("gravel");
        GameRegistry.addShapelessRecipe(
            cobble,
            new ItemStack(stone),
            new ItemStack(stone),
            new ItemStack(gravel),
            new ItemStack(gravel));

        ItemStack gypsum = new ItemStack(blockGypsum, 1);
        Block gypsumDust = Block.getBlockFromName("dustGypsum");
        GameRegistry.addShapedRecipe(gypsum, "xxx", "xxx", "xxx", 'x', gypsumDust);
        Block chalkDust = Block.getBlockFromName("dustChalk");
        ItemStack chalk = new ItemStack(blockChalk, 1);
        GameRegistry.addShapedRecipe(chalk, "xx", "xx", 'x', chalkDust);

        Block rockSaltDust = Block.getBlockFromName("dustRock_salt");
        ItemStack rockSalt = new ItemStack(blockRocksalt, 1);
        GameRegistry.addShapedRecipe(rockSalt, "xx", "xx", 'x', rockSaltDust);

        Block gypsumBlock = Block.getBlockFromName(String.valueOf(blockChalk));
        ItemStack gypsumDust5 = new ItemStack(dustGypsum, 9);
        GameRegistry.addShapelessRecipe(gypsumDust5, gypsumBlock);

        Block chalkBlock = Block.getBlockFromName(String.valueOf(blockChalk));
        ItemStack chalkDust5 = new ItemStack(dustChalk, 4);
        GameRegistry.addShapelessRecipe(chalkDust5, chalkBlock);

        Block rockSaltBlock = Block.getBlockFromName(String.valueOf(blockChalk));
        ItemStack rockSaltDust5 = new ItemStack(dustRocksalt, 4);
        GameRegistry.addShapelessRecipe(rockSaltDust5, rockSaltBlock);

        Item dryWallWhite = IoC.resolve(BlockItemPair.class, Constants.DRYWALL_WHITE, Mineralogy.MODID).PairedItem;

        ItemStack paper = new ItemStack(Block.getBlockFromName(Constants.PAPER));
        Block gypsumDust10 = Block.getBlockFromName(Constants.DUST_GYPSUM);
        ItemStack drywall = new ItemStack(dryWallWhite, 3);
        GameRegistry.addShapedRecipe(drywall, "pgp", "pgp", "pgp", 'p', paper, 'g', gypsumDust10);

        ItemStack coal = new ItemStack(Items.coal);
        Block nitrate10 = Block.getBlockFromName(Constants.DUST_NITRATE);
        Block sulfur10 = Block.getBlockFromName(Constants.DUST_SULFUR);
        ItemStack gunpowder10 = new ItemStack(Items.gunpowder, 4);
        GameRegistry.addShapelessRecipe(gunpowder10, coal, nitrate10, sulfur10);

        Block carbon = Block.getBlockFromName(Constants.DUST_CARBON);
        GameRegistry.addShapelessRecipe(gunpowder10, carbon, nitrate10, sulfur10);

        Block rockSaltBlock5 = Block.getBlockFromName(Constants.BLOCK_ROCKSALT);
        ItemStack rockSaltLamp = new ItemStack(blockRockSaltLamp, 1);
        GameRegistry.addShapelessRecipe(
            rockSaltLamp,
            new ItemStack(rockSaltBlock5),
            new ItemStack(Blocks.torch),
            new ItemStack(Items.iron_ingot));

        ItemStack ironIngot = new ItemStack(Items.iron_ingot);
        ItemStack streetLamp = new ItemStack(blockRockSaltStreetLamp, 1);
        GameRegistry.addShapedRecipe(streetLamp, "x", "y", "y", 'x', "lampRocksalt", 'y', ironIngot);

        initDone = true;
    }
}
