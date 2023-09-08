package com.mcmoddev.mineralogy.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class RockSaltStreetLamp extends Block {
    private static TextureMap textureMap;
    protected static AxisAlignedBB STANDING_AABB = getStandingBB();
    private static final String TEXTURE_SIDE = "wroughtiron_lamp";
    private static final String TEXTURE_TOP = "rocksalt_lamp";
    private static final String TEXTURE_PARTICLE = "wroughtiron_lamp";
    protected static AxisAlignedBB getStandingBB() {
        return AxisAlignedBB.getBoundingBox(
            0.4000000059604645D,
            0.0D,
            0.4000000059604645D,
            0.6000000238418579D,
            1.8000000238418579D,
            0.6000000238418579D);
    }

    public RockSaltStreetLamp() {
        super(Material.iron);

        this.setTickRandomly(true);
        this.setHardness(1F);
        this.setLightLevel(0.9375F);
        this.setStepSound(Block.soundTypeMetal); // Use the appropriate sound type
        this.setBlockName("rocksaltstreetlamp");
        this.setCreativeTab(CreativeTabs.tabDecorations); // Adjust the creative tab if needed
    }

    public AxisAlignedBB getCollisionBoundingBoxFromPool(World worldIn, int x, int y, int z) {
        return STANDING_AABB;
    }

    public boolean isOpaqueCube() {
        return false;
    }

    public boolean renderAsNormalBlock() {
        return false;
    }

    private boolean canPlaceOn(World worldIn, int x, int y, int z) {
        Block block = worldIn.getBlock(x, y, z);
        return block.canPlaceTorchOnTop(worldIn, x, y, z);
    }

    public boolean canPlaceBlockAt(World worldIn, int x, int y, int z) {
        return this.canPlaceOn(worldIn, x, y - 1, z);
    }

    private boolean canPlaceAt(World world, int x, int y, int z) {
        return this.canPlaceOn(world, x, y - 1, z);
    }

    public boolean canBlockStay(World worldIn, int x, int y, int z) {
        if (y >= 1 && y + 1 < 256) {
            Block block = worldIn.getBlock(x, y - 1, z);
            return block != null && block.isSideSolid(worldIn, x, y - 1, z, ForgeDirection.UP);
        } else {
            return false;
        }
    }

    public void onNeighborBlockChange(World worldIn, int x, int y, int z, Block neighborBlock) {
        if (!this.canBlockStay(worldIn, x, y, z)) {
            this.dropBlockAsItem(worldIn, x, y, z, worldIn.getBlockMetadata(x, y, z), 0);
            worldIn.setBlockToAir(x, y, z);
        }
    }
    @Override
    public int getRenderType() {
        return 1;
    }


}
