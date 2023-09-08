package com.mcmoddev.mineralogy.util;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;

import com.mcmoddev.mineralogy.Mineralogy;

public class MineralogyTab extends CreativeTabs {

    public MineralogyTab(String label) {
        super(label);
    }

    public static Item conglomeratesmoothbrick;


    @Override
    public String getTranslatedTabLabel() {
        return "§4Mineralogy Continuation"; // change this to the desired name of the creative tab // minecraft code
                                            // colors : https://minecraft.fr/faq/code-couleur-minecraft/
    }

    @Override
    public Item getTabIconItem() {
        return Items.bed;
    }
}
