package com.mcmoddev.mineralogy.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class RockSaltStreetLamp extends Block {

    protected static AxisAlignedBB STANDING_AABB = getStandingBB();

    protected static AxisAlignedBB getStandingBB() {
        return AxisAlignedBB.getBoundingBox(
            0.4000000059604645D,
            0.0D,
            0.4000000059604645D,
            0.6000000238418579D,
            1.8000000238418579D,
            0.6000000238418579D);
    }

    public static final SoundType SOUND_TYPE = new SoundType("metal", 1.0F, 1.0F);

    public RockSaltStreetLamp() {
        super(Material.iron);

        this.setTickRandomly(true);
        this.setHardness(1F);
        this.setLightLevel(0.9375F);
        this.setStepSound(SOUND_TYPE);
        this.setBlockName("rocksaltstreetlamp");
    }

    public AxisAlignedBB getBoundingBox(Block state, IBlockAccess source, ChunkCoordinates pos) {
        return STANDING_AABB;
    }

    /**
     * Used to determine ambient occlusion and culling when rebuilding chunks for render
     */
    public boolean isOpaqueCube(Block state) {
        return false;
    }

    public boolean isFullCube(Block state) {
        return false;
    }

    private boolean canPlaceOn(World worldIn, int x, int y, int z) {

        Block block = worldIn.getBlock(x, y, z);

        return block.canPlaceTorchOnTop(worldIn, x, y, z);

    }

    public boolean canPlaceBlockAt(World worldIn, ChunkCoordinates pos) {
        EnumFacing enumfacing = EnumFacing.UP;
        if (this.canPlaceAt(worldIn, pos.posX, pos.posY, pos.posZ, enumfacing)) {
            return true;
        }
        return false;
    }

    private boolean canPlaceAt(World world, int x, int y, int z, EnumFacing facing) {
        Block blockAbove = world.getBlock(x, y + 1, z);

        if (!blockAbove.isAir(world, x, y + 1, z)) return false;

        int dx = 0;
        int dz = 0;
        ForgeDirection forgeDirection = ForgeDirection.NORTH;

        switch (facing) {
            case NORTH:
                dx = 0;
                dz = -1;
                forgeDirection = ForgeDirection.NORTH;
                break;
            case SOUTH:
                dx = 0;
                dz = 1;
                forgeDirection = ForgeDirection.SOUTH;
                break;
            case WEST:
                dx = -1;
                dz = 0;
                forgeDirection = ForgeDirection.WEST;
                break;
            case EAST:
                dx = 1;
                dz = 0;
                forgeDirection = ForgeDirection.EAST;
                break;
        }

        boolean horizontal = false;

        switch (facing) {
            case NORTH:
            case SOUTH:
            case WEST:
            case EAST:
                horizontal = true;
                break;
            case UP:
            case DOWN:
                horizontal = false;
                break;
        }

        return horizontal && world.isSideSolid(x + dx, y + 1, z + dz, forgeDirection)
            || facing == EnumFacing.UP && canPlaceOn(world, x, y, z);
    }

    /**
     * Called when a neighbouring block was changed and marks that this state should perform any checks during a
     * neighbour
     * change. Cases may include when redstone power is updated, cactus blocks popping off due to a neighbouring solid
     * block, etc.
     */

    private EnumFacing getFacingFromMetadata(int metadata) {
        switch (metadata) {
            case 0:
                return EnumFacing.DOWN;
            case 1:
                return EnumFacing.UP;
            case 2:
                return EnumFacing.NORTH;
            case 3:
                return EnumFacing.SOUTH;
            case 4:
                return EnumFacing.WEST;
            case 5:
                return EnumFacing.EAST;
            default:
                return EnumFacing.NORTH;
        }
    }

    /*
     * @SideOnly(Side.CLIENT)
     * public void randomDisplayTick(Block stateIn, World worldIn, ChunkCoordinates pos, Random rand) {
     * EnumFacing enumfacing = stateIn.getValue(FACING);
     * double d0 = (double) pos.posX + 0.5D;
     * double d1 = (double) pos.posY + 1.7D;
     * double d2 = (double) pos.posZ + 0.5D;
     * if (enumfacing.getAxis()
     * .isHorizontal()) {
     * EnumFacing enumfacing1 = enumfacing.getOpposite();
     * worldIn.spawnParticle(
     * EnumParticleTypes.SMOKE_NORMAL,
     * d0 + 0.27D * (double) enumfacing1.getXOffset(),
     * d1 + 0.22D,
     * d2 + 0.27D * (double) enumfacing1.getZOffset(),
     * 0.0D,
     * 0.0D,
     * 0.0D,
     * new int[0]);
     * } else {
     * worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0, d1, d2, 0.0D, 0.0D, 0.0D, new int[0]);
     * }
     * }
     */
}
