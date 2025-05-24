package com.husheng.fastman.block.entity;

import com.husheng.fastman.register.ModBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.FurnaceBlockEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.FurnaceScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

/**
 * Description: FastFurnaceBlockEntity
 *
 * @author ZhangXinYu
 * @date 2025/5/21 15:21
 * @fileName FastFurnaceBlockEntity.java
 **/
public class FastFurnaceBlockEntity extends FurnaceBlockEntity {
    
    public FastFurnaceBlockEntity(BlockPos pos, BlockState state) {
        super(pos, state);
    }
    
    @Override
    public BlockEntityType<?> getType() {
        return ModBlocks.FAST_FURNACE_BLOCK_ENTITY;
    }
    
    @Override
    public Text getName() {
        return Text.translatable("container.fast_furnace");
    }
    
    @Override
    protected Text getContainerName() {
        return Text.translatable("container.fast_furnace");
    }
    
    /**
     * 熔炉GUI
     *
     * @return ScreenHandler
     */
    @Override
    protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
        return new FurnaceScreenHandler(syncId, playerInventory, this, this.propertyDelegate);
    }
    
    // NOTE: 熔炉功能实现
    
    
//    @Override
//    protected int getFuelTime(ItemStack fuel) {
//        return super.getFuelTime(fuel) / 10;
//    }
}
