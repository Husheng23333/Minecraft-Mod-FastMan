package com.husheng.fastman.block.custom;

import com.husheng.fastman.block.entity.FastFurnaceBlockEntity;
import com.husheng.fastman.register.ModBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.FurnaceBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class FastFurnaceBlock extends FurnaceBlock {
    
    public FastFurnaceBlock(Settings settings) {
        super(settings);
    }
    
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new FastFurnaceBlockEntity(pos, state);
    }
    
    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return world.isClient ? null : validateTicker(type, ModBlocks.FAST_FURNACE_BLOCK_ENTITY, FastFurnaceBlockEntity::tick);
    }
}
