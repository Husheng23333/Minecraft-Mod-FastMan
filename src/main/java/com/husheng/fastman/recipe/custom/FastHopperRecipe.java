package com.husheng.fastman.recipe.custom;

import com.husheng.fastman.item.ModItems;
import com.husheng.fastman.recipe.ModRecipes;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

import java.util.Random;

public class FastHopperRecipe extends SpecialCraftingRecipe {
    
    public FastHopperRecipe(CraftingRecipeCategory category) {
        super(category);
    }
    
    @Override
    public boolean matches(RecipeInputInventory inventory, World world) {
        boolean hasFastSwitcher = false;
        boolean hasFastHopper = false;
        boolean hasHopper = false;
        for (int i = 0; i < inventory.size(); i++) {
            ItemStack stack = inventory.getStack(i);
            if (stack.getItem() == ModItems.FAST_SWITCHER) {
                hasFastSwitcher = true;
            } else if (stack.getItem() == ModItems.FAST_HOPPER) {
                hasFastHopper = true;
            } else if (stack.getItem() == Items.HOPPER) {
                hasHopper = true;
            } else if (!stack.isEmpty()) {
                return false;
            }
        }
        // Check for fast_switcher + hopper or fast_switcher + fast_hopper
        return (hasFastSwitcher && hasHopper) || (hasFastSwitcher && hasFastHopper);
    }
    
    @Override
    public ItemStack craft(RecipeInputInventory inventory, DynamicRegistryManager registryManager) {
        boolean isFastSwitcherAndHopper = false;
        for (int i = 0; i < inventory.size(); i++) {
            ItemStack stack = inventory.getStack(i);
            if (stack.getItem() == ModItems.FAST_SWITCHER) {
                for (int j = 0; j < inventory.size(); j++) {
                    ItemStack otherStack = inventory.getStack(j);
                    if (otherStack.getItem() == Items.HOPPER) {
                        isFastSwitcherAndHopper = true;
                        break;
                    }
                }
                break;
            }
        }
        if (isFastSwitcherAndHopper) {
            return new ItemStack(ModItems.FAST_HOPPER);
        } else {
            return new ItemStack(Items.HOPPER);
        }
    }
    
    @Override
    public DefaultedList<ItemStack> getRemainder(RecipeInputInventory inventory) {
        DefaultedList<ItemStack> remainders = DefaultedList.ofSize(inventory.size(), ItemStack.EMPTY);
        Random random = new Random();
        for (int i = 0; i < inventory.size(); i++) {
            ItemStack stack = inventory.getStack(i);
            if (stack.getItem() == ModItems.FAST_SWITCHER) {
                ItemStack remainder = stack.copy();
                int unbreakingLevel = EnchantmentHelper.getLevel(Enchantments.UNBREAKING, stack);
                int damageToTake = 3;
                if (unbreakingLevel > 0) {
                    damageToTake = Math.max(1, 3 - (unbreakingLevel - 1));
                    float chanceToReduceDurability = unbreakingLevel * 0.1f;
                    if (unbreakingLevel >= 10) {
                        chanceToReduceDurability = 1.0f;
                    }
                    if (random.nextFloat() >= chanceToReduceDurability) {
                        remainder.setDamage(remainder.getDamage() + damageToTake);
                    }
                } else {
                    remainder.setDamage(remainder.getDamage() + damageToTake);
                }
                if (remainder.getDamage() >= remainder.getMaxDamage()) {
                    remainder = ItemStack.EMPTY;
                }
                remainders.set(i, remainder);
            } else if (stack.getItem() != Items.HOPPER && stack.getItem() != ModItems.FAST_HOPPER) {
                remainders.set(i, stack.copy());
            }
        }
        return remainders;
    }
    
    @Override
    public boolean fits(int width, int height) {
        return width >= 2 && height >= 2;
    }
    
    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.FAST_HOPPER_RECIPE;
    }
}







