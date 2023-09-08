package com.mcmoddev.mineralogy.util;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;

import com.mcmoddev.mineralogy.ItemBlock.BypassItemBlock;
import com.mcmoddev.mineralogy.Mineralogy;
import com.mcmoddev.mineralogy.init.MineralogyRegistry;
import com.mcmoddev.mineralogy.ioc.MinIoC;
import com.mcmoddev.mineralogy.lib.exceptions.ItemNotFoundException;

public class RegistrationHelper {

    public static BlockItemPair registerBlock(Block block, String name, String oreDictionaryName) {
        return registerBlock(block, name, oreDictionaryName, true, 64, false);
    }

    public static BlockItemPair registerBlock(Block block, String name, String oreDictionaryName, int maxStackSize) {
        return registerBlock(block, name, oreDictionaryName, true, maxStackSize, false);
    }

    public static BlockItemPair registerBlock(Block block, String name, String oreDictionaryName, boolean addToTab,
        int maxStackSize, boolean bypassSneak) {
        block.setBlockName(Mineralogy.MODID + "." + name);
        block.setBlockTextureName(name);
        Item item = null;

        if (addToTab) {
            if (bypassSneak) item = registerItem(new BypassItemBlock(block), name, maxStackSize);
            else item = registerItem(new ItemBlock(block), name, maxStackSize);
        } else {
            oreDictionaryName = "ITEMLESS" + oreDictionaryName;
        }
        MinIoC IoC = MinIoC.getInstance();

        BlockItemPair pair = new BlockItemPair(block, item);

        IoC.register(BlockItemPair.class, pair, name, Mineralogy.MODID);

        MineralogyRegistry.BlocksToRegister.put(oreDictionaryName, block);
        MineralogyRegistry.MineralogyBlockRegistry.put(name, pair);

        return pair;
    }

    public static Item registerItem(Item item, String name) {
        return registerItem(item, name, 64);
    }

    public static Item registerItem(Item item, String name, int maxStackSize) {
        String itemName = Mineralogy.MODID + "." + name;

        item.setUnlocalizedName(itemName);
        item.setTextureName(name);
        item.setMaxStackSize(maxStackSize);

        MineralogyRegistry.MineralogyItemRegistry.put(name, item);
        return item;
    }
}
