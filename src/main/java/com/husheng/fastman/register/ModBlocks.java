package com.husheng.fastman.register;

import com.husheng.fastman.FastManMod;
import com.husheng.fastman.block.custom.FastHopperBlock;
import com.husheng.fastman.block.entity.FastHopperBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

/**
 * Description: 方块注册类
 *
 * @author HuSheng
 * @date 2024/7/20 下午7:36
 * @fileName ModBlocks.java
 **/
public class ModBlocks {
    
    private static Block registerBlock(String name, Block block) {
        return Registry.register(Registries.BLOCK, new Identifier(FastManMod.MOD_ID, name), block);
    }
    
    public static void registerModBlocks() {
        FastManMod.LOGGER.info("注册方块：" + FastManMod.MOD_ID);
    }
    
    // NOTE: 注册方块与实体
    public static final Block FAST_HOPPER_BLOCK = registerBlock("fast_hopper", new FastHopperBlock(Block.Settings.copy(Blocks.HOPPER)));
    
    public static BlockEntityType<FastHopperBlockEntity> FAST_HOPPER_BLOCK_ENTITY = Registry.register(
            Registries.BLOCK_ENTITY_TYPE,
            new Identifier(FastManMod.MOD_ID, "fast_hopper"),
            FabricBlockEntityTypeBuilder.create(FastHopperBlockEntity::new, ModBlocks.FAST_HOPPER_BLOCK).build(null)
    );
}
