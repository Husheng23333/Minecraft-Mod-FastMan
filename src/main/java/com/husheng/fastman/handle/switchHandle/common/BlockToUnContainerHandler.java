package com.husheng.fastman.handle.switchHandle.common;

import com.husheng.fastman.handle.switchHandle.BaseSwitchHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Description: BlockToContainerHandler
 *
 * @author ZhangXinYu
 * @date 2025/5/20 14:16
 * @fileName BlockToContainerHandler.java
 **/
public class BlockToUnContainerHandler extends BaseSwitchHandler {
    
    public BlockToUnContainerHandler(Block fromBlock, Block toBlock) {
        super(fromBlock, toBlock);
    }
    
    /**
     * 转换到目标方块
     *
     * @param context context
     */
    @Override
    public boolean switchTo(ItemUsageContext context) {
        boolean isCheckSuccess = checkContext(context);
        if (!isCheckSuccess) {
            return false;
        }
        
        World world = context.getWorld();
        // 获取方块位置
        BlockPos pos = context.getBlockPos();
        // 获取当前方块状态
        BlockState nowBlockState = world.getBlockState(pos);
        // 获取当前方块
        Block nowBlock = nowBlockState.getBlock();
        
        // 直接替换
        BlockState newBlockState = copyBlockState(nowBlockState, toBlock);
        world.setBlockState(pos, newBlockState);
        return true;
    }
    
    @Override
    protected boolean checkContext(ItemUsageContext context) {
        World world = context.getWorld();
        BlockPos pos = context.getBlockPos();
        BlockState nowBlockState = world.getBlockState(pos);
        Block nowBlock = nowBlockState.getBlock();
        
        // 检查当前方块
        return nowBlock == fromBlock;
    }
}
