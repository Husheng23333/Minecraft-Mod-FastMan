package com.husheng.fastman.block.entity;


import com.husheng.fastman.register.ModBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

public class FastHopperBlockEntity extends HopperBlockEntity {
    
    public FastHopperBlockEntity(BlockPos pos, BlockState state) {
        super(pos, state);
    }
    
    @Override
    public BlockEntityType<?> getType() {
        return ModBlocks.FAST_HOPPER_BLOCK_ENTITY;
    }
    
    @Override
    public Text getName() {
        return Text.translatable("container.fast_hopper");
    }
}
