package bedrockium.items;

import bedrockium.energy.BlockItemEnergyContainer;
import bedrockium.energy.EnergyContainerItemWrapper;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public class BlockItemEnergy extends BlockItemEnergyContainer {

    public BlockItemEnergy(Block block, Properties properties) {
        super(block, properties, 1000000, 1000000, 0);
    }


    /* CAPABILITIES */
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, CompoundNBT nbt) {

        return new EnergyContainerItemWrapper(stack, this);
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return stack.hasTag();
    }

    @Override
    public int getRGBDurabilityForDisplay(ItemStack stack) {
        return MathHelper.rgb(1.0F, 0.0F, 0.0F);
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        if (stack.hasTag())  {
            double energyDiff = capacity - stack.getTag().getInt("Energy");
            return energyDiff / capacity;
        }
        return 0;
    }
}
