package bedrockium.items;

import bedrockium.config.Config;
import bedrockium.init.ModItems;
import net.minecraft.block.CraftingTableBlock;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.PickaxeItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import javax.annotation.Nullable;
import java.util.List;

public class BedrockiumPick extends PickaxeItem {

    public BedrockiumPick(Properties properties) {
        super(ModItems.bedrockiumTier, 0, -2.0F, properties);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        if (Config.CLIENT.displayInformationOnTools.get()) {
            tooltip.add(new StringTextComponent("Right-click to switch between enchantments\nMining gives the user haste\nYou can choose not to show this information in the config, \nor by sneak+right-clicking on a workbench with any bedrockium tool"));
        } else {
            return;
        }
    }

    @Override
    public boolean hasEffect(ItemStack stack) {
        return true;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return false;
    }

    @Override
    public void onCreated(ItemStack stack, World worldIn, PlayerEntity playerIn) {
        stack.addEnchantment(Enchantments.EFFICIENCY, 5);
        stack.addEnchantment(Enchantments.FORTUNE, 3);
        stack.getTag().putInt("pick", 0);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        if (!worldIn.isRemote) {
            if (playerIn.isSneaking()) {
                return super.onItemRightClick(worldIn, playerIn, handIn);
            }
            ItemStack stack = playerIn.getHeldItemMainhand();
            if (stack.hasTag()) {
                int i = stack.getTag().getInt("pick");
                if (i == 0) {
                    stack.setTag(new CompoundNBT());
                    stack.addEnchantment(Enchantments.EFFICIENCY, 5);
                    stack.addEnchantment(Enchantments.SILK_TOUCH, 1);
                    stack.getTag().putInt("pick", 1);
                    playerIn.sendMessage(new StringTextComponent("Enchanted: Silk Touch"));
                } else {
                    stack.setTag(new CompoundNBT());
                    onCreated(stack, worldIn, playerIn);
                    playerIn.sendMessage(new StringTextComponent("Enchanted: Fortune III"));
                }
            }
        }
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    @SubscribeEvent
    public static void onBlockBreak(final BlockEvent.BreakEvent event) {
        PlayerEntity player = event.getPlayer();
        if (player.getHeldItemMainhand().getItem() == ModItems.pick ||
                player.getHeldItemMainhand().getItem() == ModItems.spade ||
                player.getHeldItemMainhand().getItem() == ModItems.axe) {
            player.addPotionEffect(new EffectInstance(Effects.HASTE, 600, 2));
        } else {
            return;
        }
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        if (context.getPlayer().isSneaking()
                && context.getWorld().getBlockState(context.getPos()).getBlock() instanceof CraftingTableBlock
                && context.getPlayer().getHeldItemMainhand().getItem() instanceof BedrockiumPick) {
            if (context.getWorld().isRemote) {
                if (Config.CLIENT.displayInformationOnTools.get()) {
                    Config.CLIENT.displayInformationOnTools.set(false);
                    return ActionResultType.PASS;
                } else {
                    Config.CLIENT.displayInformationOnTools.set(true);
                    return ActionResultType.PASS;
                }
            }
        }
        return super.onItemUse(context);

    }

}
