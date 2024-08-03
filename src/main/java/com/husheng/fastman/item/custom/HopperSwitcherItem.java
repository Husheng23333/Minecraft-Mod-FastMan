package com.husheng.fastman.item.custom;

import com.husheng.fastman.block.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class HopperSwitcherItem extends Item {
    
    public HopperSwitcherItem(Settings settings) {
        super(settings);
    }
    
    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        BlockPos pos = context.getBlockPos();
        PlayerEntity player = context.getPlayer();
        ItemStack itemStack = context.getStack();
        
        if (!world.isClient && player != null) {
            
            BlockState currentState = world.getBlockState(pos);
            Block currentBlock = currentState.getBlock();
            DirectionProperty facingProperty = getFacingProperty(currentState);
            
            if (currentBlock == Blocks.HOPPER) {
                // 获取原版漏斗中的物品
                BlockEntity blockEntity = world.getBlockEntity(pos);
                DefaultedList<ItemStack> inventory = DefaultedList.ofSize(5, ItemStack.EMPTY);
                if (blockEntity instanceof HopperBlockEntity) {
                    HopperBlockEntity hopperBlockEntity = (HopperBlockEntity) blockEntity;
                    for (int i = 0; i < hopperBlockEntity.size(); i++) {
                        inventory.set(i, hopperBlockEntity.getStack(i).copy());
                    }
                }
                
                // 设置新的快速漏斗状态
                BlockState newState = ModBlocks.FAST_HOPPER_BLOCK.getDefaultState();
                if (facingProperty != null) {
                    newState = newState.with(facingProperty, currentState.get(facingProperty));
                }
                boolean isEnbaled = currentState.get(Properties.ENABLED);
                newState = newState.with(Properties.ENABLED, isEnbaled);
                
                // 移除原版漏斗（避免物品弹出）
                world.removeBlockEntity(pos);
                world.removeBlock(pos, false);
                
                // 放置新的快速漏斗
                world.setBlockState(pos, newState);
                world.playSound(null, pos, SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.BLOCKS, 1.0F, 1.0F);
                player.sendMessage(Text.translatable("info.switch.fast_hopper"), true);
                
                // 将物品转移到新的快速漏斗中
                BlockEntity newBlockEntity = world.getBlockEntity(pos);
                if (newBlockEntity instanceof HopperBlockEntity) {
                    HopperBlockEntity newHopperBlockEntity = (HopperBlockEntity) newBlockEntity;
                    for (int i = 0; i < inventory.size(); i++) {
                        newHopperBlockEntity.setStack(i, inventory.get(i));
                    }
                }
                
                // 消耗耐久度
                itemStack.damage(3, player, (p) -> p.sendToolBreakStatus(context.getHand()));
                
                return ActionResult.SUCCESS;
                
            } else if (currentBlock == ModBlocks.FAST_HOPPER_BLOCK) {
                // 获取快速漏斗中的物品
                BlockEntity blockEntity = world.getBlockEntity(pos);
                DefaultedList<ItemStack> inventory = DefaultedList.ofSize(5, ItemStack.EMPTY);
                if (blockEntity instanceof HopperBlockEntity) {
                    HopperBlockEntity hopperBlockEntity = (HopperBlockEntity) blockEntity;
                    for (int i = 0; i < hopperBlockEntity.size(); i++) {
                        inventory.set(i, hopperBlockEntity.getStack(i).copy());
                    }
                }
                
                // 设置新的原版漏斗状态
                BlockState newState = Blocks.HOPPER.getDefaultState();
                if (facingProperty != null) {
                    newState = newState.with(facingProperty, currentState.get(facingProperty));
                }
                
                // 移除快速漏斗（避免物品弹出）
                world.removeBlockEntity(pos);
                world.removeBlock(pos, false);
                
                // 放置新的原版漏斗
                world.setBlockState(pos, newState);
                world.playSound(null, pos, SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.BLOCKS, 1.0F, 1.0F);
                player.sendMessage(Text.translatable("info.switch.original_hopper"), true);
                
                // 将物品转移到新的原版漏斗中
                BlockEntity newBlockEntity = world.getBlockEntity(pos);
                if (newBlockEntity instanceof HopperBlockEntity) {
                    HopperBlockEntity newHopperBlockEntity = (HopperBlockEntity) newBlockEntity;
                    for (int i = 0; i < inventory.size(); i++) {
                        newHopperBlockEntity.setStack(i, inventory.get(i));
                    }
                }
                
                // 消耗耐久度
                itemStack.damage(3, player, (p) -> p.sendToolBreakStatus(context.getHand()));
                
                return ActionResult.SUCCESS;
            }
        }
        return ActionResult.PASS;
    }
    
    private DirectionProperty getFacingProperty(BlockState state) {
        if (state.contains(Properties.FACING)) {
            return Properties.FACING;
        } else if (state.contains(Properties.HORIZONTAL_FACING)) {
            return Properties.HORIZONTAL_FACING;
        } else if (state.contains(Properties.HOPPER_FACING)) {
            return Properties.HOPPER_FACING;
        }
        return null;
    }
    
    // 使物品可以被附魔
    @Override
    public boolean isEnchantable(ItemStack stack) {
        return true;
    }
}
