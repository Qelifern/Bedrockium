package bedrockium.energy;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

public class ItemEnergyContainer extends Item implements IEnergyContainerItem {

	protected int capacity;
	protected int maxReceive;
	protected int maxExtract;

	public ItemEnergyContainer() {
		super(new Properties());
	}

	public ItemEnergyContainer(Properties properties, int capacity) {

		this(properties, capacity, capacity, capacity);
	}

	public ItemEnergyContainer(Properties properties, int capacity, int maxTransfer) {

		this(properties, capacity, maxTransfer, maxTransfer);
	}

	public ItemEnergyContainer(Properties properties, int capacity, int maxReceive, int maxExtract) {
		super(properties);
		this.capacity = capacity;
		this.maxReceive = maxReceive;
		this.maxExtract = maxExtract;
	}

	public ItemEnergyContainer setCapacity(int capacity) {

		this.capacity = capacity;
		return this;
	}

	public ItemEnergyContainer setMaxTransfer(int maxTransfer) {

		setMaxReceive(maxTransfer);
		setMaxExtract(maxTransfer);
		return this;
	}

	public ItemEnergyContainer setMaxReceive(int maxReceive) {

		this.maxReceive = maxReceive;
		return this;
	}

	public ItemEnergyContainer setMaxExtract(int maxExtract) {

		this.maxExtract = maxExtract;
		return this;
	}

	/* IEnergyContainerItem */
	@Override
	public int receiveEnergy(ItemStack container, int maxReceive, boolean simulate) {

		if (!container.hasTag()) {
			container.setTag(new CompoundNBT());
		}
		int stored = Math.min(container.getTag().getInt("Energy"), getMaxEnergyStored(container));
		int energyReceived = Math.min(capacity - stored, Math.min(this.maxReceive, maxReceive));

		if (!simulate) {
			stored += energyReceived;
			container.getTag().putInt("Energy", stored);
		}
		return energyReceived;
	}

	@Override
	public int extractEnergy(ItemStack container, int maxExtract, boolean simulate) {

		if (container.getTag() == null || !container.getTag().contains("Energy")) {
			return 0;
		}
		int stored = Math.min(container.getTag().getInt("Energy"), getMaxEnergyStored(container));
		int energyExtracted = Math.min(stored, Math.min(this.maxExtract, maxExtract));

		if (!simulate) {
			stored -= energyExtracted;
			container.getTag().putInt("Energy", stored);
		}
		return energyExtracted;
	}

	@Override
	public int getEnergyStored(ItemStack container) {

		if (container.getTag() == null || !container.getTag().contains("Energy")) {
			return 0;
		}
		return Math.min(container.getTag().getInt("Energy"), getMaxEnergyStored(container));
	}

	@Override
	public int getMaxEnergyStored(ItemStack container) {

		return capacity;
	}

}
