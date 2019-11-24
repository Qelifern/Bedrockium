package bedrockium.tileentity;

import bedrockium.config.Config;
import bedrockium.container.ContainerBedrockiumMiner;
import bedrockium.energy.BedrockiumEnergyStorage;
import bedrockium.init.ModBlocks;
import bedrockium.init.ModItems;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.IIntArray;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nullable;
import java.util.Random;

public class TileEntityBedrockiumMiner extends TileEntityInventory implements ITickableTileEntity {

    private static final int[] SLOTS_UP = new int[]{3};
    private static final int[] SLOTS_DOWN = new int[]{2};
    private static final int[] SLOTS_HORIZONTAL = new int[]{1};

    private int maxProgress = Config.SERVER.minerMaxProgress.get(); //10 minutes default
    private int progress;
    private int powerUsage = Config.SERVER.minerPowerUsage.get(); //per tick
    private int timer;

    private Random rand = new Random();

    private LazyOptional<IEnergyStorage> energy = LazyOptional.of(this::createEnergy);

    public TileEntityBedrockiumMiner() {
        super(ModBlocks.MINER_TYPE, 4);
    }

    private IEnergyStorage createEnergy() {
        return new BedrockiumEnergyStorage(1000000, 1000000, 0);
    }

    @OnlyIn(Dist.CLIENT)
    public boolean isMining() {
        return this.progress > 0 && this.getEnergy() >= this.powerUsage;
    }

    @OnlyIn(Dist.CLIENT)
    public boolean hasDrill() {
        if (!this.getStackInSlot(0).isEmpty() && this.getStackInSlot(0).getDamage() < 999) {
            return true;
        }
        return false;
    }

    @Override
    public void tick() {
        if (!world.isRemote) {
            timer++;
            if (maxProgress != Config.SERVER.minerMaxProgress.get()) {
                maxProgress = Config.SERVER.minerMaxProgress.get();
            }
            if (powerUsage != Config.SERVER.minerPowerUsage.get()) {
                powerUsage = Config.SERVER.minerPowerUsage.get();
            }
            if (world.getBlockState(pos.down()).getBlock() == Blocks.BEDROCK) {
                if (!this.getStackInSlot(0).isEmpty() && this.getStackInSlot(0).getDamage() > 0 && this.getStackInSlot(0).getDamage() <= 999) {
                    if (getEnergy() >= this.powerUsage && this.getStackInSlot(2).isEmpty() || (!this.getStackInSlot(2).isEmpty() && this.getStackInSlot(2).getCount() < 64)) {
                        this.progress++;
                        this.getCapability(CapabilityEnergy.ENERGY).ifPresent(h -> {
                            ((BedrockiumEnergyStorage) h).setEnergy(h.getEnergyStored() - this.powerUsage);
                        });
                        if (this.progress >= this.maxProgress) {
                            if (this.getStackInSlot(2).isEmpty()) {
                                this.setInventorySlotContents(2, new ItemStack(ModItems.bedrockium, rand.nextInt(2) + 1));
                            } else {
                                this.getStackInSlot(2).grow(rand.nextInt(2) + 1);
                            }
                            this.progress = 0;
                            this.markDirty();
                        }
                    }
                }
            }
            if (this.timer % 10 == 0) {
                if (world.getBlockState(pos.down()).getBlock() == Blocks.BEDROCK) {
                    if (this.getEnergy() >= this.powerUsage && this.getStackInSlot(2).isEmpty() || (!this.getStackInSlot(2).isEmpty() && this.getStackInSlot(2).getCount() < 64)) {
                        if (!this.getStackInSlot(0).isEmpty()) {
                            if (this.getStackInSlot(0).getDamage() <= 999) {
                                if (this.getStackInSlot(0).getDamage() >= 1)
                                    this.getStackInSlot(0).setDamage(this.getStackInSlot(0).getDamage() + 1);
                            }
                            if (this.getStackInSlot(0).getDamage() <= 1000) {
                                if (this.getStackInSlot(0).getDamage() > 200) {
                                    if (!this.getStackInSlot(3).isEmpty()) {
                                        this.getStackInSlot(0).setDamage(this.getStackInSlot(0).getDamage() - 200);
                                        this.getStackInSlot(3).shrink(1);
                                    }
                                }
                            }
                        }
                    }
                }
                timer = 0;
            }
            ItemStack chargeSlot = this.getStackInSlot(1);
            if (!chargeSlot.isEmpty()) {
                chargeSlot.getCapability(CapabilityEnergy.ENERGY).ifPresent(h -> {
                    if (h.extractEnergy(Math.min(h.getEnergyStored(),
                            this.getCapacity() - this.getEnergy()), true) > 0) {
                        this.getCapability(CapabilityEnergy.ENERGY).ifPresent(h2 -> {
                            ((BedrockiumEnergyStorage) h2).setEnergy(h2.getEnergyStored() + h.extractEnergy(Math.min(h.getEnergyStored(),
                                    this.getCapacity() - this.getEnergy()), false));
                        });
                    }
                });
            }
        }
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        compound.putInt("Progress", this.progress);
        this.getCapability(CapabilityEnergy.ENERGY).ifPresent(h -> {
            ((BedrockiumEnergyStorage) h).writeToNBT(compound);
        });
        return super.write(compound);
    }

