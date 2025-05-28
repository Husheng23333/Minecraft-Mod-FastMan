package com.husheng.fastman.block.hopper.custom;

import com.husheng.fastman.block.hopper.entity.FastHopperBlockEntity;
import com.husheng.fastman.register.ModBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.HopperBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class FastHopperBlock extends HopperBlock {
    
    public FastHopperBlock(Settings settings) {
        super(settings);
    }
    
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new FastHopperBlockEntity(pos, state);
    }
    
    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return world.isClient ? null : validateTicker(type, ModBlocks.FAST_HOPPER_BLOCK_ENTITY, FastHopperBlockEntity::serverTick);
    }
    
    
}

