package com.mcmoddev.mineralogy.tileentity;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.ITickable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.*;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TileEntityRockFurnace extends TileEntity implements ITickable, ISidedInventory {

    private static final int[] SLOTS_TOP = new int[] { 0 };
    private static final int[] SLOTS_BOTTOM = new int[] { 2, 1 };
    private static final int[] SLOTS_SIDES = new int[] { 1 };
    /** The ItemStacks that hold the items currently being used in the furnace */
    private List<ItemStack> furnaceItemStacks = new ArrayList<>(3);
    /** The number of ticks that the furnace will keep burning */
    private int furnaceBurnTime;
    /** The number of ticks that a fresh copy of the currently-burning item would keep the furnace burning for */
    private int currentItemBurnTime;
    private int cookTime;
    private int totalCookTime;
    private String furnaceCustomName;
    private float _burnModifier;

    public TileEntityRockFurnace(float burnModifier, ChunkCoordinates pos) {
        _burnModifier = burnModifier;
    }

    // public void setBurnModifier(float burnModifier) {
    // _burnModifier = burnModifier;
    // }

    /**
     * Returns the number of slots in the inventory.
     */
    public int getSizeInventory() {
        return this.furnaceItemStacks.size();
    }

    /**
     * Returns the stack in the given slot.
     */
    public ItemStack getStackInSlot(int index) {
        return this.furnaceItemStacks.get(index);
    }

    /**
     * Removes up to a specified number of items from an inventory slot and returns them in a new stack.
     */
    public ItemStack decrStackSize(int index, int count) {

        ItemStack stack = this.furnaceItemStacks.get(index);

        if (stack != null) {
            if (stack.stackSize <= count) {
                this.furnaceItemStacks.set(index, null);
                return stack;
            }

            ItemStack result = stack.splitStack(count);
            if (stack.stackSize == 0) {
                this.furnaceItemStacks.set(index, null);
            }

            return result;
        }

        return stack;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int index) {
        return null;
    }

    /**
     * Removes a stack from the given slot and returns it.
     */
    public ItemStack removeStackFromSlot(int index) {

        ItemStack stack = this.furnaceItemStacks.get(index);

        if (stack != null) {
            this.furnaceItemStacks.set(index, null);
        }

        return stack;

    }

    /**
     * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
     */
    public void setInventorySlotContents(int index, ItemStack stack) {
        ItemStack itemstack = this.furnaceItemStacks.get(index);
        boolean flag = stack != null && ItemStack.areItemStacksEqual(stack, itemstack)
            && ItemStack.areItemStackTagsEqual(stack, itemstack);
        this.furnaceItemStacks.set(index, stack);

        if (stack != null && stack.stackSize > this.getInventoryStackLimit()) {
            stack.stackSize = this.getInventoryStackLimit();
        }

        if (index == 0 && !flag) {
            this.totalCookTime = this.getCookTime(stack);
            this.cookTime = 0;
            this.markDirty();
        }
    }

    @Override
    public String getInventoryName() {
        return null;
    }

    @Override
    public boolean hasCustomInventoryName() {
        return false;
    }

    public String getName() {
        return this.hasCustomName() ? this.furnaceCustomName : "container.furnace";
    }

    public boolean hasCustomName() {
        return this.furnaceCustomName != null && !this.furnaceCustomName.isEmpty();
    }

    public void setCustomInventoryName(String p_145951_1_) {
        this.furnaceCustomName = p_145951_1_;
    }

    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);

        furnaceItemStacks.clear();

        for (int i = 0; i < getSizeInventory(); i++) {
            NBTTagCompound itemTag = compound.getCompoundTag("item" + i);

            ItemStack stack = new ItemStack(
                Block.getBlockById(itemTag.getShort("id")),
                itemTag.getShort("Count"),
                itemTag.getShort("Damage"));

            if (itemTag.hasKey("tag")) {
                stack.setTagCompound(itemTag.getCompoundTag("tag"));
            }

        }

        this.furnaceBurnTime = compound.getInteger("BurnTime");
        this.cookTime = compound.getInteger("CookTime");
        this.totalCookTime = compound.getInteger("CookTimeTotal");
        this.currentItemBurnTime = getItemBurnTime(this.furnaceItemStacks.get(1));

        if (compound.hasKey("CustomName", 8)) {
            this.furnaceCustomName = compound.getString("CustomName");
        }
    }

    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setInteger("BurnTime", (short) this.furnaceBurnTime);
        compound.setInteger("CookTime", (short) this.cookTime);
        compound.setInteger("CookTimeTotal", (short) this.totalCookTime);

        for (int i = 0; i < furnaceItemStacks.size(); i++) {
            ItemStack stack = furnaceItemStacks.get(i);

            if (stack != null) {
                NBTTagCompound itemTag = new NBTTagCompound();
                stack.writeToNBT(itemTag);
                compound.setTag("item" + i, itemTag);
            }
        }

        if (this.hasCustomName()) {
            compound.setString("CustomName", this.furnaceCustomName);
        }
    }

    /**
     * Returns the maximum stack size for a inventory slot. Seems to always be 64, possibly will be extended.
     */
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return false;
    }

    @Override
    public void openInventory() {

    }

    @Override
    public void closeInventory() {

    }

    /**
     * Furnace isBurning
     */
    public boolean isBurning() {
        return this.furnaceBurnTime > 0;
    }

    @SideOnly(Side.CLIENT)
    public static boolean isBurning(IInventory inventory) {
        if (inventory instanceof TileEntityFurnace) {
            TileEntityFurnace furnace = (TileEntityFurnace) inventory;
            return furnace.furnaceBurnTime > 0;
        }
        return false;
    }

    /**
     * Like the old updateEntity(), except more generic.
     */
    public void update() {
        boolean flag = this.isBurning();
        boolean flag1 = false;

        if (this.isBurning()) {
            --this.furnaceBurnTime;
        }

        if (!this.worldObj.isRemote) {
            ItemStack itemstack = this.furnaceItemStacks.get(1);

            if (this.isBurning() || (itemstack != null && !(this.furnaceItemStacks.get(0) == null))) {
                if (!this.isBurning() && this.canSmelt()) {
                    this.furnaceBurnTime = (int) (getItemBurnTime(itemstack) * _burnModifier);
                    this.currentItemBurnTime = this.furnaceBurnTime;

                    if (this.isBurning()) {
                        flag1 = true;

                        Item item = itemstack.getItem();
                        itemstack.stackSize--;

                        if (itemstack.stackSize == 0) {
                            assert item != null;
                            ItemStack item1 = item.getContainerItem(itemstack);
                            this.furnaceItemStacks.set(1, item1);
                        }
                    }
                }

                if (this.isBurning() && this.canSmelt()) {
                    ++this.cookTime;

                    if (this.cookTime == this.totalCookTime) {
                        this.cookTime = 0;
                        this.totalCookTime = this.getCookTime(this.furnaceItemStacks.get(0));
                        this.smeltItem();
                        flag1 = true;
                    }
                } else {
                    this.cookTime = 0;
                }
            } else if (!this.isBurning() && this.cookTime > 0) {
                this.cookTime = MathHelper.clamp_int(this.cookTime - 2, 0, this.totalCookTime);
            }
        }

        if (flag1) {
            this.markDirty();
        }
    }

    public int getCookTime(ItemStack stack) {
        return 200;
    }

    /**
     * Returns true if the furnace can smelt an item, i.e. has a source item, destination stack isn't full, etc.
     */
    private boolean canSmelt() {
        if (this.furnaceItemStacks.get(0) == null) {
            return false;
        } else {
            ItemStack itemstack = FurnaceRecipes.smelting()
                .getSmeltingResult(this.furnaceItemStacks.get(0));

            if (itemstack == null) {
                return false;
            } else {
                ItemStack itemstack1 = this.furnaceItemStacks.get(2);

                if (itemstack1 == null) {
                    return true;
                } else if (!itemstack1.isItemEqual(itemstack)) {
                    return false;
                } else if (itemstack1.stackSize + itemstack.stackSize <= this.getInventoryStackLimit()
                    && itemstack1.stackSize + itemstack.stackSize <= itemstack1.getMaxStackSize()) // Forge fix: make
                                                                                                   // furnace respect
                                                                                                   // stack sizes in
                                                                                                   // furnace recipes
                {
                    return true;
                } else {
                    return itemstack1.stackSize + itemstack.stackSize <= itemstack.getMaxStackSize(); // Forge fix:
                                                                                                      // make furnace
                                                                                                      // respect stack
                                                                                                      // sizes in
                                                                                                      // furnace
                                                                                                      // recipes
                }
            }
        }
    }

    /**
     * Turn one item from the furnace source stack into the appropriate smelted item in the furnace result stack
     */
    public void smeltItem() {
        if (this.canSmelt()) {
            ItemStack itemstack = this.furnaceItemStacks.get(0);
            ItemStack itemstack1 = FurnaceRecipes.smelting()
                .getSmeltingResult(itemstack);
            ItemStack itemstack2 = this.furnaceItemStacks.get(2);

            if (itemstack2 == null) {
                this.furnaceItemStacks.set(2, itemstack1.copy());
            } else if (itemstack2.getItem() == itemstack1.getItem()) {
                itemstack2.stackSize += itemstack1.stackSize;
            }

            if (itemstack.getItem() == Item.getItemFromBlock(Blocks.sponge) && furnaceItemStacks.get(1) != null
                && furnaceItemStacks.get(1)
                    .getItem() == Items.bucket) {

                furnaceItemStacks.set(1, new ItemStack(Items.water_bucket));

            }

            itemstack.stackSize--;
        }
    }

    /**
     * Returns the number of ticks that the supplied fuel item will keep the furnace burning, or 0 if the item isn't
     * fuel
     */
    public static int getItemBurnTime(ItemStack stack) {
        if (stack == null) {
            return 0;
        }

        Item item = stack.getItem();

        if (item == Item.getItemFromBlock(Blocks.wooden_slab)) {
            return 150;
        } else if (item == Item.getItemFromBlock(Blocks.wool)) {
            return 100;
        } else if (item == Item.getItemFromBlock(Blocks.carpet)) {
            return 67;
        } else if (item == Item.getItemFromBlock(Blocks.ladder)) {
            return 300;
        } else if (item == Item.getItemFromBlock(Blocks.wooden_button)) {
            return 100;
        } else if (Block.getBlockFromItem(item)
            .getMaterial() == Material.wood) {
                return 300;
            } else if (item == Item.getItemFromBlock(Blocks.coal_block)) {
                return 16000;
            } else if (item instanceof ItemTool && "WOOD".equals(((ItemTool) item).getToolMaterialName())) {
                return 200;
            } else if (item instanceof ItemSword && "WOOD".equals(((ItemSword) item).getToolMaterialName())) {
                return 200;
            } else if (item instanceof ItemHoe && "WOOD".equals(((ItemHoe) item).getToolMaterialName())) {
                return 200;
            } else if (item == Items.stick) {
                return 100;
            } else if (item != Items.bow && item != Items.fishing_rod) {
                if (item == Items.sign) {
                    return 200;
                } else if (item == Items.coal) {
                    return 1600;
                } else if (item == Items.lava_bucket) {
                    return 20000;
                } else if (item != Item.getItemFromBlock(Blocks.sapling) && item != Items.bowl) {
                    if (item == Items.blaze_rod) {
                        return 2400;
                    } else if (item instanceof ItemDoor && item != Items.iron_door) {
                        return 200;
                    } else {
                        return item instanceof ItemBoat ? 400 : 0;
                    }
                } else {
                    return 100;
                }
            } else {
                return 300;
            }
    }

    public static boolean isItemFuel(ItemStack stack) {
        return getItemBurnTime(stack) > 0;
    }

    /**
     * Don't rename this method to canInteractWith due to conflicts with Container
     */
    public boolean isUsableByPlayer(EntityPlayer player) {

        if (this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord) != this) {
            return false;
        }

        return player.getDistanceSq(this.xCoord + 0.5, this.yCoord + 0.5, this.zCoord + 0.5) <= 64.0D;

    }

    public void openInventory(EntityPlayer player) {}

    public void closeInventory(EntityPlayer player) {}

    /**
     * Returns true if automation is allowed to insert the given stack (ignoring stack size) into the given slot. For
     * guis use Slot.isItemValid
     */
    public boolean isItemValidForSlot(int index, ItemStack stack) {

        if (index == 2) {
            return false;
        }

        if (index != 1) {
            return true;
        }

        // Check fuel directly
        return isItemFuel(stack) || isBucket(stack);

    }

    private boolean isBucket(ItemStack stack) {
        return stack.getItem() == Items.bucket;
    }

    public int[] getSlotsForFace(EnumFacing side) {
        if (side == EnumFacing.DOWN) {
            return SLOTS_BOTTOM;
        } else {
            return side == EnumFacing.UP ? SLOTS_TOP : SLOTS_SIDES;
        }
    }

    /**
     * Returns true if automation can insert the given item in the given slot from the given side.
     */
    public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
        return this.isItemValidForSlot(index, itemStackIn);
    }

    /**
     * Returns true if automation can extract the given item in the given slot from the given side.
     */
    public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
        if (direction == EnumFacing.DOWN && index == 1) {
            Item item = stack.getItem();

            if (item != Items.water_bucket && item != Items.bucket) {
                return false;
            }
        }

        return true;
    }

    public String getGuiID() {
        return "minecraft:furnace";
    }

    public int getField(int id) {
        switch (id) {
            case 0:
                return this.furnaceBurnTime;
            case 1:
                return this.currentItemBurnTime;
            case 2:
                return this.cookTime;
            case 3:
                return this.totalCookTime;
            default:
                return 0;
        }
    }

    public void setField(int id, int value) {
        switch (id) {
            case 0:
                this.furnaceBurnTime = value;
                break;
            case 1:
                this.currentItemBurnTime = value;
                break;
            case 2:
                this.cookTime = value;
                break;
            case 3:
                this.totalCookTime = value;
        }
    }

    public int getFieldCount() {
        return 4;
    }

    public void clear() {
        this.furnaceItemStacks.clear();
    }

    @Override
    public void tick() {

    }

    @Override
    public int[] getAccessibleSlotsFromSide(int p_94128_1_) {
        return new int[0];
    }

    @Override
    public boolean canInsertItem(int p_102007_1_, ItemStack p_102007_2_, int p_102007_3_) {
        return false;
    }

    @Override
    public boolean canExtractItem(int p_102008_1_, ItemStack p_102008_2_, int p_102008_3_) {
        return false;
    }
}
