package com.mcmoddev.mineralogy;

// DON'T FORGET TO UPDATE mcmod.info FILE!!!

import static net.minecraft.block.Block.soundTypeStone;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mcmoddev.mineralogy.blocks.*;
import com.mcmoddev.mineralogy.blocks.Ore;
import com.mcmoddev.mineralogy.init.MineralogyRegistry;
import com.mcmoddev.mineralogy.ioc.MinIoC;
import com.mcmoddev.mineralogy.items.MineralFertilizer;
import com.mcmoddev.mineralogy.util.MineralogyTab;
import com.myname.mymodid.Tags;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLFingerprintViolationEvent;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(
    modid = Mineralogy.MODID,
    name = Mineralogy.NAME,
    // TODO pick a master or docs branch to upload an updates.json folder and keep it up to date, then link the raw file
    // here.
    acceptedMinecraftVersions = "[1.7.10]",
    certificateFingerprint = "@FINGERPRINT@")
public class Mineralogy {

    public static Block chert;
    @Mod.Instance(Tags.MODID)
    public static Mineralogy instance;

    public static MineralogyTab mineralogyTab = new MineralogyTab("mineralogytab");
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
        Rock dolomite = new Rock(true, 1.5f, 5f, 1, soundTypeStone);
        
        dolomite.setBlockName("mineralogy_dolomite");
        dolomite.setCreativeTab(Mineralogy.mineralogyTab);
        dolomite.setBlockTextureName(Mineralogy.MODID + ":dolomite");

        GameRegistry.registerBlock(dolomite, "dolomite");

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

        DryWall dryWall = new DryWall("drywall_black", "drywall_black", Material.rock, true);

        dryWall.setBlockName("mineralogy_dry_wall");
        dryWall.setCreativeTab(Mineralogy.mineralogyTab);
        dryWall.setBlockTextureName(Mineralogy.MODID + ":drywall_black");

        GameRegistry.registerBlock(dryWall, "drywall_black");

        Gypsum gypsum = new Gypsum();

        gypsum.setBlockName("mineralogy_gypsum");
        gypsum.setCreativeTab(Mineralogy.mineralogyTab);
        gypsum.setBlockTextureName(Mineralogy.MODID + ":gypsum");

        GameRegistry.registerBlock(gypsum, "gypsum");

        Rock rock = new Rock(true, 1.5f, 5f, 1, soundTypeStone);

        rock.setBlockName("mineralogy_rock");
        rock.setCreativeTab(Mineralogy.mineralogyTab);
        rock.setBlockTextureName(Mineralogy.MODID + ":rock");

        GameRegistry.registerBlock(rock, "rock");

        RockFurnace rockFurnace = new RockFurnace(1.5f, 5f, 1, false, 1f);

        rockFurnace.setBlockName("mineralogy_rock_furnace");
        rockFurnace.setCreativeTab(Mineralogy.mineralogyTab);
        rockFurnace.setBlockTextureName(Mineralogy.MODID + ":rock_furnace");

        GameRegistry.registerBlock(rockFurnace, "rock_furnace");

        RockRelief rockRelief = new RockRelief(1.5f, 5f, 1, soundTypeStone);

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

        rockSaltLamp.setBlockName("mineralogy_rocksaltlamp");
        rockSaltLamp.setCreativeTab(Mineralogy.mineralogyTab);
        rockSaltLamp.setBlockTextureName(Mineralogy.MODID + ":rock_salt_lamp");

        GameRegistry.registerBlock(rockSaltLamp, "rock_salt_lamp");
        RockSaltStreetLamp rockSaltStreetLamp = new RockSaltStreetLamp();
        rockSaltStreetLamp.setBlockName("mineralogy_rocksaltstreetlamp");
        rockSaltStreetLamp.setCreativeTab(Mineralogy.mineralogyTab);
        rockSaltStreetLamp.setBlockTextureName(Mineralogy.MODID + ":rocksaltstreetlamp");

        GameRegistry.registerBlock(rockSaltStreetLamp, "rocksaltstreetlamp");

