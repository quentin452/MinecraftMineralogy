package com.mcmoddev.mineralogy;

// This seems really not nice design, it'll do til we refactor the whole thing..

public class MineralogyEventBusSubscriber {

    // todo fixme
    /*
     * @SubscribeEvent
     * public static void registerBlocks(RegistryEvent.Register<Block> event) {
     * MineralogyRegistry.MineralogyBlockRegistry.values().forEach(block ->
     * event.getRegistry().register(block.PairedBlock));
     * event.getRegistry().registerAll(PatchHandler.MineralogyPatchRegistry.values()
     * .toArray(new Block[PatchHandler.MineralogyPatchRegistry.size()]));
     * }
     * @SubscribeEvent
     * public static void registerItems(RegistryEvent.Register<Item> event) {
     * event.getRegistry().registerAll(
     * MineralogyRegistry.MineralogyItemRegistry.values().toArray(new
     * Item[MineralogyRegistry.MineralogyItemRegistry.size()]));
     * for (Map.Entry<String, Block> map : MineralogyRegistry.BlocksToRegister.entrySet()) {
     * if (!map.getKey().contains("ITEMLESS"))
     * OreDictionary.registerOre(map.getKey(), map.getValue());
     * }
     * // make all of the rock types equivalent to cobblestone
     * if (MineralogyConfig.makeRockCobblestoneEquivilent()) {
     * for (int i = 0; i < MineralogyRegistry.metamorphicStones.size(); i++) {
     * OreDictionary.registerOre(Constants.COBBLESTONE, MineralogyRegistry.metamorphicStones.get(i));
     * }
     * for (int i = 0; i < MineralogyRegistry.igneousStones.size(); i++) {
     * OreDictionary.registerOre(Constants.COBBLESTONE, MineralogyRegistry.igneousStones.get(i));
     * }
     * }
     * for (Map.Entry<String, Item> map : MineralogyRegistry.ItemsToRegister.entrySet())
     * OreDictionary.registerOre(map.getKey(), map.getValue());
     * }
     * @SubscribeEvent
     * public static void registerRecipes(RegistryEvent.Register<IRecipe> event) {
     * event.getRegistry().registerAll(MineralogyRegistry.MineralogyRecipeRegistry.values()
     * .toArray(new IRecipe[MineralogyRegistry.MineralogyRecipeRegistry.size()]));
     * }
     */
}
