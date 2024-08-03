package com.husheng.fastman;

import com.husheng.fastman.block.ModBlocks;
import com.husheng.fastman.item.ModItemGroups;
import com.husheng.fastman.item.ModItems;
import com.husheng.fastman.recipe.ModRecipes;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FastManMod implements ModInitializer {
    public static final String MOD_ID = "fastman";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    
    @Override
    public void onInitialize() {
        
        // 注册物品组
        ModItemGroups.registerModItemGroups();
        // 注册方块
        ModBlocks.registerModBlocks();
        // 注册物品
        ModItems.registerModItems();
        // 注册合成表
        ModRecipes.registerRecipes();
    }
}