        RockStairs rockStairs = new RockStairs(net.minecraft.init.Blocks.stone, 1.5f, 5f, 1, soundTypeStone);
        rockStairs.setBlockName("mineralogy_rock_stairs");
        rockStairs.setCreativeTab(Mineralogy.mineralogyTab);
        rockStairs.setBlockTextureName(Mineralogy.MODID + ":rock_stairs");

        GameRegistry.registerBlock(rockStairs, "rock_stairs");

        RockWall rockWall = new RockWall(net.minecraft.init.Blocks.stone, 1.5f, 5f, 1, soundTypeStone);

        rockWall.setBlockName("mineralogy_rock_wall");
        rockWall.setCreativeTab(Mineralogy.mineralogyTab);
        rockWall.setBlockTextureName(Mineralogy.MODID + ":rock_wall");

        GameRegistry.registerBlock(rockWall, "rock_wall");

        Soil soil = new Soil("soil");

        soil.setBlockName("mineralogy_soil");
        soil.setCreativeTab(Mineralogy.mineralogyTab);
        soil.setBlockTextureName(Mineralogy.MODID + ":soil");

        GameRegistry.registerBlock(soil, "soil");
        MinIoC IoC = MinIoC.getInstance();
        Item nitratePowder = IoC.resolve(Item.class, "dustNitrate", Mineralogy.MODID);
        Ore nitrate = new Ore(Constants.NITRATE, nitratePowder, 1, 4, 0);

        nitrate.setBlockName("mineralogy_nitrate_ore");
        nitrate.setCreativeTab(Mineralogy.mineralogyTab);
        nitrate.setBlockTextureName(Mineralogy.MODID + ":nitrate_ore");
        GameRegistry.registerBlock(nitrate, "nitrate_ore");

        Item phosphorousPowder = IoC.resolve(Item.class, "dustPhosphorous", Mineralogy.MODID);

        Ore phosphorous = new Ore(Constants.PHOSPHOROUS, phosphorousPowder, 1, 4, 0);

        phosphorous.setBlockName("mineralogy_phosphorous_ore");
        phosphorous.setCreativeTab(Mineralogy.mineralogyTab);
        phosphorous.setBlockTextureName(Mineralogy.MODID + ":phosphorous_ore");

        GameRegistry.registerBlock(phosphorous, "phosphorous_ore");

        Item sulfurPowder = IoC.resolve(Item.class, "dustSulfur", Mineralogy.MODID);

        Ore sulfur = new Ore(Constants.SULFUR, sulfurPowder, 1, 4, 0);

        sulfur.setBlockName("mineralogy_sulfur_ore");
        sulfur.setCreativeTab(Mineralogy.mineralogyTab);
        sulfur.setBlockTextureName(Mineralogy.MODID + ":sulfur_ore");
        GameRegistry.registerBlock(sulfur, "sulfur_ore");
        DoubleSlab conglomeratesmoothbrick = new DoubleSlab(1.5f, 5f, 1);

        conglomeratesmoothbrick.setBlockName("mineralogy_conglomerate_smooth_brick");
        conglomeratesmoothbrick.setCreativeTab(Mineralogy.mineralogyTab);
        conglomeratesmoothbrick.setBlockTextureName(Mineralogy.MODID + ":conglomerate_smooth_brick");

        GameRegistry.registerBlock(conglomeratesmoothbrick, "conglomerate_smooth_brick");
        // Slab Registry
        SimpleSlab conglomeratesmoothbrickslab = new SimpleSlab(1.5f, 5f, 1);

        conglomeratesmoothbrickslab.setBlockName("mineralogy_conglomerate_smooth_brick_slab");
        conglomeratesmoothbrickslab.setCreativeTab(Mineralogy.mineralogyTab);
        conglomeratesmoothbrickslab.setBlockTextureName(Mineralogy.MODID + ":conglomerate_smooth_brick");

        GameRegistry.registerBlock(conglomeratesmoothbrickslab, "conglomerate_smooth_brick_slab");

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
    }
}
