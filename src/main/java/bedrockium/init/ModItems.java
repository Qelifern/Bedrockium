package bedrockium.init;

import bedrockium.items.BedrockiumAxe;
import bedrockium.items.BedrockiumPick;
import bedrockium.items.BedrockiumSpade;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

public class ModItems {

    @ObjectHolder("bedrockium:bedrockium")
    public static Item bedrockium;

    @ObjectHolder("bedrockium:stick")
    public static Item stick;

    @ObjectHolder("bedrockium:pick")
    public static BedrockiumPick pick;

    @ObjectHolder("bedrockium:spade")
    public static BedrockiumSpade spade;

    @ObjectHolder("bedrockium:axe")
    public static BedrockiumAxe axe;

    public static void register(IForgeRegistry<Item> registry) {
        registry.register(new Item(new Item.Properties().group(ItemGroup.MISC)).setRegistryName("bedrockium"));
        registry.register(new Item(new Item.Properties().group(ItemGroup.MISC)).setRegistryName("stick"));
        registry.register(new BedrockiumPick(new Item.Properties().maxStackSize(1).group(ItemGroup.TOOLS)).setRegistryName("pick"));
        registry.register(new BedrockiumSpade(new Item.Properties().maxStackSize(1).group(ItemGroup.TOOLS)).setRegistryName("spade"));
        registry.register(new BedrockiumAxe(new Item.Properties().maxStackSize(1).group(ItemGroup.TOOLS)).setRegistryName("axe"));
    }

    public static final IItemTier bedrockiumTier = new IItemTier() {
        @Override
        public int getMaxUses() {
            return -1;
        }

        @Override
        public float getEfficiency() {
            return 14.0F;
        }

        @Override
        public float getAttackDamage() {
            return 3.0F;
        }

        @Override
        public int getHarvestLevel() {
            return 4;
        }

        @Override
        public int getEnchantability() {
            return 0;
        }

        @Override
        public Ingredient getRepairMaterial() {
            return Ingredient.fromItems(ModItems.bedrockium);
        }
    };

}