package com.mcmoddev.mineralogy;

// DON'T FORGET TO UPDATE mcmod.info FILE!!!

import com.mcmoddev.mineralogy.blocks.*;
import com.mcmoddev.mineralogy.init.Blocks;
import com.mcmoddev.mineralogy.items.MineralFertilizer;
import com.mcmoddev.mineralogy.util.MineralogyTab;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mcmoddev.mineralogy.init.MineralogyRegistry;
import com.mcmoddev.mineralogy.ioc.MinIoC;
import com.mcmoddev.mineralogy.lib.exceptions.TabNotFoundException;
import com.mcmoddev.mineralogy.lib.interfaces.IDynamicTabProvider;
import com.mcmoddev.mineralogy.worldgen.StoneReplacer;
import com.myname.mymodid.Tags;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLFingerprintViolationEvent;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;

import static net.minecraft.block.Block.soundTypeStone;


@Mod(
    modid = Mineralogy.MODID,
    name = Mineralogy.NAME,
    // TODO pick a master or docs branch to upload an updates.json folder and keep it up to date, then link the raw file
    // here.
    acceptedMinecraftVersions = "[1.7.10]",
    certificateFingerprint = "@FINGERPRINT@")
public class Mineralogy {
    public static Block chert;
    public static Item mineralFertilizer;
    @Mod.Instance(Tags.MODID)
    public static Mineralogy instance;

    public static MineralogyTab mineralogyTab = new MineralogyTab("catTab");
    /** ID of this Mod */
    public static final String MODID = "mineralogy";

    /** Display name of this Mod */
    static final String NAME = "Mineralogy";

    /**
     * Version number, in Major.Minor.Patch format. The minor number is
     * increased whenever a change is made that has the potential to break
     * compatibility with other mods that depend on this one.
     */

    private static final Logger logger = LogManager.getFormatterLogger(Mineralogy.MODID);

