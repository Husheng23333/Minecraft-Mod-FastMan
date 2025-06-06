package com.husheng.fastman.register;

import com.husheng.fastman.FastManMod;
import com.husheng.fastman.item.FastBlockSwitcherItem;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {
    
    public static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, new Identifier(FastManMod.MOD_ID, name), item);
    }
    
    private static Item registerBlockItem(String name, BlockItem blockItem) {
        return Registry.register(Registries.ITEM, new Identifier(FastManMod.MOD_ID, name), blockItem);
    }
    
    public static void registerModItems() {
        FastManMod.LOGGER.info("注册自定义物品" + FastManMod.MOD_ID);
    }
    
    // NOTE 注册物品
    public static final Item FAST_SWITCHER = registerItem("fast_switcher", new FastBlockSwitcherItem());
    
    public static final Item FAST_HOPPER = registerBlockItem("fast_hopper", new BlockItem(ModBlocks.FAST_HOPPER_BLOCK, new FabricItemSettings()));
    
    public static final Item FAST_FURNACE = registerBlockItem("fast_furnace", new BlockItem(ModBlocks.FAST_FURNACE_BLOCK, new FabricItemSettings()));
}