    @Override
    public void read(CompoundNBT compound) {
        this.progress = compound.getInt("Progress");
        this.getCapability(CapabilityEnergy.ENERGY).ifPresent(h -> {
            ((BedrockiumEnergyStorage) h).readFromNBT(compound);
        });
        super.read(compound);
    }

    public int getEnergy() {
        return this.getCapability(CapabilityEnergy.ENERGY).map(h -> h.getEnergyStored()).orElse(0);
    }

    public int getCapacity() {
        return this.getCapability(CapabilityEnergy.ENERGY).map(h -> h.getMaxEnergyStored()).orElse(0);
    }

    public final IIntArray fields = new IIntArray() {
        public int get(int index) {
            switch (index) {
                case 0:
                    return progress;
                case 1:
                    return maxProgress;
                default:
                    return 0;
            }
        }

        public void set(int index, int value) {
            switch (index) {
                case 0:
                    progress = value;
                    break;
                case 1:
                    maxProgress = value;
            }

        }

        public int size() {
            return 2;
        }
    };

    @Override
    public int[] IgetSlotsForFace(Direction side) {
        if (side == Direction.DOWN) {
            return SLOTS_DOWN;
        } else {
            return side == Direction.UP ? SLOTS_UP : SLOTS_HORIZONTAL;
        }
    }

    @Override
    public boolean IcanExtractItem(int index, ItemStack stack, Direction direction) {
        return index == 2;
    }

    @Override
    public String IgetName() {
        return "container.miner";
    }

    @Override
    public boolean IisItemValidForSlot(int index, ItemStack stack) {
        if (index == 0) {
            return stack.getItem() == ModItems.drill_head;
        }
        if (index == 1) {
            return stack.getCapability(CapabilityEnergy.ENERGY).isPresent();
        }
        if (index == 3) {
            return stack.getItem() == Items.DIAMOND;
        }
        return false;
    }

    @Override
    public Container IcreateMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        return new ContainerBedrockiumMiner(i, world, pos, playerInventory, playerEntity, this.fields);
    }


    net.minecraftforge.common.util.LazyOptional<? extends net.minecraftforge.items.IItemHandler>[] handlers =
            net.minecraftforge.items.wrapper.SidedInvWrapper.create(this, Direction.UP, Direction.DOWN, Direction.NORTH);

    @Override
    public <T> net.minecraftforge.common.util.LazyOptional<T> getCapability(net.minecraftforge.common.capabilities.Capability<T> capability, @Nullable Direction facing) {
        if (!this.removed && facing != null && capability == net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            if (facing == Direction.UP)
                return handlers[0].cast();
            else if (facing == Direction.DOWN)
                return handlers[1].cast();
            else
                return handlers[2].cast();
        }
        if (!this.removed && capability == CapabilityEnergy.ENERGY) {
            return energy.cast();
        }
        return super.getCapability(capability, facing);
    }



}
