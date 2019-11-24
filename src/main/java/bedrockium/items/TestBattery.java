package bedrockium.items;

import bedrockium.energy.EnergyContainerItemWrapper;
import bedrockium.energy.ItemEnergyContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public class TestBattery extends ItemEnergyContainer {

    public TestBattery(Properties properties) {
        super(properties, 10000, 1000, 1000);
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

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        if (!worldIn.isRemote) {
            ItemStack stack = playerIn.getHeldItemMainhand();
            if (stack.getItem() instanceof ItemEnergyContainer) {
                ((ItemEnergyContainer)stack.getItem()).receiveEnergy(stack, 10000, false);
            }
        }
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

}
