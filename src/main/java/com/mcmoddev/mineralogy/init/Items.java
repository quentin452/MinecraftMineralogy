package com.mcmoddev.mineralogy.init;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import com.mcmoddev.mineralogy.Constants;
import com.mcmoddev.mineralogy.Mineralogy;
import com.mcmoddev.mineralogy.ioc.MinIoC;
import com.mcmoddev.mineralogy.items.MineralFertilizer;
import com.mcmoddev.mineralogy.lib.exceptions.ItemNotFoundException;
import com.mcmoddev.mineralogy.lib.exceptions.TabNotFoundException;
import com.mcmoddev.mineralogy.lib.interfaces.IDynamicTabProvider;
import com.mcmoddev.mineralogy.util.RegistrationHelper;

import cpw.mods.fml.common.registry.GameRegistry;

public class Items {

    private static boolean initDone = false;

    protected Items() {
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

        Item gypsumPowder = addDust(Constants.GYPSUM);;
        Item chalkPowder = addDust(Constants.CHALK);;
        Item rocksaltPowder = addDust(Constants.ROCKSALT);;
        Item sulphurPowder = addDust(Constants.SULFUR);;
        Item phosphorousPowder = addDust(Constants.PHOSPHOROUS);;
        Item nitratePowder = addDust(Constants.NITRATE);

        Item mineralFertilizer = RegistrationHelper.registerItem(new MineralFertilizer(), "mineral_fertilizer")
            .setTextureName(Mineralogy.MODID + "." + "mineral_fertilizer");

        IoC.register(Item.class, gypsumPowder, Constants.DUST_GYPSUM, Mineralogy.MODID);
        IoC.register(Item.class, chalkPowder, Constants.DUST_CHALK, Mineralogy.MODID);
        IoC.register(Item.class, rocksaltPowder, Constants.DUST_ROCKSALT, Mineralogy.MODID);
        IoC.register(Item.class, sulphurPowder, Constants.SULFUR, Mineralogy.MODID);
        IoC.register(Item.class, phosphorousPowder, Constants.PHOSPHOROUS, Mineralogy.MODID);
        IoC.register(Item.class, nitratePowder, Constants.NITRATE, Mineralogy.MODID);
        IoC.register(Item.class, mineralFertilizer, Constants.FERTILIZER, Mineralogy.MODID);

        MineralogyRegistry.ItemsToRegister.put(Constants.FERTILIZER, mineralFertilizer);

        initDone = true;
    }

    private static Item addDust(String oreDictionaryName) {
        String dustName = oreDictionaryName.toLowerCase() + "_" + Constants.DUST;

        Item item = RegistrationHelper.registerItem(new Item(), dustName)
            .setTextureName(Mineralogy.MODID + "." + dustName);

        try {
            MinIoC.getInstance()
                .resolve(IDynamicTabProvider.class)
                .addToTab(item);
        } catch (TabNotFoundException | ItemNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        MineralogyRegistry.ItemsToRegister.put(Constants.DUST + oreDictionaryName, item);
        MinIoC.getInstance()
            .register(Item.class, item, Constants.DUST + oreDictionaryName, Mineralogy.MODID);

        Block block = Block.getBlockFromName(oreDictionaryName);
        ItemStack blockItemStack = new ItemStack(block);

        ItemStack outputItemStack = new ItemStack(item, 9);

        GameRegistry.addShapelessRecipe(outputItemStack, blockItemStack, blockItemStack);

        return item;
    }
}
