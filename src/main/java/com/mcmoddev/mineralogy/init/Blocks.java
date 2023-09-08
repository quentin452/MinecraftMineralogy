package com.mcmoddev.mineralogy.init;

import com.mcmoddev.mineralogy.MineralogyLogger;
import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import com.mcmoddev.mineralogy.Constants;
import com.mcmoddev.mineralogy.Mineralogy;
import com.mcmoddev.mineralogy.MineralogyConfig;
import com.mcmoddev.mineralogy.blocks.*;
import com.mcmoddev.mineralogy.data.Material;
import com.mcmoddev.mineralogy.data.MaterialData;
import com.mcmoddev.mineralogy.ioc.MinIoC;
import com.mcmoddev.mineralogy.lib.interfaces.IDynamicTabProvider;
import com.mcmoddev.mineralogy.tileentity.TileEntityRockFurnace;
import com.mcmoddev.mineralogy.util.BlockItemPair;
import com.mcmoddev.mineralogy.util.RegistrationHelper;

import cpw.mods.fml.common.registry.GameRegistry;

public class Blocks {

    private static boolean initDone = false;

    protected Blocks() {
        throw new IllegalAccessError("Not a instantiable class");
    }

    /**
     *
     */
    public static void init() {
        if (initDone) {
            return;
        }

        MinIoC IoC = MinIoC.getInstance();

        BlockItemPair blockChert;
        BlockItemPair blockGypsum;
        BlockItemPair blockChalk;
        BlockItemPair blockRocksalt;
        BlockItemPair blockPumice;
        BlockItemPair[] drywalls = new BlockItemPair[16];

        GameRegistry.registerTileEntity(TileEntityRockFurnace.class, "rockfurnace");

        MaterialData.toArray()
            .forEach(material -> addStoneType(material));

        MineralogyRegistry.sedimentaryStones.add(net.minecraft.init.Blocks.sandstone);

        blockChert = RegistrationHelper.registerBlock(new Chert(), Constants.CHERT, Constants.BLOCK_CHERT);
        MineralogyRegistry.sedimentaryStones.add(blockChert.PairedBlock);
        MineralogyRegistry.BlocksToRegister.put(Constants.COBBLESTONE, blockChert.PairedBlock);

        blockGypsum = RegistrationHelper
            .registerBlock(new Gypsum(), Constants.GYPSUM.toLowerCase(), Constants.BLOCK_GYPSUM);
        MineralogyRegistry.sedimentaryStones.add(blockGypsum.PairedBlock);

        blockChalk = RegistrationHelper
            .registerBlock(new Chalk(), Constants.CHALK.toLowerCase(), Constants.BLOCK_CHALK);
        MineralogyRegistry.sedimentaryStones.add(blockChalk.PairedBlock);

        blockRocksalt = RegistrationHelper
            .registerBlock(new RockSalt(), Constants.ROCKSALT.toLowerCase(), Constants.BLOCK_ROCKSALT);
        MineralogyRegistry.sedimentaryStones.add(blockRocksalt.PairedBlock);

        addStoneType(MaterialData.ROCK_SALT, blockRocksalt);

        IoC.register(BlockItemPair.class, blockGypsum, Constants.BLOCK_GYPSUM, Mineralogy.MODID);
        IoC.register(BlockItemPair.class, blockChalk, Constants.BLOCK_CHALK, Mineralogy.MODID);
        IoC.register(BlockItemPair.class, blockRocksalt, Constants.BLOCK_ROCKSALT, Mineralogy.MODID);

        blockPumice = RegistrationHelper.registerBlock(
            new Rock(false, 0.5F, 5F, 0, Block.soundTypeStone),
            Constants.PUMICE,
            Constants.BLOCK_PUMICE);
        MineralogyRegistry.igneousStones.add(blockPumice.PairedBlock);
        MineralogyRegistry.BlocksToRegister.put(Constants.COBBLESTONE, blockPumice.PairedBlock);

        IoC.register(BlockItemPair.class, blockPumice, Constants.BLOCK_PUMICE, Mineralogy.MODID);

        RegistrationHelper.registerBlock(new RockSaltLamp(), "rocksaltlamp", "lampRocksalt");
        RegistrationHelper.registerBlock(new RockSaltStreetLamp(), "rocksaltstreetlamp", "lampRocksaltStreet", 16);

        IDynamicTabProvider tabProvider = IoC.resolve(IDynamicTabProvider.class);

        for (int i = 0; i < 16; i++) {
            if (MineralogyConfig.groupCreativeTabItemsByType())
                tabProvider.setTabItemMapping("Item", Constants.DRYWALL + "_" + Constants.colorSuffixes[i]);

            drywalls[i] = RegistrationHelper.registerBlock(
                new DryWall(
                    Constants.colorSuffixes[i],
                    Constants.colorSuffixes[i],
                    net.minecraft.block.material.Material.rock,
                    true),
                Constants.DRYWALL + "_" + Constants.colorSuffixes[i],
                Constants.DRYWALL + Constants.colorSuffixesTwo[i]);

            IoC.register(
                BlockItemPair.class,
                drywalls[i],
                Constants.DRYWALL + Constants.colorSuffixesTwo[i],
                Mineralogy.MODID);
            ItemStack drywallItemStack = new ItemStack(drywalls[i].PairedItem, 1);
            ItemStack dyeItemStack = new ItemStack(Items.dye, 1, i);
            ItemStack outputItemStack = new ItemStack(drywalls[i].PairedItem, 1);
            ItemStack drywallwhite = new ItemStack(Block.getBlockFromName(Constants.DRYWALL_WHITE), 1);
            GameRegistry.addShapelessRecipe(outputItemStack, drywallItemStack,drywallwhite, dyeItemStack);
        }

        initDone = true;
    }

