package com.husheng.fastman.register;

import com.husheng.fastman.FastManMod;
import com.husheng.fastman.block.furnace.custom.FastFurnaceBlock;
import com.husheng.fastman.block.furnace.entity.FastFurnaceBlockEntity;
import com.husheng.fastman.block.hopper.custom.FastHopperBlock;
import com.husheng.fastman.block.hopper.entity.FastHopperBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModBlocks {
    
    private static Block registerBlock(String name, Block block) {
        return Registry.register(Registries.BLOCK, new Identifier(FastManMod.MOD_ID, name), block);
    }
    
    public static void registerModBlocks() {
        FastManMod.LOGGER.info("注册方块：" + FastManMod.MOD_ID);
    }
    
    // NOTE: 注册方块与实体
    // Fast Hopper
    public static final Block FAST_HOPPER_BLOCK = registerBlock("fast_hopper", new FastHopperBlock(Block.Settings.copy(Blocks.HOPPER)));
    
    public static BlockEntityType<FastHopperBlockEntity> FAST_HOPPER_BLOCK_ENTITY = Registry.register(
            Registries.BLOCK_ENTITY_TYPE,
            new Identifier(FastManMod.MOD_ID, "fast_hopper"),
            FabricBlockEntityTypeBuilder.create(FastHopperBlockEntity::new, ModBlocks.FAST_HOPPER_BLOCK).build(null)
    );
    
    // Fast Furnace
    public static final Block FAST_FURNACE_BLOCK = registerBlock("fast_furnace", new FastFurnaceBlock(Block.Settings.copy(Blocks.FURNACE)));
    
    public static BlockEntityType<FastFurnaceBlockEntity> FAST_FURNACE_BLOCK_ENTITY = Registry.register(
            Registries.BLOCK_ENTITY_TYPE,
            new Identifier(FastManMod.MOD_ID, "fast_furnace"),
            FabricBlockEntityTypeBuilder.create(FastFurnaceBlockEntity::new, ModBlocks.FAST_FURNACE_BLOCK).build(null)
    );
}
