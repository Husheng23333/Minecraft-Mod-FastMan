package com.husheng.fastman.handle.switchHandle.common;

import com.husheng.fastman.handle.switchHandle.BaseSwitchHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Description: BlockToContainerHandler
 *
 * @author ZhangXinYu
 * @date 2025/5/20 14:16
 * @fileName BlockToContainerHandler.java
 **/
public class BlockToContainerHandler extends BaseSwitchHandler {
    
    public BlockToContainerHandler(Block fromBlock, Block toBlock) {
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
        // 获取当前方块实体
        BlockEntity nowBaseBlockEntity = world.getBlockEntity(pos);
        
        // 非容器方块直接替换
        if (!(world.getBlockEntity(pos) instanceof LootableContainerBlockEntity)) {
            BlockState newBlockState = copyBlockState(nowBlockState, toBlock);
            world.setBlockState(pos, newBlockState);
            return true;
        }
        
        // 当前方块是容器方块
        LootableContainerBlockEntity nowBlockEntity = (LootableContainerBlockEntity) world.getBlockEntity(pos);
        
        // 保存原容器物品
        DefaultedList<ItemStack> inventory = DefaultedList.ofSize(nowBlockEntity.size(), ItemStack.EMPTY);
        for (int i = 0; i < nowBlockEntity.size(); i++) {
            inventory.set(i, nowBlockEntity.getStack(i).copy());
        }
        
        // 移除原方块实体（避免物品弹出）
        world.removeBlockEntity(pos);
        world.removeBlock(pos, false);
        
        // 设置新方块状态
        BlockState newBlockState = copyBlockState(nowBlockState, toBlock);
        // 放置新方块
        world.setBlockState(pos, newBlockState);
        
        // 将物品转移到新容器中
        if ((world.getBlockEntity(pos) instanceof LootableContainerBlockEntity)) {
            LootableContainerBlockEntity newBlockEntity = (LootableContainerBlockEntity) world.getBlockEntity(pos);
            int transferSize = Math.min(inventory.size(), newBlockEntity.size());
            for (int i = 0; i < transferSize; i++) {
                newBlockEntity.setStack(i, inventory.get(i));
            }
        }
        
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
