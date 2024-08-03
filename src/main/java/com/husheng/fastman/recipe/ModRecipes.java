package com.husheng.fastman.recipe;


import com.husheng.fastman.FastManMod;
import com.husheng.fastman.recipe.custom.FastHopperRecipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModRecipes {
    public static final RecipeSerializer<FastHopperRecipe> FAST_HOPPER_RECIPE = Registry.register(
            Registries.RECIPE_SERIALIZER,
            new Identifier(FastManMod.MOD_ID, "fast_hopper"),
            new SpecialRecipeSerializer<>(FastHopperRecipe::new)
    );
    
    public static void registerRecipes() {
        FastManMod.LOGGER.info("注册自定义合成表" + FastManMod.MOD_ID);
    }
}
