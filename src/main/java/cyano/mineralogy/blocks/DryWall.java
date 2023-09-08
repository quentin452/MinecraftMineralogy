package cyano.mineralogy.blocks;

import net.minecraft.block.material.Material;

import cyano.mineralogy.Mineralogy;

public class DryWall extends net.minecraft.block.BlockPane {

    final static String itemName = "drywall";

    public DryWall(String color) {
        super(
            Mineralogy.MODID + ":" + itemName + "_" + color,
            Mineralogy.MODID + ":" + itemName + "_top",
            Material.rock,
            true);
        this.setBlockName(Mineralogy.MODID + "_" + itemName + "_" + color);
    }
}
