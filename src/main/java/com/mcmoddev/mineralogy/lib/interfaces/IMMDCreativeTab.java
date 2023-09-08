package com.mcmoddev.mineralogy.lib.interfaces;

import java.util.Comparator;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public interface IMMDCreativeTab {

    boolean hasSearchBar();

    void displayAllRelevantItems(List<ItemStack> itemList);

    ItemStack createIcon();

    void setSortingAlgorithm(Comparator<ItemStack> comparator);

    void setTabIconItem();

    void setTabIconItem(Block iconBlock);

    void setTabIconItem(Item iconItem);

    void setTabIconItem(ItemStack iconItem);

    void Initialise();
}
