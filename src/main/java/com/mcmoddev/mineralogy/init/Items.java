package com.mcmoddev.mineralogy.init;

import com.mcmoddev.mineralogy.Mineralogy;
import com.mcmoddev.mineralogy.blocks.Rock;
import com.mcmoddev.mineralogy.data.MaterialData;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.BlockItem;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

@Mod.EventBusSubscriber(modid = Mineralogy.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
@ObjectHolder(Mineralogy.MODID)
public class Items {
    public static final BlockItem basalt = null;
    
    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
    	event.getRegistry().registerAll(
// TODO: make a mineralogy tab
                new BlockItem(Blocks.basalt, new BlockItem.Properties().group(ItemGroup.BUILDING_BLOCKS)).setRegistryName(Mineralogy.MODID, MaterialData.BASALT.materialName.toLowerCase())

        );
    }
}


//package com.mcmoddev.mineralogy.init;
//
//import com.mcmoddev.mineralogy.Constants;
//import com.mcmoddev.mineralogy.Mineralogy;
//import com.mcmoddev.mineralogy.ioc.MinIoC;
//import com.mcmoddev.mineralogy.items.MineralFertilizer;
//import com.mcmoddev.mineralogy.lib.exceptions.ItemNotFoundException;
//import com.mcmoddev.mineralogy.lib.exceptions.TabNotFoundException;
//import com.mcmoddev.mineralogy.lib.interfaces.IDynamicTabProvider;
//import com.mcmoddev.mineralogy.util.RecipeHelper;
//import com.mcmoddev.mineralogy.util.RegistrationHelper;
//
//import net.minecraft.item.Item;
//import net.minecraft.item.ItemStack;
//
//public class Items {
//	private static boolean initDone = false;
//		
//	protected Items() {
//		throw new IllegalAccessError("Not a instantiable class");
//	}
//
//	/**
//	 *
//	 */
//	public static void init() {
//		if (initDone) {
//			return;
//		}
//		
//		MinIoC IoC = MinIoC.getInstance();
//		
//		Item gypsumPowder = addDust(Constants.GYPSUM);;
//		Item sulphurPowder = addDust(Constants.SULFUR);;
//		Item phosphorousPowder = addDust(Constants.PHOSPHOROUS);;
//		Item nitratePowder = addDust(Constants.NITRATE);
//		
//		Item mineralFertilizer = RegistrationHelper.registerItem(new MineralFertilizer(), "mineral_fertilizer")
//				.setTranslationKey(Mineralogy.MODID + "." + "mineral_fertilizer");
//		
//		IoC.register(Item.class, gypsumPowder, Constants.DUST_GYPSUM, Mineralogy.MODID);
//		IoC.register(Item.class, sulphurPowder, Constants.SULFUR, Mineralogy.MODID);
//		IoC.register(Item.class, phosphorousPowder, Constants.PHOSPHOROUS, Mineralogy.MODID);
//		IoC.register(Item.class, nitratePowder, Constants.NITRATE, Mineralogy.MODID);
//		IoC.register(Item.class, mineralFertilizer, Constants.FERTILIZER, Mineralogy.MODID);
//		
//		MineralogyRegistry.ItemsToRegister.put(Constants.FERTILIZER, mineralFertilizer);
//		
//		initDone = true;
//	}
//	
//	private static Item addDust(String oreDictionaryName) {
//		String dustName = oreDictionaryName.toLowerCase() + "_" + Constants.DUST;
//
//		Item item = RegistrationHelper.registerItem(new Item(), dustName).setTranslationKey(Mineralogy.MODID + "." + dustName);
//
//		try {
//			MinIoC.getInstance().resolve(IDynamicTabProvider.class).addToTab(item);
//		} catch (TabNotFoundException | ItemNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		MineralogyRegistry.ItemsToRegister.put(Constants.DUST + oreDictionaryName, item);
//		MinIoC.getInstance().register(Item.class, item, Constants.DUST + oreDictionaryName, Mineralogy.MODID);
//		
//		RecipeHelper.addShapelessOreRecipe(Constants.BLOCK.toLowerCase() + oreDictionaryName, new ItemStack(item, 9),
//				Constants.BLOCK.toLowerCase() + oreDictionaryName);
//
//		return item;
//	}
//}
