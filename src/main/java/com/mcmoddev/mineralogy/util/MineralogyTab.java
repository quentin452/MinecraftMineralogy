package com.mcmoddev.mineralogy.util;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

import com.mcmoddev.mineralogy.Mineralogy;

public class MineralogyTab extends CreativeTabs {

    public MineralogyTab(String label) {
        super(label);
    }

    @Override
    public Item getTabIconItem() {
        return Mineralogy.mineralFertilizer;
    }

    @Override
    public String getTranslatedTabLabel() {
        return "§4Mineralogy Continuation"; // change this to the desired name of the creative tab // minecraft code
                                            // colors : https://minecraft.fr/faq/code-couleur-minecraft/
    }
}
