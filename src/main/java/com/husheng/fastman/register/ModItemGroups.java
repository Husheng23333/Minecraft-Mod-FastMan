package com.husheng.fastman.register;

import com.husheng.fastman.FastManMod;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModItemGroups {
    
    public static void registerModItemGroups() {
        FastManMod.LOGGER.info("注册自定义物品组" + FastManMod.MOD_ID);
    }
    
    // NOTE 注册物品组
    public static final ItemGroup RUBY_GROUP = Registry.register(
            Registries.ITEM_GROUP,
            new Identifier(FastManMod.MOD_ID, "mod"),
            FabricItemGroup.builder().displayName(Text.translatable("itemgroup.mod"))
                           .icon(() -> new ItemStack(ModItems.FAST_SWITCHER))
                           .entries((displayContext, entries) -> {
                               entries.add(ModItems.FAST_SWITCHER);
                               entries.add(ModItems.FAST_HOPPER);
                           }).build());
}
