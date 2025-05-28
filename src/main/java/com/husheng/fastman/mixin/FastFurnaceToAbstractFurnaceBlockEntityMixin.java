package com.husheng.fastman.mixin;

import com.husheng.fastman.block.furnace.entity.FastFurnaceBlockEntity;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractFurnaceBlockEntity.class)
public abstract class FastFurnaceToAbstractFurnaceBlockEntityMixin {
    
    private final static int fast_times = 20;
    
    /**
     * 修改燃料燃烧时间
     */
    @Inject(
            method = "getFuelTime(Lnet/minecraft/item/ItemStack;)I",
            at = @At("RETURN"),
            cancellable = true
    )
    private void onGetFuelTime(ItemStack fuel, CallbackInfoReturnable<Integer> cir) {
        if ((Object)this instanceof FastFurnaceBlockEntity) {
            cir.setReturnValue(Math.max(1, cir.getReturnValueI() / fast_times));
        }
    }
    
    /**
     * 修改烧制时间
     */
    @Inject(
            method = "getCookTime(Lnet/minecraft/world/World;Lnet/minecraft/block/entity/AbstractFurnaceBlockEntity;)I",
            at = @At("RETURN"),
            cancellable = true
    )
    private static void onGetCookTime(World world, AbstractFurnaceBlockEntity furnace, CallbackInfoReturnable<Integer> cir) {
        if (furnace instanceof FastFurnaceBlockEntity) {
            cir.setReturnValue(Math.max(1, cir.getReturnValueI() / fast_times));
        }
    }
}
