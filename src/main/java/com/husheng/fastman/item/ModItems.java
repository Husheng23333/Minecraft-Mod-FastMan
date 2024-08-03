package com.husheng.fastman.item;

import com.husheng.fastman.FastManMod;
import com.husheng.fastman.block.ModBlocks;
import com.husheng.fastman.item.custom.HopperSwitcherItem;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

/**
 * Description: 物品注册类
 *
 * @author HuSheng
 * @date 2024/7/20 下午7:33
 * @fileName ModItems.java
 **/
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
    
    // TODO: HuSheng 2024/7/20 | 这里添加新的物品
    public static final Item FAST_SWITCHER = registerItem("fast_switcher", new HopperSwitcherItem(new FabricItemSettings().maxCount(1).maxDamage(90)));
    
    public static final Item FAST_HOPPER = registerBlockItem("fast_hopper", new BlockItem(ModBlocks.FAST_HOPPER_BLOCK, new FabricItemSettings()));
}
