package bedrockium.energy;


import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nullable;

public class EnergyContainerItemWrapper implements ICapabilityProvider {

	final ItemStack stack;
	final IEnergyContainerItem container;

	final boolean canExtract;
	final boolean canReceive;

	private LazyOptional<IEnergyStorage> energyCap = LazyOptional.of(this::createEnergy);

	public EnergyContainerItemWrapper(ItemStack stackIn, IEnergyContainerItem containerIn, boolean extractIn, boolean receiveIn) {

		this.stack = stackIn;
		this.container = containerIn;

		this.canExtract = extractIn;
		this.canReceive = receiveIn;
	}

	private IEnergyStorage createEnergy() {
		return new IEnergyStorage() {

			@Override
			public int receiveEnergy(int maxReceive, boolean simulate) {

				return container.receiveEnergy(stack, maxReceive, simulate);
			}

			@Override
			public int extractEnergy(int maxExtract, boolean simulate) {

				return container.extractEnergy(stack, maxExtract, simulate);
			}

			@Override
			public int getEnergyStored() {

				return container.getEnergyStored(stack);
			}

			@Override
			public int getMaxEnergyStored() {

				return container.getMaxEnergyStored(stack);
			}

			@Override
			public boolean canExtract() {

				return canExtract;
			}

			@Override
			public boolean canReceive() {

				return canReceive;
			}
		};
	}

	public EnergyContainerItemWrapper(ItemStack stackIn, IEnergyContainerItem containerIn) {

		this(stackIn, containerIn, true, true);
	}

	/* ICapabilityProvider */

	@SuppressWarnings("unchecked")
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
		if (cap == CapabilityEnergy.ENERGY) {
			return energyCap.cast();
		}
		return LazyOptional.empty();
	}

}
