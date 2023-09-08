package com.mcmoddev.mineralogy.blocks;

import java.util.List;

import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class RockSlab extends net.minecraft.block.Block {

    private String _doubleSlab = "";
    public RockSlab() {
        this(1.5f, 5f, 1, soundTypeStone);
    }
    private static final float THICKNESS = 0.5f;

    private static final AxisAlignedBB[] BOXES = new AxisAlignedBB[EnumFacing.values().length];
    static {
        for (int i = 0; i < EnumFacing.values().length; i++) {
            EnumFacing orientation = EnumFacing.values()[i];
            float x1 = 0;
            float x2 = 1;
            float y1 = 0;
            float y2 = 1;
            float z1 = 0;
            float z2 = 1;
            switch (orientation) {
                case DOWN:
                    y1 = 1f - THICKNESS;
                    break;
                case SOUTH:
                    z2 = THICKNESS;
                    break;
                case NORTH:
                    z1 = 1f - THICKNESS;
                    break;
                case EAST:
                    x2 = THICKNESS;
                    break;
                case WEST:
                    x1 = 1f - THICKNESS;
                    break;
                case UP:
                default:
                    y2 = THICKNESS;
                    break;
            }
            BOXES[orientation.ordinal()] = AxisAlignedBB.getBoundingBox(x1, y1, z1, x2, y2, z2);
        }
    }

    public RockSlab(float hardness, float blastResistance, int toolHardnessLevel, SoundType sound) {
        super(Material.rock);
        this.setHardness((float) hardness); // dirt is 0.5, grass is 0.6, stone is 1.5,iron ore is 3, obsidian is 50
        this.setResistance((float) blastResistance); // dirt is 0, iron ore is 5, stone is 10, obsidian is 2000
        this.setStepSound(sound); // sound for stone
        this.setHarvestLevel("pickaxe", toolHardnessLevel);
        this.useNeighborBrightness = true;
    }

    public RockSlab(float hardness, float blastResistance, int toolHardnessLevel, SoundType sound, String doubleSlab) {
        super(Material.rock);
        this.setHardness((float) hardness); // dirt is 0.5, grass is 0.6, stone is 1.5,iron ore is 3, obsidian is 50
        this.setResistance((float) blastResistance); // dirt is 0, iron ore is 5, stone is 10, obsidian is 2000
        this.setStepSound(sound); // sound for stone
        this.setHarvestLevel("pickaxe", toolHardnessLevel);
        this.useNeighborBrightness = true;
        _doubleSlab = doubleSlab;
    }

    @Deprecated
    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World worldIn, int x, int y, int z) {
        EnumFacing orientation = EnumFacing.values()[worldIn.getBlockMetadata(x, y, z)];
        return BOXES[orientation.ordinal()];
    }

    @Override
    @Deprecated
    public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB mask, List list,
        Entity entity) {
        int metadata = world.getBlockMetadata(x, y, z);
        EnumFacing orientation = EnumFacing.getFront(metadata & 7);
        super.addCollisionBoxesToList(world, x, y, z, mask, list, entity);
        AxisAlignedBB collisionBox = BOXES[orientation.ordinal()];
        if (collisionBox != null && mask.intersectsWith(collisionBox)) {
            list.add(collisionBox);
        }
    }
}