    @Mod.EventHandler
    public void onFingerprintViolation(FMLFingerprintViolationEvent event) {
        logger.warn("Invalid fingerprint detected!");
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(com.mcmoddev.mineralogy.init.Blocks.class);
        MinecraftForge.EVENT_BUS.register(com.mcmoddev.mineralogy.init.Items.class);
        MinecraftForge.EVENT_BUS.register(com.mcmoddev.mineralogy.init.Ores.class);
        MinecraftForge.EVENT_BUS.register(com.mcmoddev.mineralogy.init.Recipes.class);

        // Block Registry
        Chert chertBlock = new Chert();

        chertBlock.setBlockName("mineralogy_chert");
        chertBlock.setCreativeTab(Mineralogy.mineralogyTab);
        chertBlock.setBlockTextureName(Mineralogy.MODID + ":chert");

        GameRegistry.registerBlock(chertBlock, "chert");

        Chalk chalkBlock = new Chalk();

        chalkBlock.setBlockName("mineralogy_chalk");
        chalkBlock.setCreativeTab(Mineralogy.mineralogyTab);
        chalkBlock.setBlockTextureName(Mineralogy.MODID + ":chalk");

        GameRegistry.registerBlock(chalkBlock, "_chalk");

        Block drops = Block.getBlockFromName("double_slab");

        DoubleSlab doubleSlab = new DoubleSlab(
            1.0F,
            10.0F,
            1,
            drops
        );

        doubleSlab.setBlockName("mineralogy_double_slab");
        doubleSlab.setCreativeTab(Mineralogy.mineralogyTab);
        doubleSlab.setBlockTextureName(Mineralogy.MODID + ":double_slab");

        GameRegistry.registerBlock(doubleSlab, "double_slab");

        DryWall dryWall = new DryWall("drywall_black", "drywall_black", Material.rock, true);

        dryWall.setBlockName("mineralogy_dry_wall");
        dryWall.setCreativeTab(Mineralogy.mineralogyTab);
        dryWall.setBlockTextureName(Mineralogy.MODID + ":dry_wall");

        GameRegistry.registerBlock(dryWall, "dry_wall");

        Gypsum gypsum = new Gypsum();

        gypsum.setBlockName("mineralogy_gypsum");
        gypsum.setCreativeTab(Mineralogy.mineralogyTab);
        gypsum.setBlockTextureName(Mineralogy.MODID + ":gypsum");

        GameRegistry.registerBlock(gypsum, "gypsum");

        String name = "iron";
        Item oreDrop = Item.getItemFromBlock(net.minecraft.init.Blocks.iron_ore);

        int min = 2;
        int max = 5;
        int level = 1;

        Ore ore = new Ore(name, oreDrop, min, max, level);

        ore.setBlockName("mineralogy_ore");
        ore.setCreativeTab(Mineralogy.mineralogyTab);
        ore.setBlockTextureName(Mineralogy.MODID + ":ore");

        GameRegistry.registerBlock(ore, "ore");

        Rock rock = new Rock(
            true,
            1.5f,
            5f,
            1,
            soundTypeStone
        );

        rock.setBlockName("mineralogy_rock");
        rock.setCreativeTab(Mineralogy.mineralogyTab);
        rock.setBlockTextureName(Mineralogy.MODID + ":rock");

        GameRegistry.registerBlock(rock, "rock");

        RockFurnace rockFurnace = new RockFurnace(
            1.5f,
            5f,
            1,
            false,
            1f
        );

        rockFurnace.setBlockName("mineralogy_rock_furnace");
        rockFurnace.setCreativeTab(Mineralogy.mineralogyTab);
        rockFurnace.setBlockTextureName(Mineralogy.MODID + ":rock_furnace");

        GameRegistry.registerBlock(rockFurnace, "rock_furnace");

        RockRelief rockRelief = new RockRelief(
            1.5f,
            5f,
            1,
            soundTypeStone
        );

        rockRelief.setBlockName("mineralogy_rock_relief");
        rockRelief.setCreativeTab(Mineralogy.mineralogyTab);
        rockRelief.setBlockTextureName(Mineralogy.MODID + ":rock_relief");

        GameRegistry.registerBlock(rockRelief, "rock_relief");

        RockSalt rockSalt = new RockSalt();

        rockSalt.setBlockName("mineralogy_rock_salt");
        rockSalt.setCreativeTab(Mineralogy.mineralogyTab);
        rockSalt.setBlockTextureName(Mineralogy.MODID + ":rock_salt");

        GameRegistry.registerBlock(rockSalt, "rock_salt");

        RockSaltLamp rockSaltLamp = new RockSaltLamp();

        rockSaltLamp.setBlockName("mineralogy_rock_salt_lamp");
        rockSaltLamp.setCreativeTab(Mineralogy.mineralogyTab);
        rockSaltLamp.setBlockTextureName(Mineralogy.MODID + ":rock_salt_lamp");

        GameRegistry.registerBlock(rockSaltLamp, "rock_salt_lamp");
        RockSaltStreetLamp rockSaltStreetLamp = new RockSaltStreetLamp();

        rockSaltStreetLamp.setBlockName("mineralogy_rock_salt_street_lamp");
        rockSaltStreetLamp.setCreativeTab(Mineralogy.mineralogyTab);
        rockSaltStreetLamp.setBlockTextureName(Mineralogy.MODID + ":rock_salt_street_lamp");

        GameRegistry.registerBlock(rockSaltStreetLamp, "rock_salt_street_lamp");
        RockSlab rockSlab = new RockSlab();

        rockSlab.setBlockName("mineralogy_rock_slab");
        rockSlab.setCreativeTab(Mineralogy.mineralogyTab);
        rockSlab.setBlockTextureName(Mineralogy.MODID + ":rock_slab");

        GameRegistry.registerBlock(rockSlab, "rock_slab");

        RockStairs rockStairs = new RockStairs(
            net.minecraft.init.Blocks.stone,
            1.5f,
            5f,
            1,
             soundTypeStone
        );
        rockStairs.setBlockName("mineralogy_rock_stairs");
        rockStairs.setCreativeTab(Mineralogy.mineralogyTab);
        rockStairs.setBlockTextureName(Mineralogy.MODID + ":rock_stairs");

        GameRegistry.registerBlock(rockStairs, "rock_stairs");

        RockWall rockWall = new RockWall(
            net.minecraft.init.Blocks.stone,
            1.5f,
            5f,
            1,
            soundTypeStone
        );

        rockWall.setBlockName("mineralogy_rock_wall");
        rockWall.setCreativeTab(Mineralogy.mineralogyTab);
        rockWall.setBlockTextureName(Mineralogy.MODID + ":rock_wall");

        GameRegistry.registerBlock(rockWall, "rock_wall");

        Soil soil = new Soil("soil");

        soil.setBlockName("mineralogy_soil");
        soil.setCreativeTab(Mineralogy.mineralogyTab);
        soil.setBlockTextureName(Mineralogy.MODID + ":soil");

        GameRegistry.registerBlock(soil, "soil");

        // Item Registry
        MineralFertilizer mineralFertilizer = new MineralFertilizer();

        mineralFertilizer.setUnlocalizedName("mineral_fertilizer");
        mineralFertilizer.setCreativeTab(Mineralogy.mineralogyTab);
        mineralFertilizer.setTextureName(Mineralogy.MODID + ":mineral_fertilizer");

        GameRegistry.registerItem(mineralFertilizer, "mineral_fertilizer");
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        com.mcmoddev.mineralogy.init.Blocks.init();
        com.mcmoddev.mineralogy.init.Items.init();
        com.mcmoddev.mineralogy.init.Ores.Init();
        com.mcmoddev.mineralogy.init.Recipes.Init();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        // addons to other mods

        // process black-lists and white-lists
        for (String id : MineralogyConfig.igneousWhitelist()) {
            Block b = Block.getBlockFromName(id);
            if (b == null) continue;
            MineralogyRegistry.igneousStones.add(b);
        }
        for (String id : MineralogyConfig.metamorphicWhitelist()) {
            Block b = Block.getBlockFromName(id);
            if (b == null) continue;
            MineralogyRegistry.metamorphicStones.add(b);
        }
        for (String id : MineralogyConfig.sedimentaryWhitelist()) {
            Block b = Block.getBlockFromName(id);
            if (b == null) continue;
            MineralogyRegistry.sedimentaryStones.add(b);
        }
        for (String id : MineralogyConfig.igneousBlacklist()) {
            Block b = Block.getBlockFromName(id);
            if (b == null) continue;
            MineralogyRegistry.igneousStones.remove(b);
        }
        for (String id : MineralogyConfig.metamorphicBlacklist()) {
            Block b = Block.getBlockFromName(id);
            if (b == null) continue;
            MineralogyRegistry.metamorphicStones.remove(b);
        }
        for (String id : MineralogyConfig.sedimentaryBlacklist()) {
            Block b = Block.getBlockFromName(id);
            if (b == null) continue;
            MineralogyRegistry.sedimentaryStones.remove(b);
        }

        MinIoC IoC = MinIoC.getInstance();
        ItemStack sulphurStack = new ItemStack(IoC.resolve(Item.class, Constants.SULFUR, Mineralogy.MODID));

        try {
            IDynamicTabProvider tabProvider = IoC.resolve(IDynamicTabProvider.class);

            tabProvider.setTabIcons();

            if (MineralogyConfig.groupCreativeTabItemsByType()) tabProvider.setIcon("Item", sulphurStack);

        } catch (TabNotFoundException e) {
            e.printStackTrace();
        }
    }
}
