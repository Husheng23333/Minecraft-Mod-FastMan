package com.husheng.fastman.recipe;

import com.husheng.fastman.common.SwitchItemEnum;
import com.husheng.fastman.register.ModItems;
import com.husheng.fastman.register.ModRecipes;
import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

public class FastItemSwitchRecipe extends SpecialCraftingRecipe {
    
    public FastItemSwitchRecipe(CraftingRecipeCategory category) {
        super(category);
    }
    
    /**
     * 判断是否可合成
     *
     * @param inventory inventory
     * @param world world
     * @return boolean
     */
    @Override
    public boolean matches(RecipeInputInventory inventory, World world) {
        boolean hasFastSwitcher = false;
        boolean hasFromItem = false;
        
        for (int i = 0; i < inventory.size(); i++) {
            ItemStack stack = inventory.getStack(i);
            if (stack.isEmpty() || stack.getItem() == null) {
                continue;
            }
            
            if (stack.getItem() == ModItems.FAST_SWITCHER) {
                if (hasFastSwitcher) {
                    // 保证只有一个fast_switcher
                    return false;
                }
                hasFastSwitcher = true;
            } else {
                SwitchItemEnum switchItemEnum = SwitchItemEnum.getSwitchEnum(stack.getItem());
                if (switchItemEnum != null) {
                    if (hasFromItem) {
                        // 保证只有一个fromItem
                        return false;
                    }
                    hasFromItem = true;
                } else {
                    // 其他物品不允许
                    return false;
                }
            }
        }
        
        return hasFastSwitcher && hasFromItem;
    }
    
    /**
     * 展示合成的物品
     *
     * @param inventory inventory
     * @param registryManager registryManager
     * @return ItemStack
     */
    @Override
    public ItemStack craft(RecipeInputInventory inventory, DynamicRegistryManager registryManager) {
        
        SwitchItemEnum switchItemEnum = null;
        for (int i = 0; i < inventory.size(); i++) {
            ItemStack stack = inventory.getStack(i);
            if (stack.isEmpty() || stack.getItem() == null || stack.getItem() == ModItems.FAST_SWITCHER) {
                // 跳过空物品和fast_switcher
                continue;
            }
            
            switchItemEnum = SwitchItemEnum.getSwitchEnum(stack.getItem());
            break;
        }
        
        return switchItemEnum != null ? new ItemStack(switchItemEnum.getToItem()) : ItemStack.EMPTY;
    }
    
    /**
     * 合成结果
     *
     * @param inventory inventory
     * @return DefaultedList<ItemStack>
     */
    @Override
    public DefaultedList<ItemStack> getRemainder(RecipeInputInventory inventory) {
        DefaultedList<ItemStack> remainders = DefaultedList.ofSize(inventory.size(), ItemStack.EMPTY);
        boolean isRemainder = false;
        for (int i = 0; i < inventory.size(); i++) {
            ItemStack stack = inventory.getStack(i);
            
            if (isRemainder || stack.isEmpty() || stack.getItem() == null || stack.getItem() == ModItems.FAST_SWITCHER) {
                // 空物品、fast_switcher保留，已匹配合成则剩余全保留
                remainders.set(i, stack.copy());
                continue;
            }
            
            SwitchItemEnum switchEnum = SwitchItemEnum.getSwitchEnum(stack.getItem());
            if (switchEnum == null) {
                remainders.set(i, stack.copy());
                continue;
            }
            
            // 匹配到的物品-1 （设置为ItemStack.EMPTY就是-1）
            remainders.set(i, ItemStack.EMPTY);
            isRemainder = true;
        }
        return remainders;
    }
    
    @Override
    public boolean fits(int width, int height) {
        return width >= 2 && height >= 2;
    }
    
    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.FAST_ITEM_SWITCH_RECIPE;
    }
}