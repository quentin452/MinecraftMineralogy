package com.mcmoddev.mineralogy.blocks;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class RockRelief extends RockSlab {

    private static final AxisAlignedBB[][] BOXES = new AxisAlignedBB[EnumFacing.values().length][];
    static {
        for (int i = 0; i < EnumFacing.values().length; i++) {
            EnumFacing orientation = EnumFacing.values()[i];
            float x1 = 0, x2 = 1, y1 = 0, y2 = 1, z1 = 0, z2 = 1;
            float thickness = 0.07f;
            switch (orientation) {
                case DOWN:
                    y1 = 1f - thickness;
                    break;
                case SOUTH:
                    z2 = thickness;
                    break;
                case NORTH:
                    z1 = 1f - thickness;
                    break;
                case EAST:
                    x2 = thickness;
                    break;
                case WEST:
                    x1 = 1f - thickness;
                    break;
                case UP:
                default:
                    y2 = thickness;
                    break;
            }
            AxisAlignedBB box = AxisAlignedBB.getBoundingBox(x1, y1, z1, x2, y2, z2);
            BOXES[orientation.ordinal()] = new AxisAlignedBB[] { box };
        }
    }

    public RockRelief(float hardness, float blastResistance, int toolHardnessLevel, SoundType sound) {
        super(hardness, blastResistance, toolHardnessLevel, sound);
        // TODO Auto-generated constructor stub
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World worldIn, int x, int y, int z) {

        int metadata = worldIn.getBlockMetadata(x, y, z);
        EnumFacing orientation = EnumFacing.getFront(metadata & 7);

        return BOXES[orientation.ordinal()][0];
    }

    @Override
    @Deprecated
    public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB mask, List list,
        Entity entity) {
        int metadata = world.getBlockMetadata(x, y, z);
        EnumFacing orientation = EnumFacing.getFront(metadata & 7);
        super.addCollisionBoxesToList(world, x, y, z, mask, list, entity);
        AxisAlignedBB collisionBox = BOXES[orientation.ordinal()][0];
        if (collisionBox != null && mask.intersectsWith(collisionBox)) {
            list.add(collisionBox);
        }
    }
}
