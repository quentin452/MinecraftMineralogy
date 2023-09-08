package com.mcmoddev.mineralogy.init;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import com.mcmoddev.mineralogy.Constants;
import com.mcmoddev.mineralogy.Mineralogy;
import com.mcmoddev.mineralogy.MineralogyConfig;
import com.mcmoddev.mineralogy.MineralogyLogger;
import com.mcmoddev.mineralogy.blocks.Ore;
import com.mcmoddev.mineralogy.blocks.Rock;
import com.mcmoddev.mineralogy.ioc.MinIoC;
import com.mcmoddev.mineralogy.lib.exceptions.ItemNotFoundException;
import com.mcmoddev.mineralogy.lib.exceptions.TabNotFoundException;
import com.mcmoddev.mineralogy.lib.interfaces.IDynamicTabProvider;
import com.mcmoddev.mineralogy.util.BlockItemPair;
import com.mcmoddev.mineralogy.util.RegistrationHelper;
import com.mcmoddev.mineralogy.worldgen.OreSpawner;

import cpw.mods.fml.common.registry.GameRegistry;

public class Ores {

    private static boolean initDone = false;

    private static int oreWeightCount = 20;

    protected Ores() {
        throw new IllegalAccessError("Not a instantiable class");
    }

    public static void Init() {
        try {
            if (initDone) {
                return;
            }

            MinIoC IoC = MinIoC.getInstance();

            final String ORES = "ores";

            Item sulfurPowder = IoC.resolve(Item.class, "dustSulfur", Mineralogy.MODID);
            Item phosphorousPowder = IoC.resolve(Item.class, "dustPhosphorous", Mineralogy.MODID);
            Item nitratePowder = IoC.resolve(Item.class, "dustNitrate", Mineralogy.MODID);

            // register ores
            addOre(
                Constants.SULFUR,
                sulfurPowder,
                1,
                4,
                0,
                MineralogyConfig.config()
                    .getInt("sulfur_ore.minY", ORES, 16, 1, 255, "Minimum ore spawn height"),
                MineralogyConfig.config()
                    .getInt("sulfur_ore.maxY", ORES, 64, 1, 255, "Maximum ore spawn height"),
                MineralogyConfig.config()
                    .getFloat("sulfur_ore.frequency", ORES, 1, 0, 63, "Number of ore deposits per chunk"),
                MineralogyConfig.config()
                    .getInt("sulfur_ore.quantity", ORES, 16, 0, 63, "Size of ore deposit"));

            addOre(
                Constants.PHOSPHOROUS,
                phosphorousPowder,
                1,
                4,
                0,
                MineralogyConfig.config()
                    .getInt("phosphorous_ore.minY", ORES, 16, 1, 255, "Minimum ore spawn height"),
                MineralogyConfig.config()
                    .getInt("phosphorous_ore.maxY", ORES, 64, 1, 255, "Maximum ore spawn height"),
                MineralogyConfig.config()
                    .getFloat("phosphorous_ore.frequency", ORES, 1, 0, 63, "Number of ore deposits per chunk"),
                MineralogyConfig.config()
                    .getInt("phosphorous_ore.quantity", ORES, 16, 0, 63, "Size of ore deposit"));

            addOre(
                Constants.NITRATE,
                nitratePowder,
                1,
                4,
                0,
                MineralogyConfig.config()
                    .getInt("nitrate_ore.minY", ORES, 16, 1, 255, "Minimum ore spawn height"),
                MineralogyConfig.config()
                    .getInt("nitrate_ore.maxY", ORES, 64, 1, 255, "Maximum ore spawn height"),
                MineralogyConfig.config()
                    .getFloat("nitrate_ore.frequency", ORES, 1, 0, 63, "Number of ore deposits per chunk"),
                MineralogyConfig.config()
                    .getInt("nitrate_ore.quantity", ORES, 16, 0, 63, "Size of ore deposit"));

            MineralogyConfig.config()
                .save();

            initDone = true;
        } catch (NullPointerException e) {
            MineralogyLogger.LOGGER.error("Error loading configuration", e);
        }
    }

    private static Block addOre(String oreDictionaryName, Item oreDropItem, int numMin, int numMax, int pickLevel,
        int minY, int maxY, float spawnFrequency, int spawnQuantity) {
        String oreName = oreDictionaryName.toLowerCase() + "_" + Constants.ORE;

        Block oreBlock = new Ore(oreName, oreDropItem, numMin, numMax, pickLevel)
            .setBlockName(Mineralogy.MODID + "." + oreName);

        RegistrationHelper.registerBlock(oreBlock, oreName, Constants.ORE + oreDictionaryName);

        GameRegistry.registerWorldGenerator(
            new OreSpawner(oreBlock, minY, maxY, spawnFrequency, spawnQuantity, (oreWeightCount * 25214903917L) + 11L),
            oreWeightCount++);

        addBlock(oreDictionaryName, 0, oreDropItem);

        try {
            MinIoC.getInstance()
                .resolve(IDynamicTabProvider.class)
                .addToTab(oreBlock);
        } catch (TabNotFoundException | ItemNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return oreBlock;
    }

    private static Block addBlock(String oreDictionaryName, int pickLevel, Item dust) {
        String name = oreDictionaryName.toLowerCase() + "_block";

        BlockItemPair pair = RegistrationHelper.registerBlock(
            new Rock(false, (float) 1.5, (float) 10, 0, Block.soundTypeStone),
            name,
            Constants.BLOCK.toLowerCase() + oreDictionaryName);
        Block block = Block.getBlockFromName(oreDictionaryName);
        ItemStack blockItemStack = new ItemStack(block);

        ItemStack dustItemStack = new ItemStack(dust);

        ItemStack outputItemStack = new ItemStack(pair.PairedItem);
        GameRegistry.addShapedRecipe(outputItemStack, new String[] { "xxx", "xxx", "xxx" }, 'x', dustItemStack);

        ItemStack dustOutput = new ItemStack(dust, 9);
        GameRegistry.addShapelessRecipe(dustOutput, blockItemStack, blockItemStack);

        return pair.PairedBlock;
    }
}
