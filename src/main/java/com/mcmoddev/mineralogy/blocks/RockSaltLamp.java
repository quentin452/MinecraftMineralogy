package com.mcmoddev.mineralogy.blocks;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class RockSaltLamp extends Block {

    private static final AxisAlignedBB STANDING_AABB = AxisAlignedBB.getBoundingBox(
        0.4000000059604645D,
        0.0D,
        0.4000000059604645D,
        0.6000000238418579D,
        0.6000000238418579D,
        0.6000000238418579D);
    private static final AxisAlignedBB TORCH_NORTH_AABB = AxisAlignedBB.getBoundingBox(
        0.3499999940395355D,
        0.20000000298023224D,
        0.699999988079071D,
        0.6499999761581421D,
        0.800000011920929D,
        1.0D);
    private static final AxisAlignedBB TORCH_SOUTH_AABB = AxisAlignedBB.getBoundingBox(
        0.3499999940395355D,
        0.20000000298023224D,
        0.0D,
        0.6499999761581421D,
        0.800000011920929D,
        0.30000001192092896D);
    private static final AxisAlignedBB TORCH_WEST_AABB = AxisAlignedBB.getBoundingBox(
        0.699999988079071D,
        0.20000000298023224D,
        0.3499999940395355D,
        1.0D,
        0.800000011920929D,
        0.6499999761581421D);
    private static final AxisAlignedBB TORCH_EAST_AABB = AxisAlignedBB.getBoundingBox(
        0.0D,
        0.20000000298023224D,
        0.3499999940395355D,
        0.30000001192092896D,
        0.800000011920929D,
        0.6499999761581421D);

    public RockSaltLamp() {
        super(Material.rock);
        this.setTickRandomly(true);
        this.setHardness(0.25F);
        this.setLightLevel(0.9375F);
        this.setStepSound(Block.soundTypeStone);
        this.setBlockName("rocksaltlamp");
    }

    public AxisAlignedBB getBoundingBox(Block block, IBlockAccess world, int x, int y, int z) {

        int meta = world.getBlockMetadata(x, y, z);

        EnumFacing facing = EnumFacing.getFront(meta);

        switch (facing) {
            case EAST:
                return TORCH_EAST_AABB;
            case WEST:
                return TORCH_WEST_AABB;
            case SOUTH:
                return TORCH_SOUTH_AABB;
            case NORTH:
                return TORCH_NORTH_AABB;
            default:
                return STANDING_AABB;
        }

    }

    @Nullable
    public AxisAlignedBB getCollisionBoundingBox(Block blockState, World worldIn, int x, int y, int z) {
        return null;
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

    private boolean canPlaceOn(World world, int x, int y, int z) {

        Block block = world.getBlock(x, y, z);

        if (block.isSideSolid(world, x, y, z, ForgeDirection.UP)) {
            return true;
        }

        return block.canPlaceTorchOnTop(world, x, y, z);

    }

    private boolean canPlaceAt(World world, int x, int y, int z, EnumFacing facing) {
        int dx = x + facing.getFrontOffsetX();
        int dy = y + facing.getFrontOffsetY();
        int dz = z + facing.getFrontOffsetZ();

        Block blockAbove = world.getBlock(x, y + 1, z);

        if (blockAbove.isSideSolid(world, x, y + 1, z, ForgeDirection.DOWN)) {
            return true;
        }

        if (facing == EnumFacing.NORTH || facing == EnumFacing.SOUTH
            || facing == EnumFacing.WEST
            || facing == EnumFacing.EAST) {

            return world.isSideSolid(dx, dy, dz, ForgeDirection.getOrientation(facing.ordinal()));
        }

        return facing == EnumFacing.UP && canPlaceOn(world, dx, dy, dz);
    }

    /*
     * @SideOnly(Side.CLIENT)
     * public void randomDisplayTick(Block stateIn, World worldIn, ChunkCoordinates pos, Random rand) {
     * EnumFacing enumfacing = stateIn.getValue(FACING);
     * double d0 = (double) pos.getX() + 0.5D;
     * double d1 = (double) pos.getY() + 0.7D;
     * double d2 = (double) pos.getZ() + 0.5D;
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
     * } else worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0, d1, d2, 0.0D, 0.0D, 0.0D, new int[0]);
     * }
     */
}
