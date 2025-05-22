//package com.husheng.fastman.mixin;
//
//import com.husheng.fastman.block.custom.FastHopperBlock;
//import com.husheng.fastman.block.entity.FastHopperBlockEntity;
//import net.minecraft.block.BlockState;
//import net.minecraft.block.entity.HopperBlockEntity;
//import net.minecraft.state.property.Properties;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.world.World;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.Shadow;
//import org.spongepowered.asm.mixin.Unique;
//import org.spongepowered.asm.mixin.injection.*;
//import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
//
//import java.util.function.BooleanSupplier;
//
//@Mixin(HopperBlockEntity.class)
//public abstract class FastHopperBlockEntityMixin {
//
//    @Shadow
//    public abstract void setTransferCooldown(int cooldown);
//
//    @Unique
//    private static int getCooldown(int initialValue, HopperBlockEntity blockEntity) {
//        if (blockEntity instanceof FastHopperBlockEntity) {
//            return 1;
//        }
//        return initialValue;
//    }
//
//    // 判断是否可用(被红石锁住则不可用)
//    @Unique
//    private static boolean isEnabled(World world, BlockPos pos) {
//        BlockState state = world.getBlockState(pos);
//        if (state.getBlock() instanceof FastHopperBlock) {
//            return state.get(Properties.ENABLED);
//        }
//        return true;
//    }
//
//    @Inject(method = "insertAndExtract", at = @At("HEAD"), cancellable = true)
//    private static void onInsertAndExtract(World world, BlockPos pos, BlockState state, HopperBlockEntity blockEntity, BooleanSupplier booleanSupplier, CallbackInfoReturnable<Boolean> cir) {
//        if (blockEntity instanceof FastHopperBlockEntity && !isEnabled(world, pos)) {
//            cir.setReturnValue(false);
//        }
//    }
//
//    @ModifyConstant(method = "insertAndExtract", constant = @Constant(intValue = 8))
//    private static int modifyLongerInsertAndExtractCooldown(int initialValue, World world, BlockPos pos, BlockState state, HopperBlockEntity blockEntity, BooleanSupplier booleanSupplier) {
//        return FastHopperBlockEntityMixin.getCooldown(initialValue, blockEntity);
//    }
//
//    @Redirect(method = "transfer(Lnet/minecraft/inventory/Inventory;Lnet/minecraft/inventory/Inventory;Lnet/minecraft/item/ItemStack;ILnet/minecraft/util/math/Direction;)Lnet/minecraft/item/ItemStack;", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/entity/HopperBlockEntity;setTransferCooldown(I)V"))
//    private static void setLongerTransferCooldown(HopperBlockEntity blockEntity, int cooldown) {
//        ((FastHopperBlockEntityMixin) (Object) blockEntity).setTransferCooldown(cooldown + (blockEntity instanceof HopperBlockEntity ? 4 : 0));
//    }
//
//    @ModifyConstant(method = "isDisabled", constant = @Constant(intValue = 8))
//    private int modifyLongerDisabledCooldown(int initialValue) {
//        return FastHopperBlockEntityMixin.getCooldown(initialValue, (HopperBlockEntity) (Object) this);
//    }
//}
