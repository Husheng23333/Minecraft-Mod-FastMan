package com.husheng.fastman.block.minecart.entity;

import com.husheng.fastman.block.minecart.BaseFastMinecartEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.vehicle.MinecartEntity;
import net.minecraft.world.World;

public class FastMinecartEntity extends MinecartEntity implements BaseFastMinecartEntity {
    
    public FastMinecartEntity(EntityType<?> entityType, World world) {
        super(entityType, world);
    }
}
