package com.husheng.fastman.common;

import com.husheng.fastman.register.ModItems;
import net.minecraft.item.Item;
import net.minecraft.item.Items;

public enum SwitchItemEnum {
    
    // Fast Hopper
    Hopper_to_FastHopper(Items.HOPPER, ModItems.FAST_HOPPER),
    FastHopper_to_Hopper(ModItems.FAST_HOPPER, Items.HOPPER),
    
    // Fast Furnace
    Furnace_to_FastFurnace(Items.FURNACE, ModItems.FAST_FURNACE),
    FastFurnace_to_Furnace(ModItems.FAST_FURNACE, Items.FURNACE),
    
    ;
    private Item fromItem;
    private Item toItem;
    
    SwitchItemEnum(Item fromItem, Item toItem) {
        this.fromItem = fromItem;
        this.toItem = toItem;
    }
    
    public Item getFromItem() {
        return fromItem;
    }
    
    public Item getToItem() {
        return toItem;
    }
    
    public static SwitchItemEnum getSwitchEnum(Item nowItem) {
        if (nowItem == null) {
            return null;
        }
        for (SwitchItemEnum switchItemEnum : values()) {
            if (switchItemEnum.getFromItem() == nowItem) {
                return switchItemEnum;
            }
        }
        return null;
    }
    
}
