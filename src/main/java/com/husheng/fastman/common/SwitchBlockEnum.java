package com.husheng.fastman.common;

import com.husheng.fastman.handle.switchHandle.BaseSwitchHandler;
import com.husheng.fastman.handle.switchHandle.common.BlockToContainerHandler;
import com.husheng.fastman.register.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;

public enum SwitchBlockEnum {
    
    // Fast Hopper
    Hopper_to_FastHopper(Blocks.HOPPER, ModBlocks.FAST_HOPPER_BLOCK, new BlockToContainerHandler(Blocks.HOPPER, ModBlocks.FAST_HOPPER_BLOCK)),
    FastHopper_to_Hopper(ModBlocks.FAST_HOPPER_BLOCK, Blocks.HOPPER, new BlockToContainerHandler(ModBlocks.FAST_HOPPER_BLOCK, Blocks.HOPPER)),
    
    ;
    private Block fromBlock;
    private Block toBlock;
    private BaseSwitchHandler baseSwitchHandler;
    
    SwitchBlockEnum(Block fromBlock, Block toBlock, BaseSwitchHandler baseSwitchHandler) {
        this.fromBlock = fromBlock;
        this.toBlock = toBlock;
        this.baseSwitchHandler = baseSwitchHandler;
    }
    
    public Block getFromBlock() {
        return fromBlock;
    }
    
    public Block getToBlock() {
        return toBlock;
    }
    
    public BaseSwitchHandler getBaseSwitchHandler() {
        return baseSwitchHandler;
    }
    
    /**
     * 获取当前方块对应的转换枚举
     *
     * @param nowBlock 当前方块
     * @return SwitchBlockEnum
     */
    public static SwitchBlockEnum getSwitchEnum(Block nowBlock) {
        if (nowBlock == null) {
            return null;
        }
        for (SwitchBlockEnum switchBlockEnum : values()) {
            if (switchBlockEnum.getFromBlock() == nowBlock) {
                return switchBlockEnum;
            }
        }
        return null;
    }
}
