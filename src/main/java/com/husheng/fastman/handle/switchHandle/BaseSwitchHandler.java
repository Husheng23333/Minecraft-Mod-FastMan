package com.husheng.fastman.handle.switchHandle;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;

/**
 * Description: BaseSwitchHandler
 *
 * @author ZhangXinYu
 * @date 2025/5/20 12:25
 * @fileName BaseSwitchHandler.java
 **/
public abstract class BaseSwitchHandler {
    protected Block fromBlock;
    protected Block toBlock;
    
    public BaseSwitchHandler(Block fromBlock, Block toBlock) {
        this.fromBlock = fromBlock;
        this.toBlock = toBlock;
    }
    
    /**
     * 转换到目标方块
     *
     * @param context context
     */
    public abstract boolean switchTo(ItemUsageContext context);
    
    /**
     * 获取当前方块朝向属性
     *
     * @param state 当前方块状态
     * @return DirectionProperty
     */
    protected DirectionProperty getFacingProperty(BlockState state) {
        if (state.contains(Properties.FACING)) {
            return Properties.FACING;
        } else if (state.contains(Properties.HORIZONTAL_FACING)) {
            return Properties.HORIZONTAL_FACING;
        } else if (state.contains(Properties.HOPPER_FACING)) {
            return Properties.HOPPER_FACING;
        }
        return null;
    }
    
    /**
     * 拷贝方块状态
     *
     * @param fromBlockState 当前方块状态
     * @param toBlock 目标方块
     * @return toBlockState
     */
    protected BlockState copyBlockState(BlockState fromBlockState, Block toBlock) {
        BlockState toBlockState = toBlock.getDefaultState();
        for (Property<?> property : fromBlockState.getProperties()) {
            try {
                // 检查新方块是否支持该属性
                if (toBlockState.contains(property)) {
                    toBlockState = copyProperty(fromBlockState, toBlockState, property);
                }
            } catch (Exception e) {
                continue;
            }
        }
        
        return toBlockState;
    }
    
    /**
     * 拷贝属性
     *
     * @param source 源方块状态
     * @param target 目标方块状态
     * @param property 属性
     * @return toProperty
     */
    protected <T extends Comparable<T>> BlockState copyProperty(BlockState source, BlockState target, Property<T> property) {
        return target.with(property, source.get(property));
    }
    
    /**
     * 检查当前方块
     *
     * @param context context
     * @return true/false
     */
    protected abstract boolean checkContext(ItemUsageContext context);
}
