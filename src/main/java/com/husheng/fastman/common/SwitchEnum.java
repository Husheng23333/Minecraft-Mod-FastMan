package com.husheng.fastman.common;

import com.husheng.fastman.handle.switchHandle.BaseSwitchHandler;
import com.husheng.fastman.handle.switchHandle.fastHopper.FastHopperToHopperHandler;
import com.husheng.fastman.handle.switchHandle.fastHopper.HopperToFastHopperHandler;
import com.husheng.fastman.register.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;

public enum SwitchEnum {
    
    // Fast Hopper
    Hopper_to_FastHopper(Blocks.HOPPER, ModBlocks.FAST_HOPPER_BLOCK, new HopperToFastHopperHandler(Blocks.HOPPER, ModBlocks.FAST_HOPPER_BLOCK)),
    FastHopper_to_Hopper(ModBlocks.FAST_HOPPER_BLOCK, Blocks.HOPPER, new FastHopperToHopperHandler(ModBlocks.FAST_HOPPER_BLOCK, Blocks.HOPPER)),
    
    ;
    private Block fromBlock;
    private Block toBlock;
    private BaseSwitchHandler baseSwitchHandler;
    
    SwitchEnum(Block fromBlock, Block toBlock, BaseSwitchHandler baseSwitchHandler) {
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
     * @return SwitchEnum
     */
    public static SwitchEnum getSwitchEnum(Block nowBlock) {
        if (nowBlock == null) {
            return null;
        }
        for (SwitchEnum switchEnum : values()) {
            if (switchEnum.getFromBlock() == nowBlock) {
                return switchEnum;
            }
        }
        return null;
    }
}