    private static void generateReliefs(String materialName, double hardness, double blastResistance,
        int toolHardnessLevel, final BlockItemPair rock) {

        String oreDictName = "stone" + materialName.substring(0, 1)
            .toUpperCase() + materialName.substring(1) + "Smooth";

        final BlockItemPair blankRelief = RegistrationHelper.registerBlock(
            new RockRelief((float) hardness, (float) blastResistance / 2, toolHardnessLevel, Block.soundTypeStone),
            materialName + "_relief_blank",
            Constants.RELIEF + "Blank" + materialName);
        ItemStack outputItemStack = new ItemStack(blankRelief.PairedItem, 16);
        try {
            GameRegistry.addShapedRecipe(outputItemStack, "xxx", "xxx", "xxx", 'x', oreDictName);
        } catch(NullPointerException e) {
            MineralogyLogger.LOGGER.error("Error adding recipe", e);
        }
        final BlockItemPair axeRelief = RegistrationHelper.registerBlock(
            new RockRelief((float) hardness, (float) blastResistance / 2, toolHardnessLevel, Block.soundTypeStone),
            materialName + "_relief_axe",
            Constants.RELIEF + "Axe" + materialName);
        ItemStack outputItemStack1 = new ItemStack(axeRelief.PairedItem, 8);
        ItemStack blankRelief2 = new ItemStack(blankRelief.PairedBlock, 1);
        GameRegistry.addShapelessRecipe(
            outputItemStack1,
            blankRelief2,
            blankRelief2,
            blankRelief2,
            blankRelief2,
            blankRelief2,
            blankRelief2,
            blankRelief2,
            blankRelief2,
            Items.stone_axe);

        final BlockItemPair crossRelief = RegistrationHelper.registerBlock(
            new RockRelief((float) hardness, (float) blastResistance / 2, toolHardnessLevel, Block.soundTypeStone),
            materialName + "_relief_cross",
            Constants.RELIEF + "Cross" + materialName);
        Block ingredientBlock = Block.getBlockFromName(Constants.RELIEF + "Blank" + materialName);

        ItemStack outputItemStack10 = new ItemStack(crossRelief.PairedItem, 4);
        ItemStack ingredientItemStack = new ItemStack(ingredientBlock);

        try {
            GameRegistry.addShapedRecipe(outputItemStack10, "x x", "   ", "x x", 'x', ingredientBlock);
        } catch(NullPointerException e) {
            MineralogyLogger.LOGGER.error("Error adding recipe", e);
        }
        final BlockItemPair hammerRelief = RegistrationHelper.registerBlock(
            new RockRelief((float) hardness, (float) blastResistance / 2, toolHardnessLevel, Block.soundTypeStone),
            materialName + "_relief_hammer",
            Constants.RELIEF + "Hammer" + materialName);
        ItemStack outputItemStack4 = new ItemStack(hammerRelief.PairedItem, 7);

        ItemStack ingredient1ItemStack = new ItemStack(
            Block.getBlockFromName(Constants.RELIEF + "Blank" + materialName));
        ItemStack ingredient2ItemStack = new ItemStack(Items.stick);

        Block oreBlock = Block.getBlockFromName(oreDictName);
        ItemStack oreItemStack = new ItemStack(oreBlock);

        GameRegistry.addShapedRecipe(
            outputItemStack4,
            "zxz",
            "zyz",
            "zzz",
            'x',
            oreItemStack,
            'y',
            ingredient2ItemStack,
            'z',
            ingredient1ItemStack);

        final BlockItemPair hoeRelief = RegistrationHelper.registerBlock(
            new RockRelief((float) hardness, (float) blastResistance / 2, toolHardnessLevel, Block.soundTypeStone),
            materialName + "_relief_hoe",
            Constants.RELIEF + "Hoe" + materialName);
        ItemStack outputItemStack5 = new ItemStack(hoeRelief.PairedItem, 8);

        Block ingredientBlock2 = Block.getBlockFromName(Constants.RELIEF + "Blank" + materialName);
        ItemStack ingredientItemStack10 = new ItemStack(ingredientBlock2);

        ItemStack toolItemStack = new ItemStack(Items.stone_hoe);

        GameRegistry.addShapelessRecipe(
            outputItemStack5,
            ingredientItemStack10,
            ingredientItemStack10,
            ingredientItemStack10,
            ingredientItemStack10,
            ingredientItemStack10,
            ingredientItemStack10,
            ingredientItemStack10,
            ingredientItemStack10,
            toolItemStack);

        final BlockItemPair horizontalRelief = RegistrationHelper.registerBlock(
            new RockRelief((float) hardness, (float) blastResistance / 2, toolHardnessLevel, Block.soundTypeStone),
            materialName + "_relief_horizontal",
            Constants.RELIEF + "Horizontal" + materialName);
        Block ingredientBlock99 = Block.getBlockFromName(Constants.RELIEF + "Blank" + materialName);
        ItemStack ingredientItemStack99 = new ItemStack(ingredientBlock99);

        ItemStack outputItemStack99 = new ItemStack(horizontalRelief.PairedItem, 3);
        GameRegistry.addShapedRecipe(outputItemStack99, "xxx", 'x', ingredientItemStack99);

        final BlockItemPair leftRelief = RegistrationHelper.registerBlock(
            new RockRelief((float) hardness, (float) blastResistance / 2, toolHardnessLevel, Block.soundTypeStone),
            materialName + "_relief_left",
            Constants.RELIEF + "Left" + materialName);
        ItemStack outputItemStack2 = new ItemStack(leftRelief.PairedItem, 3);
        GameRegistry.addShapedRecipe(outputItemStack2, "x  ", " x ", "  x", 'x', ingredientItemStack);

        final BlockItemPair pickaxeRelief = RegistrationHelper.registerBlock(
            new RockRelief((float) hardness, (float) blastResistance / 2, toolHardnessLevel, Block.soundTypeStone),
            materialName + "_relief_pickaxe",
            Constants.RELIEF + "Pickaxe" + materialName);
        Block ingredientBlock98 = Block.getBlockFromName(Constants.RELIEF + "Blank" + materialName);
        ItemStack ingredientItemStack98 = new ItemStack(ingredientBlock98);

        ItemStack outputItemStack98 = new ItemStack(pickaxeRelief.PairedItem, 8);
        ItemStack toolItemStack98 = new ItemStack(Items.stone_pickaxe);

        GameRegistry.addShapelessRecipe(
            outputItemStack98,
            ingredientItemStack98,
            ingredientItemStack98,
            ingredientItemStack98,
            ingredientItemStack98,
            ingredientItemStack98,
            ingredientItemStack98,
            ingredientItemStack98,
            ingredientItemStack98,
            toolItemStack98);

        final BlockItemPair plusRelief = RegistrationHelper.registerBlock(
            new RockRelief((float) hardness, (float) blastResistance / 2, toolHardnessLevel, Block.soundTypeStone),
            materialName + "_relief_plus",
            Constants.RELIEF + "Plus" + materialName);
        Block ingredientBlock18 = Block.getBlockFromName(Constants.RELIEF + "Blank" + materialName);
        ItemStack ingredientItemStack88 = new ItemStack(ingredientBlock18);

        ItemStack outputItemStack18 = new ItemStack(plusRelief.PairedItem, 5);
        GameRegistry.addShapedRecipe(outputItemStack18, " x ", "xxx", " x ", 'x', ingredientItemStack88);

        final BlockItemPair rightRelief = RegistrationHelper.registerBlock(
            new RockRelief((float) hardness, (float) blastResistance / 2, toolHardnessLevel, Block.soundTypeStone),
            materialName + "_relief_right",
            Constants.RELIEF + "Right" + materialName);
        ItemStack outputItemStack02 = new ItemStack(rightRelief.PairedItem, 3);
        GameRegistry.addShapedRecipe(outputItemStack02, "  x", " x ", "x  ", 'x', ingredientItemStack88);

        final BlockItemPair swordRelief = RegistrationHelper.registerBlock(
            new RockRelief((float) hardness, (float) blastResistance / 2, toolHardnessLevel, Block.soundTypeStone),
            materialName + "_relief_sword",
            Constants.RELIEF + "Sword" + materialName);
        Block ingredientBlock10 = Block.getBlockFromName(Constants.RELIEF + "Blank" + materialName);
        ItemStack ingredientItemStack20 = new ItemStack(ingredientBlock10);

        ItemStack outputItemStack01 = new ItemStack(swordRelief.PairedItem, 8);
        ItemStack toolItemStack10 = new ItemStack(Items.stone_sword);
        GameRegistry.addShapelessRecipe(
            outputItemStack01,
            ingredientItemStack20,
            ingredientItemStack20,
            ingredientItemStack20,
            ingredientItemStack20,
            ingredientItemStack20,
            ingredientItemStack20,
            ingredientItemStack20,
            ingredientItemStack20,
            toolItemStack10);

        final BlockItemPair iRelief = RegistrationHelper.registerBlock(
            new RockRelief((float) hardness, (float) blastResistance / 2, toolHardnessLevel, Block.soundTypeStone),
            materialName + "_relief_i",
            Constants.RELIEF + "I" + materialName);
        ItemStack outputItemStack22 = new ItemStack(iRelief.PairedItem, 7);
        GameRegistry.addShapedRecipe(outputItemStack22, "xxx", " x ", "xxx", 'x', ingredientItemStack);

        final BlockItemPair verticalRelief = RegistrationHelper.registerBlock(
            new RockRelief((float) hardness, (float) blastResistance / 2, toolHardnessLevel, Block.soundTypeStone),
            materialName + "_relief_vertical",
            Constants.RELIEF + "Vertical" + materialName);
        ItemStack outputItemStack3 = new ItemStack(verticalRelief.PairedItem, 3);
        GameRegistry.addShapedRecipe(outputItemStack3, "x", "x", "x", 'x', ingredientItemStack);

    }

