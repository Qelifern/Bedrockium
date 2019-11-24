package bedrockium.energy;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public class ItemEnergyContainerCap extends ItemEnergyContainer {

	/* CAPABILITIES */
	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, CompoundNBT nbt) {

		return new EnergyContainerItemWrapper(stack, this);
	}

}
