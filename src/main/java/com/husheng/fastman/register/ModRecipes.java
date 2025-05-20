package com.husheng.fastman.register;

import com.husheng.fastman.FastManMod;
import com.husheng.fastman.recipe.FastItemSwitchRecipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModRecipes {
    
    public static void registerRecipes() {
        FastManMod.LOGGER.info("注册自定义合成表" + FastManMod.MOD_ID);
    }
    
    // NOTE 注册合成表
    public static final RecipeSerializer<FastItemSwitchRecipe> FAST_ITEM_SWITCH_RECIPE = Registry.register(
            Registries.RECIPE_SERIALIZER,
            new Identifier(FastManMod.MOD_ID, "fast_item_switch"),
            new SpecialRecipeSerializer<>(FastItemSwitchRecipe::new)
    );
    
    
}
