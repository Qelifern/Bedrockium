package bedrockium.items;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class DrillHead extends Item {

    public DrillHead(Properties properties) {
        super(properties);
    }

    @Override
    public int getMaxDamage(ItemStack stack) {
        return 1000;
    }

    @Override
    public void onCreated(ItemStack stack, World worldIn, PlayerEntity playerIn) {
        stack.damageItem(1, playerIn, playerEntity -> {
            playerEntity.sendBreakAnimation(playerIn.getActiveHand());
        });
    }
}