    protected static void addStoneType(Material materialType, BlockItemPair rockPair) {

        String name = materialType.materialName.toLowerCase();
        String oreDictName = "stone" + materialType.materialName;
        float burnModifier = (float) (1 + ((materialType.hardness - 3) / 10));

        final BlockItemPair rockFurnacePair;
        final BlockItemPair rockStairPair;
        final BlockItemPair rockSlabPair;
        final BlockItemPair rockWallPair;
        final BlockItemPair brickPair;
        final BlockItemPair brickFurnacePair;
        final BlockItemPair brickStairPair;
        final BlockItemPair brickSlabPair;
        final BlockItemPair brickWallPair;
        final BlockItemPair smoothPair;
        final BlockItemPair smoothFurnacePair;
        final BlockItemPair smoothStairPair;
        final BlockItemPair smoothSlabPair;
        final BlockItemPair smoothWallPair;
        final BlockItemPair smoothBrickPair;
        final BlockItemPair smoothBrickFurnacePair;
        final BlockItemPair smoothBrickStairPair;
        final BlockItemPair smoothBrickSlabPair;
        final BlockItemPair smoothBrickWallPair;
        Block oreBlock = Block.getBlockFromName(oreDictName);
        ItemStack oreItemStack = new ItemStack(oreBlock);

        ItemStack outputItemStack = new ItemStack(Block.getBlockById(1), 4);

        ItemStack gravelItemStack = new ItemStack(Block.getBlockById(12), 1);

        GameRegistry.addShapelessRecipe(outputItemStack, oreItemStack, oreItemStack, gravelItemStack, gravelItemStack);

        switch (materialType.rockType) {
            case IGNEOUS:
                MineralogyRegistry.igneousStones.add(rockPair.PairedBlock);
                break;
            case METAMORPHIC:
                MineralogyRegistry.metamorphicStones.add(rockPair.PairedBlock);
                break;
            case SEDIMENTARY:
                MineralogyRegistry.sedimentaryStones.add(rockPair.PairedBlock);
                break;
            case ANY:
                MineralogyRegistry.sedimentaryStones.add(rockPair.PairedBlock);
                MineralogyRegistry.metamorphicStones.add(rockPair.PairedBlock);
                MineralogyRegistry.igneousStones.add(rockPair.PairedBlock);
                break;
        }

        GameRegistry.addSmelting(rockPair.PairedItem, new ItemStack(net.minecraft.init.Blocks.stone), 0.1F);

        // no point in ore dicting these recipes I think
        if (MineralogyConfig.generateRockStairs()) {
            rockStairPair = RegistrationHelper.registerBlock(
                new RockStairs(
                    rockPair.PairedBlock,
                    (float) materialType.hardness,
                    (float) materialType.blastResistance,
                    materialType.toolHardnessLevel,
                    Block.soundTypeStone),
                name + "_" + Constants.STAIRS,
                Constants.STAIRS + materialType.materialName);
            Block oreBlock10 = Block.getBlockFromName(oreDictName);
            ItemStack oreItemStak = new ItemStack(oreBlock10);

            ItemStack outputItemStak = new ItemStack(rockStairPair.PairedItem, 4);

            GameRegistry.addShapedRecipe(outputItemStak, "x  ", "xx ", "xxx", 'x', oreItemStak);
        }

        if (MineralogyConfig.generateRockSlab()) {
            rockSlabPair = RegistrationHelper.registerBlock(
                new RockSlab(
                    (float) materialType.hardness,
                    (float) materialType.blastResistance,
                    materialType.toolHardnessLevel,
                    Block.soundTypeStone,
                    name + "_double_" + Constants.SLAB),
                name + "_" + Constants.SLAB,
                Constants.SLAB + materialType.materialName,
                true,
                64,
                true);
            Block oreBlock1 = Block.getBlockFromName(oreDictName);
            ItemStack oreItemStack1 = new ItemStack(oreBlock1);

            ItemStack outputItemStack1 = new ItemStack(rockSlabPair.PairedItem, 6);

            GameRegistry.addShapedRecipe(outputItemStack1, "xxx", 'x', oreItemStack1);

            RegistrationHelper.registerBlock(
                new DoubleSlab(
                    (float) materialType.hardness,
                    (float) materialType.blastResistance,
                    materialType.toolHardnessLevel,
                    rockSlabPair.PairedBlock
                ),
                name + "_double_" + Constants.SLAB,
                Constants.SLAB + "Double" + materialType.materialName,
                false,
                64,
                false
            );
            if (MineralogyConfig.generateRockFurnace()) {
                rockFurnacePair = RegistrationHelper.registerBlock(
                    new RockFurnace(
                        (float) materialType.hardness,
                        (float) materialType.blastResistance,
                        materialType.toolHardnessLevel,
                        false,
                        burnModifier),
                    name + "_" + Constants.FURNACE,
                    Constants.FURNACE + materialType.materialName,
                    true,
                    1,
                    false);
                RegistrationHelper.registerBlock(
                    new RockFurnace(
                        (float) materialType.hardness,
                        (float) materialType.blastResistance,
                        materialType.toolHardnessLevel,
                        true,
                        burnModifier).setLightLevel(0.875F),
                    "lit_" + name + "_" + Constants.FURNACE,
                    Constants.FURNACE + "Lit" + materialType.materialName,
                    false,
                    1,
                    true);

                Block slabBlock = Block.getBlockFromName(Constants.SLAB + materialType.materialName);
                ItemStack slabItemStack = new ItemStack(slabBlock);

                Block furnaceBlock = Block.getBlockFromName("furnace");
                ItemStack furnaceItemStack = new ItemStack(furnaceBlock);
                ItemStack outputItemStack14 = new ItemStack(rockFurnacePair.PairedItem, 1);

                GameRegistry
                    .addShapedRecipe(outputItemStack14, "xxx", "xyx", "xxx", 'x', slabItemStack, 'y', furnaceItemStack);
            }
        }

        if (MineralogyConfig.generateRockWall()) {
            rockWallPair = RegistrationHelper.registerBlock(
                new RockWall(
                    rockPair.PairedBlock,
                    (float) materialType.hardness,
                    (float) materialType.blastResistance,
                    materialType.toolHardnessLevel,
                    Block.soundTypeStone),
                name + "_" + Constants.WALL,
                Constants.WALL + materialType.materialName);
            Block block = Block.getBlockFromName(oreDictName);
            ItemStack oreItemStack40 = new ItemStack(block);

            ItemStack outputItemStack40 = new ItemStack(rockWallPair.PairedItem, 6);

            GameRegistry.addShapedRecipe(outputItemStack40, "xxx", "xxx", 'x', oreItemStack40);
        }

        if (MineralogyConfig.generateBrick()) {
            brickPair = RegistrationHelper.registerBlock(
                new Rock(
                    false,
                    (float) materialType.hardness,
                    (float) materialType.blastResistance,
                    materialType.toolHardnessLevel,
                    Block.soundTypeStone),
                name + "_" + Constants.BRICK,
                "stone" + materialType.materialName + "Brick");
            Block block = Block.getBlockFromName(oreDictName);
            ItemStack oreItemStack10 = new ItemStack(block);

            ItemStack outputItemStack5 = new ItemStack(brickPair.PairedItem, 4);

            GameRegistry.addShapedRecipe(outputItemStack5, "xx", "xx", 'x', oreItemStack10);
            if (MineralogyConfig.generateBrickStairs()) {
                brickStairPair = RegistrationHelper.registerBlock(
                    new RockStairs(
                        rockPair.PairedBlock,
                        (float) materialType.hardness,
                        (float) materialType.blastResistance,
                        materialType.toolHardnessLevel,
                        Block.soundTypeStone),
                    name + "_" + Constants.BRICK + "_" + Constants.STAIRS,
                    Constants.STAIRS + materialType.materialName + "Brick");

                Block brickBlock = Block.getBlockFromName("stone" + materialType.materialName + "Brick");
                ItemStack brickItemStack = new ItemStack(brickBlock);

                ItemStack outputItemStack40 = new ItemStack(brickStairPair.PairedItem, 4);

                GameRegistry.addShapedRecipe(outputItemStack40, "x  ", "xx ", "xxx", 'x', brickItemStack);
            }

            if (MineralogyConfig.generateBrickSlab()) {
                brickSlabPair = RegistrationHelper.registerBlock(
                    new RockSlab(
                        (float) materialType.hardness,
                        (float) materialType.blastResistance,
                        materialType.toolHardnessLevel,
                        Block.soundTypeStone,
                        name + "_" + Constants.BRICK + "_double_" + Constants.SLAB),
                    name + "_" + Constants.BRICK + "_" + Constants.SLAB,
                    Constants.SLAB + materialType.materialName + "Brick",
                    true,
                    64,
                    true);
                Block brickBlock = Block.getBlockFromName("stone" + materialType.materialName + "Brick");
                ItemStack brickItemStack = new ItemStack(brickBlock);

                ItemStack outputItemStack4 = new ItemStack(brickSlabPair.PairedItem, 6);

                GameRegistry.addShapedRecipe(outputItemStack4, "xxx", 'x', brickItemStack);

                RegistrationHelper.registerBlock(
                    new DoubleSlab(
                        (float) materialType.hardness,
                        (float) materialType.blastResistance,
                        materialType.toolHardnessLevel,
                        brickSlabPair.PairedBlock),
                    name + "_" + Constants.BRICK + "_double_" + Constants.SLAB,
                    Constants.SLAB + "Double" + materialType.materialName + "Brick",
                    false,
                    64,
                    false);

                if (MineralogyConfig.generateBrickFurnace()) {
                    brickFurnacePair = RegistrationHelper.registerBlock(
                        new RockFurnace(
                            (float) materialType.hardness,
                            (float) materialType.blastResistance,
                            materialType.toolHardnessLevel,
                            false,
                            burnModifier),
                        name + "_" + Constants.BRICK + "_" + Constants.FURNACE,
                        Constants.FURNACE + materialType.materialName,
                        true,
                        1,
                        false);
                    RegistrationHelper.registerBlock(
                        new RockFurnace(
                            (float) materialType.hardness,
                            (float) materialType.blastResistance,
                            materialType.toolHardnessLevel,
                            true,
                            burnModifier).setLightLevel(0.875F),
                        "lit_" + name + "_" + Constants.BRICK + "_" + Constants.FURNACE,
                        Constants.FURNACE + "Lit" + materialType.materialName,
                        false,
                        1,
                        false);

                    Block brickBlock4 = Block.getBlockFromName(Constants.SLAB + materialType.materialName + "Brick");
                    ItemStack brickItemStack4 = new ItemStack(brickBlock4);

                    Block furnaceBlock = Block.getBlockFromName("furnace");
                    ItemStack furnaceItemStack = new ItemStack(furnaceBlock);

                    ItemStack outputItemStack44 = new ItemStack(brickFurnacePair.PairedItem, 1);

                    GameRegistry.addShapedRecipe(
                        outputItemStack44,
                        "xxx",
                        "xyx",
                        "xxx",
                        'x',
                        brickItemStack4,
                        'y',
                        furnaceItemStack);
                }
            }

            if (MineralogyConfig.generateBrickWall()) {
                brickWallPair = RegistrationHelper.registerBlock(
                    new RockWall(
                        rockPair.PairedBlock,
                        (float) materialType.hardness,
                        (float) materialType.blastResistance,
                        materialType.toolHardnessLevel,
                        Block.soundTypeStone),
                    name + "_" + Constants.BRICK + "_" + Constants.WALL,
                    Constants.WALL + materialType.materialName);
                Block brickBlock = Block.getBlockFromName("stone" + materialType.materialName + "Brick");
                ItemStack brickItemStack = new ItemStack(brickBlock);

                ItemStack outputItemStack44 = new ItemStack(brickWallPair.PairedItem, 6);

                GameRegistry.addShapedRecipe(outputItemStack44, "xxx", "xxx", 'x', brickItemStack);
            }
        }

        if (MineralogyConfig.generateSmooth()) {
            smoothPair = RegistrationHelper.registerBlock(
                new Rock(
                    false,
                    (float) materialType.hardness,
                    (float) materialType.blastResistance,
                    materialType.toolHardnessLevel,
                    Block.soundTypeStone),
                name + "_" + Constants.SMOOTH,
                "stone" + materialType.materialName + "Smooth");
            Block block = Block.getBlockFromName(oreDictName);
            ItemStack oreItemStack7 = new ItemStack(block);

            ItemStack outputItemStack7 = new ItemStack(smoothPair.PairedItem, 1);

            ItemStack sandItemStack = new ItemStack(Block.getBlockFromName("sand"), 1);

            GameRegistry.addShapelessRecipe(outputItemStack7, oreItemStack7, sandItemStack);

            if (MineralogyConfig.generateReliefs()) {
                generateReliefs(
                    name,
                    materialType.hardness,
                    materialType.blastResistance,
                    materialType.toolHardnessLevel,
                    smoothPair);
            }

            if (MineralogyConfig.generateSmoothStairs()) {
                smoothStairPair = RegistrationHelper.registerBlock(
                    new RockStairs(
                        rockPair.PairedBlock,
                        (float) materialType.hardness,
                        (float) materialType.blastResistance,
                        materialType.toolHardnessLevel,
                        Block.soundTypeStone),
                    name + "_" + Constants.SMOOTH + "_" + Constants.STAIRS,
                    Constants.STAIRS + materialType.materialName + "Smooth");
                Block smoothBlock = Block.getBlockFromName("stone" + materialType.materialName + "Smooth");
                ItemStack smoothItemStack = new ItemStack(smoothBlock);

                ItemStack outputItemStac5k = new ItemStack(smoothStairPair.PairedItem, 4);

                GameRegistry.addShapedRecipe(outputItemStac5k, "x  ", "xx ", "xxx", 'x', smoothItemStack);
            }

            if (MineralogyConfig.generateSmoothSlab()) {
                smoothSlabPair = RegistrationHelper.registerBlock(
                    new RockSlab(
                        (float) materialType.hardness,
                        (float) materialType.blastResistance,
                        materialType.toolHardnessLevel,
                        Block.soundTypeStone,
                        name + "_" + Constants.SMOOTH + "_double_" + Constants.SLAB),
                    name + "_" + Constants.SMOOTH + "_" + Constants.SLAB,
                    Constants.SLAB + materialType.materialName + "Smooth",
                    true,
                    64,
                    true);
                Block smoothBlock = Block.getBlockFromName("stone" + materialType.materialName + "Smooth");
                ItemStack smoothItemStack = new ItemStack(smoothBlock);

                ItemStack outputItem5Stack = new ItemStack(smoothSlabPair.PairedItem, 6);

                GameRegistry.addShapedRecipe(outputItem5Stack, "xxx", 'x', smoothItemStack);
                RegistrationHelper.registerBlock(
                    new DoubleSlab(
                        (float) materialType.hardness,
                        (float) materialType.blastResistance,
                        materialType.toolHardnessLevel,
                        smoothSlabPair.PairedBlock),
                    name + "_" + Constants.SMOOTH + "_double_" + Constants.SLAB,
                    Constants.SLAB + "Double" + materialType.materialName + "Smooth",
                    false,
                    64,
                    false);

                if (MineralogyConfig.generateSmoothFurnace()) {
                    smoothFurnacePair = RegistrationHelper.registerBlock(
                        new RockFurnace(
                            (float) materialType.hardness,
                            (float) materialType.blastResistance,
                            materialType.toolHardnessLevel,
                            false,
                            burnModifier),
                        name + "_" + Constants.SMOOTH + "_" + Constants.FURNACE,
                        Constants.FURNACE + materialType.materialName,
                        true,
                        1,
                        false);
                    RegistrationHelper.registerBlock(
                        new RockFurnace(
                            (float) materialType.hardness,
                            (float) materialType.blastResistance,
                            materialType.toolHardnessLevel,
                            true,
                            burnModifier).setLightLevel(0.875F),
                        "lit_" + name + "_" + Constants.SMOOTH + "_" + Constants.FURNACE,
                        Constants.FURNACE + "Lit" + materialType.materialName,
                        false,
                        1,
                        false);
                    Block smoothBloc5k = Block.getBlockFromName(Constants.SLAB + materialType.materialName + "Smooth");
                    ItemStack smoothItemStac5k = new ItemStack(smoothBloc5k);

                    Block furnaceBlock = Block.getBlockFromName("furnace");
                    ItemStack furnaceItemStack = new ItemStack(furnaceBlock);

                    ItemStack outputItemStac5k = new ItemStack(smoothFurnacePair.PairedItem, 1);

                    GameRegistry.addShapedRecipe(
                        outputItemStac5k,
                        "xxx",
                        "xyx",
                        "xxx",
                        'x',
                        smoothItemStac5k,
                        'y',
                        furnaceItemStack);
                }
            }

            if (MineralogyConfig.generateSmoothWall()) {
                smoothWallPair = RegistrationHelper.registerBlock(
                    new RockWall(
                        rockPair.PairedBlock,
                        (float) materialType.hardness,
                        (float) materialType.blastResistance,
                        materialType.toolHardnessLevel,
                        Block.soundTypeStone),
                    name + "_" + Constants.SMOOTH + "_" + Constants.WALL,
                    Constants.WALL + materialType.materialName);
                Block smoothBlock = Block.getBlockFromName("stone" + materialType.materialName + "Smooth");
                ItemStack smoothItemStack = new ItemStack(smoothBlock);

                ItemStack outputIt5emStack = new ItemStack(smoothWallPair.PairedItem, 6);

                GameRegistry.addShapedRecipe(outputIt5emStack, "xxx", "xxx", 'x', smoothItemStack);
            }

            if (MineralogyConfig.generateSmoothBrick()) {
                smoothBrickPair = RegistrationHelper.registerBlock(
                    new Rock(
                        false,
                        (float) materialType.hardness,
                        (float) materialType.blastResistance,
                        materialType.toolHardnessLevel,
                        Block.soundTypeStone),
                    name + "_" + Constants.SMOOTH + "_" + Constants.BRICK,
                    "stone" + materialType.materialName + "SmoothBrick");
                Block smoothBlock = Block.getBlockFromName("stone" + materialType.materialName + "Smooth");
                ItemStack smoothItemStack = new ItemStack(smoothBlock);

                ItemStack outputItem5Stack = new ItemStack(smoothBrickPair.PairedItem, 4);

                GameRegistry.addShapedRecipe(outputItem5Stack, "xx", "xx", 'x', smoothItemStack);

                if (MineralogyConfig.generateSmoothBrickStairs()) {
                    smoothBrickStairPair = RegistrationHelper.registerBlock(
                        new RockStairs(
                            rockPair.PairedBlock,
                            (float) materialType.hardness,
                            (float) materialType.blastResistance,
                            materialType.toolHardnessLevel,
                            Block.soundTypeStone),
                        name + "_" + Constants.SMOOTH + "_" + Constants.BRICK + "_" + Constants.STAIRS,
                        Constants.STAIRS + materialType.materialName + "SmoothBrick");
                    Block smoothBrickBlock = Block
                        .getBlockFromName("stone" + materialType.materialName + "SmoothBrick");
                    ItemStack smoothBrickItemStack = new ItemStack(smoothBrickBlock);

                    ItemStack outputItemStack55 = new ItemStack(smoothBrickStairPair.PairedItem, 4);

                    GameRegistry.addShapedRecipe(outputItemStack55, "x  ", "xx ", "xxx", 'x', smoothBrickItemStack);
                }

                if (MineralogyConfig.generateSmoothBrickSlab()) {
                    smoothBrickSlabPair = RegistrationHelper.registerBlock(
                        new RockSlab(
                            (float) materialType.hardness,
                            (float) materialType.blastResistance,
                            materialType.toolHardnessLevel,
                            Block.soundTypeStone,
                            name + "_" + Constants.SMOOTH + "_" + Constants.BRICK + "_double_" + Constants.SLAB),
                        name + "_" + Constants.SMOOTH + "_" + Constants.BRICK + "_" + Constants.SLAB,
                        Constants.SLAB + materialType.materialName + "SmoothBrick",
                        true,
                        64,
                        true);
                    Block smoothBrickBlock = Block
                        .getBlockFromName("stone" + materialType.materialName + "SmoothBrick");
                    ItemStack smoothBrickItemStack = new ItemStack(smoothBrickBlock);

                    ItemStack outputItemStack3 = new ItemStack(smoothBrickSlabPair.PairedItem, 6);

                    GameRegistry.addShapedRecipe(outputItemStack3, "xxx", 'x', smoothBrickItemStack);
                    RegistrationHelper.registerBlock(
                        new DoubleSlab(
                            (float) materialType.hardness,
                            (float) materialType.blastResistance,
                            materialType.toolHardnessLevel,
                            smoothBrickSlabPair.PairedBlock),
                        name + "_" + Constants.SMOOTH + "_" + Constants.BRICK + "_double_" + Constants.SLAB,
                        Constants.SLAB + "Double" + materialType.materialName + "SmoothBrick",
                        false,
                        64,
                        false);

                    if (MineralogyConfig.generateSmoothBrickFurnace()) {
                        smoothBrickFurnacePair = RegistrationHelper.registerBlock(
                            new RockFurnace(
                                (float) materialType.hardness,
                                (float) materialType.blastResistance,
                                materialType.toolHardnessLevel,
                                false,
                                burnModifier),
                            name + "_" + Constants.SMOOTH + "_" + Constants.BRICK + "_" + Constants.FURNACE,
                            Constants.FURNACE + materialType.materialName,
                            true,
                            1,
                            false);
                        RegistrationHelper.registerBlock(
                            new RockFurnace(
                                (float) materialType.hardness,
                                (float) materialType.blastResistance,
                                materialType.toolHardnessLevel,
                                true,
                                burnModifier).setLightLevel(0.875F),
                            "lit_" + name + "_" + Constants.SMOOTH + "_" + Constants.BRICK + "_" + Constants.FURNACE,
                            Constants.FURNACE + "Lit" + materialType.materialName,
                            false,
                            1,
                            false);

                        Block smoothBrickBlock5 = Block
                            .getBlockFromName(Constants.SLAB + materialType.materialName + "SmoothBrick");
                        ItemStack smoothBrickItemStack44 = new ItemStack(smoothBrickBlock5);

                        Block furnaceBlock = Block.getBlockFromName("furnace");
                        ItemStack furnaceItemStack = new ItemStack(furnaceBlock);

                        ItemStack outputItemStack5 = new ItemStack(smoothBrickFurnacePair.PairedItem, 1);

                        GameRegistry.addShapedRecipe(
                            outputItemStack5,
                            "xxx",
                            "xyx",
                            "xxx",
                            'x',
                            smoothBrickItemStack44,
                            'y',
                            furnaceItemStack);
                    }
                }

                if (MineralogyConfig.generateSmoothBrickWall()) {
                    smoothBrickWallPair = RegistrationHelper.registerBlock(
                        new RockWall(
                            rockPair.PairedBlock,
                            (float) materialType.hardness,
                            (float) materialType.blastResistance,
                            materialType.toolHardnessLevel,
                            Block.soundTypeStone),
                        name + "_" + Constants.SMOOTH + "_" + Constants.BRICK + "_" + Constants.WALL,
                        Constants.WALL + materialType.materialName);
                    Block smoothBrickBlock = Block
                        .getBlockFromName("stone" + materialType.materialName + "SmoothBrick");
                    ItemStack smoothBrickItemStack = new ItemStack(smoothBrickBlock);

                    ItemStack outputItemStac3k = new ItemStack(smoothBrickWallPair.PairedItem, 6);

                    GameRegistry.addShapedRecipe(outputItemStac3k, "xxx", "xxx", 'x', smoothBrickItemStack);
                }
            }
        }

    }

    protected static void addStoneType(Material materialType) {
        String name = materialType.materialName.toLowerCase();
        final BlockItemPair rockPair = RegistrationHelper.registerBlock(
            new Rock(
                true,
                (float) materialType.hardness,
                (float) materialType.blastResistance,
                materialType.toolHardnessLevel,
                Block.soundTypeStone),
            name,
            "stone" + materialType.materialName);

        if (materialType.cobbleEquivilent)
            MineralogyRegistry.BlocksToRegister.put(Constants.COBBLESTONE, rockPair.PairedBlock);

        addStoneType(materialType, rockPair);
    }
}
