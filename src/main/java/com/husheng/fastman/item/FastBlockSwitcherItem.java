package com.husheng.fastman.item;

import com.husheng.fastman.common.SwitchEnum;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.BlockState;
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
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class FastBlockSwitcherItem extends Item {
    
    public FastBlockSwitcherItem() {
        // super(new FabricItemSettings().maxCount(1).maxDamage(90));
        super(new FabricItemSettings().maxCount(1));
    }
    
    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        BlockPos pos = context.getBlockPos();
        PlayerEntity player = context.getPlayer();
        ItemStack itemStack = context.getStack();
        
        if (!world.isClient && player != null) {
            // 获取转换枚举
            SwitchEnum switchEnum = SwitchEnum.getSwitchEnum(world.getBlockState(pos).getBlock());
            if (switchEnum == null) {
                return ActionResult.PASS;
            }
            boolean isSuccess;
            try {
                isSuccess = switchEnum.getBaseSwitchHandler().switchTo(context);
            } catch (Exception e) {
                isSuccess = false;
            }
            
            if (isSuccess) {
                // 消耗耐久度
                // itemStack.damage(3, player, (p) -> p.sendToolBreakStatus(context.getHand()));
                world.playSound(null, pos, SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.BLOCKS, 1.0F, 1.0F);
                player.sendMessage(Text.translatable("info.switch.success"), true);
                return ActionResult.SUCCESS;
            } else {
                // 失败提示
                player.sendMessage(Text.translatable("info.switch.fail"), true);
                return ActionResult.FAIL;
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
    
    /**
     * 物品是否可以被附魔
     *
     * @param stack 物品
     * @return boolean
     */
    @Override
    public boolean isEnchantable(ItemStack stack) {
        return false;
    }
}
