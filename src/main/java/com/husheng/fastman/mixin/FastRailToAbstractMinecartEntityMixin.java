package com.husheng.fastman.mixin;

import com.husheng.fastman.block.rail.BaseFastRailBlockCustom;
import net.minecraft.block.AbstractRailBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractMinecartEntity.class)
public abstract class FastRailToAbstractMinecartEntityMixin {
    
    private static final double FAST_MULTIPLIER = 5.0;
    
    /**
     * 修改最大速度
     */
    @Inject(
            method = "getMaxSpeed",
            at = @At("RETURN"),
            cancellable = true
    )
    private void injectMaxSpeed(CallbackInfoReturnable<Double> cir) {
        
        AbstractMinecartEntity thisObj = (AbstractMinecartEntity) (Object) this;
        
        if (thisObj instanceof AbstractMinecartEntity || chackIsFastRail(thisObj)) {
            cir.setReturnValue(cir.getReturnValueD() * FAST_MULTIPLIER);
        }
    }
    
    /**
     * 修改加速度
     */
    @ModifyConstant(
        method = "moveOnRail",
        constant = @Constant(doubleValue = 0.0078125, ordinal = 0)
    )
    private double modifyRailAcceleration(double original, BlockPos pos, BlockState state) {
        if (state.getBlock() instanceof BaseFastRailBlockCustom) {
            return original * FAST_MULTIPLIER;
        }
        
        AbstractMinecartEntity thisObj = (AbstractMinecartEntity) (Object) this;
        if (thisObj instanceof AbstractMinecartEntity) {
            return original * FAST_MULTIPLIER;
        }
        
        return original;
    }
    
    /**
     * 修改减速度
     */
    @ModifyVariable(
            method = "applySlowdown",
            at = @At(value = "STORE"), // 在变量被存储时注入
            ordinal = 0                // 选择第一个存储点
    )
    private double modifyApplySlowdownD(double original) {
        
        AbstractMinecartEntity thisObj = (AbstractMinecartEntity) (Object) this;
        if (thisObj instanceof AbstractMinecartEntity) {
            return original * FAST_MULTIPLIER;
        }
        
        if (chackIsFastRail(thisObj)) {
            return original * FAST_MULTIPLIER;
        }
        
        return original;
    }
    
    /**
     * 检查是否在快速铁轨上
     */
    private static boolean chackIsFastRail(AbstractMinecartEntity minecartEntity) {
        int i = MathHelper.floor(minecartEntity.getX());
        int j = MathHelper.floor(minecartEntity.getY());
        int k = MathHelper.floor(minecartEntity.getZ());
        if (minecartEntity.getWorld().getBlockState(new BlockPos(i, j - 1, k)).isIn(BlockTags.RAILS)) {
            --j;
        }
        BlockPos blockPos = new BlockPos(i, j, k);
        BlockState blockState = minecartEntity.getWorld().getBlockState(blockPos);
        boolean onRail = AbstractRailBlock.isRail(blockState);
        
        if (!onRail) { // 不在铁轨上
            return false;
        }
        
        if (blockState.getBlock() instanceof BaseFastRailBlockCustom) {
            return true;
        }
        
        return false;
    }
}