package bedrockium.container;

import bedrockium.energy.BedrockiumEnergyStorage;
import bedrockium.init.ModBlocks;
import bedrockium.init.ModItems;
import bedrockium.tileentity.TileEntityBedrockiumMiner;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.IntArray;
import net.minecraft.util.IntReferenceHolder;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;


public class ContainerBedrockiumMiner extends Container {

    protected TileEntityBedrockiumMiner te;
    protected IIntArray fields;
    protected PlayerEntity playerEntity;
    protected IItemHandler playerInventory;
    protected final World world;

    public ContainerBedrockiumMiner(int windowId, World world, BlockPos pos, PlayerInventory playerInventory, PlayerEntity player) {
        this(windowId, world, pos, playerInventory, player, new IntArray(2));
    }

    public ContainerBedrockiumMiner(int windowId, World world, BlockPos pos, PlayerInventory playerInventory, PlayerEntity player, IIntArray fields) {
        super(ModBlocks.MINER_CONTAINER, windowId);
        this.te = (TileEntityBedrockiumMiner) world.getTileEntity(pos);
        this.playerEntity = player;
        this.playerInventory = new InvWrapper(playerInventory);
        this.world = playerInventory.player.world;
        this.fields = fields;
        assertIntArraySize(this.fields, 2);
        this.trackIntArray(this.fields);

        this.addSlot(new SlotDrillHead(te, 0, 53, 48));
        this.addSlot(new SlotEnergy(te, 1, 8, 62));
        this.addSlot(new SlotOutput(te, 2, 116, 33));
        this.addSlot(new SlotDiamond(te, 3, 53, 18));
        layoutPlayerInventorySlots(8, 92);




        this.trackInt(new IntReferenceHolder() {
            @Override
            public int get() {
                return te.getCapability(CapabilityEnergy.ENERGY).map(IEnergyStorage::getEnergyStored).orElse(0);
            }

            @Override
            public void set(int value) {
                te.getCapability(CapabilityEnergy.ENERGY).ifPresent(h -> ((BedrockiumEnergyStorage)h).setEnergy(value));
            }

        });

    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
    }

    @Override
    public void updateProgressBar(int id, int data) {
        super.updateProgressBar(id, data);
        this.te.fields.set(id, data);

    }


    @OnlyIn(Dist.CLIENT)
    public int getCookScaled(int pixels) {
        int lvt_1_1_ = this.fields.get(0);
        int lvt_2_1_ = this.fields.get(1);
        return lvt_2_1_ != 0 && lvt_1_1_ != 0 ? lvt_1_1_ * pixels / lvt_2_1_ : 0;
    }

    @OnlyIn(Dist.CLIENT)
    public int getPowerScaled(int pixels) {
        int lvt_1_1_ = this.getEnergy();
        int lvt_2_1_ = this.getCapacity();
        return lvt_2_1_ != 0 && lvt_1_1_ != 0 ? lvt_1_1_ * pixels / lvt_2_1_ : 0;
    }

    @OnlyIn(Dist.CLIENT)
    public String getProgress() {
        int i = this.fields.get(0);
        int j = this.fields.get(1);
        float percent = (float) i / j * 100.0f;
        return (int)percent + "%";
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return isWithinUsableDistance(IWorldPosCallable.of(te.getWorld(), te.getPos()), playerEntity, ModBlocks.miner);
    }


    public int getEnergy() {
        return te.getCapability(CapabilityEnergy.ENERGY).map(IEnergyStorage::getEnergyStored).orElse(0);
    }

    public int getCapacity() {
        return te.getCapability(CapabilityEnergy.ENERGY).map(IEnergyStorage::getMaxEnergyStored).orElse(0);
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if (index == 2) {
                if (!this.mergeItemStack(itemstack1, 4, 39, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onSlotChange(itemstack1, itemstack);
            } else if (index != 1 && index != 0 && index != 3) {
                if (itemstack1.getItem() == Items.DIAMOND) {
                    if (!this.mergeItemStack(itemstack1, 3, 4, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (itemstack1.getItem() == ModItems.drill_head) {
                    if (!this.mergeItemStack(itemstack1, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                }else if (itemstack1.getCapability(CapabilityEnergy.ENERGY).isPresent()) {
                    if (!this.mergeItemStack(itemstack1, 1, 2, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index >= 4 && index < 30) {
                    if (!this.mergeItemStack(itemstack1, 30, 39, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index >= 30 && index < 39 && !this.mergeItemStack(itemstack1, 4, 30, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(itemstack1, 4, 39, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(playerIn, itemstack1);
        }
        return itemstack;
    }



    private int addSlotRange(IItemHandler handler, int index, int x, int y, int amount, int dx) {
        for (int i = 0 ; i < amount ; i++) {
            addSlot(new SlotItemHandler(handler, index, x, y));
            x += dx;
            index++;
        }
        return index;
    }

    private int addSlotBox(IItemHandler handler, int index, int x, int y, int horAmount, int dx, int verAmount, int dy) {
        for (int j = 0 ; j < verAmount ; j++) {
            index = addSlotRange(handler, index, x, y, horAmount, dx);
            y += dy;
        }
        return index;
    }

    private void layoutPlayerInventorySlots(int leftCol, int topRow) {
        // Player inventory
        addSlotBox(playerInventory, 9, leftCol, topRow, 9, 18, 3, 18);

        // Hotbar
        topRow += 58;
        addSlotRange(playerInventory, 0, leftCol, topRow, 9, 18);
    }

}
