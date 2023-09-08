package com.mcmoddev.mineralogy.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;

import com.mcmoddev.mineralogy.tileentity.TileEntityRockFurnace;

public class RockFurnace extends BlockContainer {

    private final boolean isBurning;
    private static boolean keepInventory;
    private float _burnModifier;

    public RockFurnace(float hardness, float blastResistance, int toolHardnessLevel, boolean isBurning,
        float burnModifier) {
        super(Material.rock);
        this.isBurning = isBurning;
        this.setHardness(hardness);
        this.blockResistance = blastResistance;
        this.setHarvestLevel("pickaxe", toolHardnessLevel);
        _burnModifier = burnModifier;
    }

    @Override
    protected boolean canSilkHarvest() {
        // TODO Auto-generated method stub
        return false;
    }

    /*
     * public Item getItemDropped(Block state, Random rand, int fortune)
     * {
     * Block drop = Block.getBlockFromItem(state.getItemDropped());
     * ResourceLocation resource = drop.getRegistryName();
     * String path = resource.getResourcePath();
     * if (path.startsWith("lit_"))
     * drop = Block.getBlockFromName(resource.getResourcePath() + ":" + path.substring(4));
     * return Item.getItemFromBlock(drop);
     * }
     */

    public void onBlockAdded(World worldIn, ChunkCoordinates pos, Block state) {
        this.setDefaultFacing(worldIn, pos, state);
    }

    private void setDefaultFacing(World worldIn, ChunkCoordinates pos, Block state) {
        if (!worldIn.isRemote) {

            int metadata = worldIn.getBlockMetadata(pos.posX, pos.posY, pos.posZ);
            worldIn.setBlockMetadataWithNotify(pos.posX, pos.posY, pos.posZ, metadata | (0), 2);
        }
    }

    /*
     * @SideOnly(Side.CLIENT)
     * @SuppressWarnings("incomplete-switch")
     * public void randomDisplayTick(Block stateIn, World worldIn, int x, int y, int z, Random rand) {
     * if (this.isBurning) {
     * int meta = worldIn.getBlockMetadata(x,y,z);
     * EnumFacing facing = EnumFacing.getFront(meta);
     * double d0 = (double) x + 0.5D;
     * double d1 = (double) y + rand.nextDouble() * 6.0D / 16.0D;
     * double d2 = (double) z + 0.5D;
     * double d4 = rand.nextDouble() * 0.6D - 0.3D;
     * if (rand.nextDouble() < 0.1D) {
     * worldIn.playSound(d0, y, d2, "random.fizz", 1.0F, 1.0F, false);
     * }
     * switch (facing) {
     * case WEST:
     * worldIn.spawnParticle("smoke", d0 - 0.52D, d1, d2 + d4, 0.0D, 0.0D, 0.0D);
     * worldIn.spawnParticle("flame", d0 - 0.52D, d1, d2 + d4, 0.0D, 0.0D, 0.0D);
     * break;
     * case EAST:
     * worldIn.spawnParticle("smoke", d0 + 0.52D, d1, d2 + d4, 0.0D, 0.0D, 0.0D);
     * worldIn.spawnParticle("flame", d0 + 0.52D, d1, d2 + d4, 0.0D, 0.0D, 0.0D);
     * break;
     * case NORTH:
     * worldIn.spawnParticle("smoke", d0 + d4, d1, d2 - 0.52D, 0.0D, 0.0D, 0.0D);
     * worldIn.spawnParticle("flame", d0 + d4, d1, d2 - 0.52D, 0.0D, 0.0D, 0.0D);
     * break;
     * case SOUTH:
     * worldIn.spawnParticle("smoke", d0 + d4, d1, d2 + 0.52D, 0.0D, 0.0D, 0.0D);
     * worldIn.spawnParticle("flame", d0 + d4, d1, d2 + 0.52D, 0.0D, 0.0D, 0.0D);
     * }
     * }
     * }
     */
    public void onBlockAdded(World worldIn, int x, int y, int z) {
        super.onBlockAdded(worldIn, x, y, z);
    }

    public TileEntity createNewTileEntity(World worldIn, int meta) {
        TileEntityRockFurnace newTE = new TileEntityRockFurnace(_burnModifier, null);
        return newTE;
    }
}
