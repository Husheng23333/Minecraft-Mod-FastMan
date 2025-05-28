package com.husheng.fastman.block.entity;

import com.husheng.fastman.register.ModBlocks;
import net.minecraft.block.*;
import net.minecraft.block.entity.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.screen.HopperScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class FastHopperBlockEntity extends HopperBlockEntity {
    
    public FastHopperBlockEntity(BlockPos pos, BlockState state) {
        super(pos, state);
    }
    
    @Override
    public BlockEntityType<?> getType() {
        return ModBlocks.FAST_HOPPER_BLOCK_ENTITY;
    }
    
    @Override
    public Text getName() {
        return Text.translatable("container.fast_hopper");
    }
    
    @Override
    protected Text getContainerName() {
        return Text.translatable("container.fast_hopper");
    }
    
    /**
     * 漏斗GUI
     *
     * @return ScreenHandler
     */
    @Override
    protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
        return new HopperScreenHandler(syncId, playerInventory, this);
    }
    
    // NOTE: 漏斗功能实现
    
    // 漏斗冷却时间
    private static final int MAX_TRANSFER_COOLDOWN = 8;
    // 容器
    private DefaultedList<ItemStack> inventory = DefaultedList.ofSize(5, ItemStack.EMPTY);
    
    private int transferCooldown = -1;
    private long lastTickTime;
    
    /**
     * 核心功能，每 1 tick 执行一次
     */
    public static void serverTick(World world, BlockPos pos, BlockState state, FastHopperBlockEntity blockEntity) {
        blockEntity.transferCooldown--;
        blockEntity.lastTickTime = world.getTime();
        
        if (!blockEntity.needsCooldown()) { // 无需冷却 执行传输逻辑
            blockEntity.setTransferCooldown(0);
            FastHopperBlockEntity.insertAndExtract(world, pos, state, blockEntity, () -> FastHopperBlockEntity.extract(world, blockEntity));
        }
    }
    
    /**
     * 每 1 tick 执行一次，检测实体碰撞(吸漏斗上面的物品)
     */
    public static void onEntityCollided(World world, BlockPos pos, BlockState state, Entity entity, FastHopperBlockEntity blockEntity) {
        ItemEntity itemEntity;
        if (entity instanceof ItemEntity &&
                !(itemEntity = (ItemEntity)entity).getStack().isEmpty() &&
                VoxelShapes.matchesAnywhere(VoxelShapes.cuboid(entity.getBoundingBox().offset(-pos.getX(), -pos.getY(), -pos.getZ())), blockEntity.getInputAreaShape(), BooleanBiFunction.AND)
        ) {
            FastHopperBlockEntity.insertAndExtract(world, pos, state, blockEntity, () -> FastHopperBlockEntity.extract(blockEntity, itemEntity));
        }
    }
    
    /**
     * 插入和提取物品
     *
     * @return boolean
     */
    private static boolean insertAndExtract(World world, BlockPos pos, BlockState state, FastHopperBlockEntity blockEntity, BooleanSupplier booleanSupplier) {
        if (world.isClient) {
            return false;
        }
        
        // 漏斗未被禁用且未冷却时执行
        if (!blockEntity.needsCooldown() && state.get(HopperBlock.ENABLED).booleanValue()) {
            
            boolean bl = false;
            
            // 漏斗中有物品时向下传输
            if (!blockEntity.isEmpty()) {
                bl = FastHopperBlockEntity.insert(world, pos, state, blockEntity);
            }
            
            // 漏斗未满时向上提取
            if (!blockEntity.isFull()) {
                bl |= booleanSupplier.getAsBoolean();
            }
            
            // 执行成功，进入冷却状态
            if (bl) {
                blockEntity.setTransferCooldown(MAX_TRANSFER_COOLDOWN);
                FastHopperBlockEntity.markDirty(world, pos, state);
                return true;
            }
        }
        return false;
    }
    
    /**
     * 向下传输物品 (每次传输一格物品)
     *
     * @return boolean
     */
    private static boolean insert(World world, BlockPos pos, BlockState state, Inventory inventory) {
        // 获取输出位置的容器（根据漏斗朝向）
        Inventory outputInventory = FastHopperBlockEntity.getOutputInventory(world, pos, state);
        if (outputInventory == null) {
            return false;
        }
        
        Direction direction = state.get(HopperBlock.FACING).getOpposite();
        if (FastHopperBlockEntity.isInventoryFull(outputInventory, direction)) {
            return false;
        }
        
        // 传输逻辑
        for (int i = 0; i < inventory.size(); ++i) {
            ItemStack stack = inventory.getStack(i);
            if (stack.isEmpty()) {
                continue;
            }
            
            // 传输并返回未传输的物品
            ItemStack remaining = FastHopperBlockEntity.transfer(inventory, outputInventory, stack.copy(), direction);
            
            // 传输成功则移除当前漏斗中已传输的物品
            if (remaining.getCount() < stack.getCount()) {
                
                int transferredCount = stack.getCount() - remaining.getCount();
                inventory.removeStack(i, transferredCount); // 移除传输的数量
                
                outputInventory.markDirty(); // NOTE: 触发更新保存
                return true;
            }
        }
        
        return false;
    }
    
    private static IntStream getAvailableSlots(Inventory inventory, Direction side) {
        if (inventory instanceof SidedInventory) {
            return IntStream.of(((SidedInventory)inventory).getAvailableSlots(side));
        }
        return IntStream.range(0, inventory.size());
    }
    
    private static boolean isInventoryFull(Inventory inventory, Direction direction) {
        return FastHopperBlockEntity.getAvailableSlots(inventory, direction).allMatch(slot -> {
            ItemStack itemStack = inventory.getStack(slot);
            return itemStack.getCount() >= itemStack.getMaxCount();
        });
    }
    
    private static boolean isInventoryEmpty(Inventory inv, Direction facing) {
        return FastHopperBlockEntity.getAvailableSlots(inv, facing).allMatch(slot -> inv.getStack(slot).isEmpty());
    }
    
    /**
     * 从上方(world)提取物品 到漏斗(hopper)中
     * @return boolean
     */
    public static boolean extract(World world, Hopper hopper) {
        // 从上方容器提取
        Inventory inventory = FastHopperBlockEntity.getInputInventory(world, hopper);
        if (inventory != null) {
            Direction direction = Direction.DOWN;
            if (FastHopperBlockEntity.isInventoryEmpty(inventory, direction)) {
                return false;
            }
            
            return FastHopperBlockEntity.getAvailableSlots(inventory, direction)
                                        .anyMatch(slot -> FastHopperBlockEntity.extract(hopper, inventory, slot, direction));
        }
        
        // 如果没有上方容器
        return false;
    }
    
    /**
     * 提取物品到漏斗中
     *
     * @return boolean
     */
    private static boolean extract(Hopper hopper, Inventory inventory, int slot, Direction side) {
        ItemStack sourceStack = inventory.getStack(slot);
        if (sourceStack.isEmpty() || !FastHopperBlockEntity.canExtract(hopper, inventory, sourceStack, slot, side)) {
            return false;
        }
        
        // 尝试传输物品
        ItemStack remaining = FastHopperBlockEntity.transfer(inventory, hopper, sourceStack.copy(), null);
        
        // 有物品被传输
        int originStackCount = sourceStack.getCount();
        if (remaining.getCount() < originStackCount) {
            int transferredCount = originStackCount - remaining.getCount();
            if (transferredCount == originStackCount) {
                inventory.removeStack(slot);
            } else {
                ItemStack newStack = inventory.getStack(slot).copy();
                newStack.decrement(transferredCount);
                inventory.setStack(slot, newStack);
            }
            inventory.markDirty();
            return true;
        }
        
        return false;
    }
    
    /**
     * 传输物品
     */
    public static ItemStack transfer(@Nullable Inventory from, Inventory to, ItemStack stack, @Nullable Direction side) {
        // 有方向性的容器
        if (to instanceof SidedInventory && side != null) {
            
            SidedInventory sidedInventory = (SidedInventory)to;
            int[] availableSlots = sidedInventory.getAvailableSlots(side);
            
            for (int slot : availableSlots) {
                int originalCount = stack.getCount();
                stack = transfer(from, to, stack, slot, side);
                
                //如有物品被转移，停止遍历
                if (stack.getCount() < originalCount) {
                    break;
                }
            }
        }
        
        // 无方向性的容器
        else {
            for (int slot = 0; slot < to.size(); slot++) {
                
                int before = stack.getCount();
                stack = transfer(from, to, stack, slot, side);
                
                //如有物品被转移，停止遍历
                if (stack.getCount() < before) {
                    break;
                }
            }
        }
        return stack;
    }
    
    private static ItemStack transfer(@Nullable Inventory from, Inventory to, ItemStack stack, int slot, @Nullable Direction side) {
        ItemStack targetStack = to.getStack(slot);
        
        if (!canInsert(to, stack, slot, side)) {
            return stack;
        }
        
        boolean changed = false;
        boolean wasEmpty = to.isEmpty();
        
        // 目标槽位为空，直接放入
        if (targetStack.isEmpty()) {
            int maxTransfer = Math.min(stack.getCount(), stack.getMaxCount());
            ItemStack toInsert = stack.copy();
            toInsert.setCount(maxTransfer);
            to.setStack(slot, toInsert);
            stack.decrement(maxTransfer);
            changed = true;
        }
        
        // 目标槽位有相同物品，尝试合并
        else if (FastHopperBlockEntity.canMergeItems(targetStack, stack)) {
            int maxTransfer = Math.min(stack.getCount(), targetStack.getMaxCount() - targetStack.getCount());
            if (maxTransfer > 0) {
                targetStack.increment(maxTransfer);
                stack.decrement(maxTransfer);
                to.setStack(slot, targetStack);
                changed = true;
            }
        }
        
        if (changed) {
            // 触发接收方冷却（防止 [漏斗A] → [漏斗B] → [漏斗A] 的无限循环）
            if (wasEmpty && to instanceof FastHopperBlockEntity toHopper) {
                int cooldown = MAX_TRANSFER_COOLDOWN;
                if (from instanceof FastHopperBlockEntity fromHopper) {
                    cooldown -= (toHopper.lastTickTime >= fromHopper.lastTickTime) ? 1 : 0;
                }
                toHopper.setTransferCooldown(cooldown);
            }
            
            to.markDirty();
        }
        
        return stack;
    }
    
    /**
     * 是否可以插入物品
     *
     * @return boolean
     */
    private static boolean canInsert(Inventory inventory, ItemStack stack, int slot, @Nullable Direction side) {
        SidedInventory sidedInventory;
        if (!inventory.isValid(slot, stack)) {
            return false;
        }
        return !(inventory instanceof SidedInventory) || (sidedInventory = (SidedInventory)inventory).canInsert(slot, stack, side);
    }
    
    private static boolean canExtract(Inventory hopperInventory, Inventory fromInventory, ItemStack stack, int slot, Direction facing) {
        SidedInventory sidedInventory;
        if (!fromInventory.canTransferTo(hopperInventory, slot, stack)) {
            return false;
        }
        return !(fromInventory instanceof SidedInventory) || (sidedInventory = (SidedInventory)fromInventory).canExtract(slot, stack, facing);
    }
    
    @Nullable
    private static Inventory getOutputInventory(World world, BlockPos pos, BlockState state) {
        Direction direction = state.get(HopperBlock.FACING);
        return FastHopperBlockEntity.getInventoryAt(world, pos.offset(direction));
    }
    
    @Nullable
    private static Inventory getInputInventory(World world, Hopper hopper) {
        return FastHopperBlockEntity.getInventoryAt(world, hopper.getHopperX(), hopper.getHopperY() + 1.0, hopper.getHopperZ());
    }
    
    public static List<ItemEntity> getInputItemEntities(World world, Hopper hopper) {
        return hopper.getInputAreaShape().getBoundingBoxes().stream().flatMap(box -> world.getEntitiesByClass(ItemEntity.class, box.offset(hopper.getHopperX() - 0.5, hopper.getHopperY() - 0.5, hopper.getHopperZ() - 0.5), EntityPredicates.VALID_ENTITY).stream()).collect(Collectors.toList());
    }
    
    @Nullable
    public static Inventory getInventoryAt(World world, BlockPos pos) {
        return FastHopperBlockEntity.getInventoryAt(world, (double)pos.getX() + 0.5, (double)pos.getY() + 0.5, (double)pos.getZ() + 0.5);
    }
    
    @Nullable
    private static Inventory getInventoryAt(World world, double x, double y, double z) {
        List<Entity> list;
        BlockEntity blockEntity;
        Inventory inventory = null;
        BlockPos blockPos = BlockPos.ofFloored(x, y, z);
        BlockState blockState = world.getBlockState(blockPos);
        Block block = blockState.getBlock();
        if (block instanceof InventoryProvider) {
            inventory = ((InventoryProvider)((Object)block)).getInventory(blockState, world, blockPos);
        } else if (blockState.hasBlockEntity() && (blockEntity = world.getBlockEntity(blockPos)) instanceof Inventory && (inventory = (Inventory)((Object)blockEntity)) instanceof ChestBlockEntity && block instanceof ChestBlock) {
            inventory = ChestBlock.getInventory((ChestBlock)block, blockState, world, blockPos, true);
        }
        if (inventory == null && !(list = world.getOtherEntities(null, new Box(x - 0.5, y - 0.5, z - 0.5, x + 0.5, y + 0.5, z + 0.5), EntityPredicates.VALID_INVENTORIES)).isEmpty()) {
            inventory = (Inventory)((Object)list.get(world.random.nextInt(list.size())));
        }
        return inventory;
    }
    
    private static boolean canMergeItems(ItemStack first, ItemStack second) {
        return first.getCount() <= first.getMaxCount() && ItemStack.canCombine(first, second);
    }
    
    private void setTransferCooldown(int transferCooldown) {
        this.transferCooldown = transferCooldown;
    }
    
    /**
     * 是否装满
     *
     * @return boolean
     */
    private boolean isFull() {
        for (ItemStack itemStack : this.inventory) {
            if (!itemStack.isEmpty() && itemStack.getCount() == itemStack.getMaxCount()) {
                continue;
            }
            return false;
        }
        return true;
    }
    
    /**
     * 是否需要冷却(transferCooldown > 0 时表示需要冷却)
     *
     * @return boolean
     */
    private boolean needsCooldown() {
        return this.transferCooldown > 0;
    }
    
    /**
     * 是否被禁用(transferCooldown > MAX_TRANSFER_COOLDOWN 时表示被禁用)
     *
     * @return boolean
     */
    private boolean isDisabled() {
        return this.transferCooldown > MAX_TRANSFER_COOLDOWN;
    }
    
    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        this.inventory = DefaultedList.ofSize(this.size(), ItemStack.EMPTY);
        if (!this.readLootTable(nbt)) {
            Inventories.readNbt(nbt, this.inventory);
        }
        this.transferCooldown = nbt.getInt("TransferCooldown");
    }
    
    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        if (!this.writeLootTable(nbt)) {
            Inventories.writeNbt(nbt, this.inventory);
        }
        nbt.putInt("TransferCooldown", this.transferCooldown);
    }
    
    @Override
    public int size() {
        return this.inventory.size();
    }
    
    @Override
    public ItemStack removeStack(int slot, int amount) {
        this.generateLoot(null);
        return Inventories.splitStack(this.method_11282(), slot, amount);
    }
    
    @Override
    public void setStack(int slot, ItemStack stack) {
        this.generateLoot(null);
        this.method_11282().set(slot, stack);
        if (stack.getCount() > this.getMaxCountPerStack()) {
            stack.setCount(this.getMaxCountPerStack());
        }
    }
    
    @Override
    public double getHopperX() {
        return (double)this.pos.getX() + 0.5;
    }
    
    @Override
    public double getHopperY() {
        return (double)this.pos.getY() + 0.5;
    }
    
    @Override
    public double getHopperZ() {
        return (double)this.pos.getZ() + 0.5;
    }
    
    @Override
    protected DefaultedList<ItemStack> method_11282() {
        return this.inventory;
    }
    
    @Override
    protected void setInvStackList(DefaultedList<ItemStack> list) {
        this.inventory = list;
    }
}
