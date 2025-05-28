package com.husheng.fastman.block.minecart.entity;

import com.husheng.fastman.block.minecart.BaseFastMinecartEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.vehicle.ChestMinecartEntity;
import net.minecraft.world.World;

public class FastChestMinecartEntity extends ChestMinecartEntity implements BaseFastMinecartEntity {
    
    public FastChestMinecartEntity(EntityType<? extends ChestMinecartEntity> entityType, World world) {
        super(entityType, world);
    }
}
