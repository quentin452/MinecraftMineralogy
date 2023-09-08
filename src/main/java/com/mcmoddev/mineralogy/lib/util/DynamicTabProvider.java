package com.mcmoddev.mineralogy.lib.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.mcmoddev.mineralogy.Mineralogy;
import com.mcmoddev.mineralogy.ioc.MinIoC;
import com.mcmoddev.mineralogy.lib.data.Names;
import com.mcmoddev.mineralogy.lib.exceptions.TabNotFoundException;
import com.mcmoddev.mineralogy.lib.interfaces.IDynamicTabProvider;
import com.mcmoddev.mineralogy.lib.interfaces.IMMDMaterial;
import com.mcmoddev.mineralogy.lib.interfaces.ITabProvider;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;

public final class DynamicTabProvider implements IDynamicTabProvider {

    private final Map<String, MMDCreativeTab> tabs = new HashMap<>();
    private final Map<String, String> tabsByMod = new HashMap<>();
    private final Multimap<String, String> tabItemMapping = ArrayListMultimap.create();
    private IDynamicTabProvider.DefaultTabGenerationMode generationMode = DefaultTabGenerationMode.ByClass;
    private final ItemStack defaultIcon;

    public DynamicTabProvider() {
        MinIoC ioC = MinIoC.getInstance();
        defaultIcon = ioC.resolve(ItemStack.class, "defaultIcon", Mineralogy.MODID);
    }

    private MMDCreativeTab getTabByName(String tabName) {
        MMDCreativeTab tab = tabs.get(tabName);

        if(tab == null) {
            tab = tabs.get("default");
        }

        return tab;
    }

    @Override
    public DynamicTabProvider addToTab(String tabName, Block block) {

        MMDCreativeTab tab = getTabByName("default");

   //     ItemStack currentIcon = tab.createIcon();

      //  Item iconItem = currentIcon.getItem();

        if (FMLCommonHandler.instance()
            .getEffectiveSide() == Side.CLIENT) {

            //   assert iconItem != null;
           // if (iconItem.getClass() == Objects.requireNonNull(defaultIcon.getItem())
          //      .getClass()) {
         //       tab.setIconItem(block);
         //   }

            block.setCreativeTab(tab);

        }

        return this;

    }

    @Override
    public DynamicTabProvider addToTab(String tabName, Item item) {

        MMDCreativeTab tab = getTabByName(tabName);

        if (FMLCommonHandler.instance()
            .getEffectiveSide() == Side.CLIENT) {

            ItemStack currentIcon = tab.createIcon();
            Item iconItem = currentIcon.getItem();

            assert iconItem != null;
            if (iconItem.getClass() == Objects.requireNonNull(defaultIcon.getItem())
                .getClass()) {
                tab.setIconItem(item);
            }

        }

        item.setCreativeTab(tab);
        return this;

    }

    @Override
    public DynamicTabProvider setIcon(String tabName, IMMDMaterial material) {
        tabs.get(tabName)
            .setTabIconItem(
                new ItemStack(
                    Item.getItemFromBlock(
                        material.hasBlock(Names.BLOCK) ? material.getBlock(Names.BLOCK) : Blocks.iron_block)));
        return this;
    }

    private Optional<String> getTab(String itemName, String modID) {
        for (String tab : tabItemMapping.get(itemName)) if (modID.equals(tabsByMod.get(tab))) return Optional.of(tab);

        return Optional.empty();
    }

    public DynamicTabProvider setTabItemMapping(String tabName, String itemName) {
        tabItemMapping.put(itemName, tabName);
        return this;
    }

    private Optional<String> getTab(String itemName) {
        return tabItemMapping.get(itemName)
            .stream()
            .findFirst();
    }

    @Override
    public DynamicTabProvider addTab(String tabName, boolean searchable, String modID) {
        if (tabs.get(tabName) != null) return this;

        MMDCreativeTab tab = new MMDCreativeTab(String.format("%s.%s", modID, tabName), searchable);

        tab.Initialise();

        tabs.put(tabName, tab);
        tabsByMod.put(tabName, modID);
        setTabItemMapping(tabName, tabName);

        return this;
    }

    @Override
    public DynamicTabProvider addToTab(Block block) throws TabNotFoundException {
        addToTab(getTab(block), block);
        return this;
    }

    @Override
    public DynamicTabProvider addToTab(Item item) throws TabNotFoundException {
        addToTab(getTab(item), item);
        return this;
    }

    @Override
    public String[] getTabs() {
        return tabs.keySet()
            .toArray(new String[0]);
    }

    @Override
    public String getTab(Item item) {
        String className = item.getClass()
            .getSimpleName();
        return getTabByClass(className);
    }

    // todo fixme , maybe change the tab name?
    private String getTabByClass(String className) {
        String tab = tabItemMapping.get(className)
            .toString();
        if (tab == null) {
            tab = "Other";
        }

        return tab;
    }

    @Override
    public String getTab(Block block) {
        String className = block.getClass()
            .getSimpleName();

        return tabItemMapping.get(className)
            .toString();
    }

    private String getTabBySequence(String path, String domain, String simpleName) {
        return getTab(path, domain) // try getting a tab mapping
            .orElseGet(
                () -> getTab(path) // try a tab mapping without the mod id
                    .orElseGet(
                        () -> getTab(domain, domain) // try a tab mapping without just mod id
                            .orElseGet(
                                () -> getTab(simpleName, domain) // try and map on class name and mod id
                                    .orElseGet(
                                        () -> getTab(simpleName) // try and map just on class name
                                            .orElseGet(() -> { // add a tab to match the classname
                                                if (generationMode == DefaultTabGenerationMode.ByClass) {
                                                    addTab(simpleName, true, domain);
                                                    return simpleName;
                                                } else {
                                                    addTab(domain, true, domain);
                                                    return domain;
                                                }
                                            })))));
    }

    @Override
    public IDynamicTabProvider setDefaultTabCreationLogic(DefaultTabGenerationMode generationMode) {
        this.generationMode = generationMode;
        return this;
    }

    @Override
    public IDynamicTabProvider setTabIcons() {
        for (Entry<String, MMDCreativeTab> tabEntry : tabs.entrySet()) tabEntry.getValue()
            .setTabIconItem();

        return this;
    }

    @Override
    public ITabProvider setIcon(String tabName, ItemStack iconItem) {
        tabs.get(tabName)
            .setTabIconItem(iconItem);
        return this;
    }
}
