package bedrockium.init;

import bedrockium.Main;
import bedrockium.blocks.BlockBedrockiumMiner;
import bedrockium.items.*;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

public class ModItems {

    @ObjectHolder("bedrockium:bedrockium")
    public static Item bedrockium;

    @ObjectHolder("bedrockium:stick")
    public static Item stick;

    @ObjectHolder("bedrockium:drill_head")
    public static Item drill_head;

    @ObjectHolder("bedrockium:pick")
    public static BedrockiumPick pick;

    @ObjectHolder("bedrockium:spade")
    public static BedrockiumSpade spade;

    @ObjectHolder("bedrockium:axe")
    public static BedrockiumAxe axe;

    public static void register(IForgeRegistry<Item> registry) {

        registry.register(new BlockItemEnergy(ModBlocks.miner, new Item.Properties().group(Main.itemGroup)).setRegistryName(BlockBedrockiumMiner.MINER));
        registry.register(new BedrockiumPick(new Item.Properties().maxStackSize(1).group(Main.itemGroup)).setRegistryName("pick"));
        registry.register(new BedrockiumSpade(new Item.Properties().maxStackSize(1).group(Main.itemGroup)).setRegistryName("spade"));
        registry.register(new BedrockiumAxe(new Item.Properties().maxStackSize(1).group(Main.itemGroup)).setRegistryName("axe"));

        registry.register(new Item(new Item.Properties().group(Main.itemGroup)).setRegistryName("bedrockium"));
        registry.register(new Item(new Item.Properties().group(Main.itemGroup)).setRegistryName("stick"));
        registry.register(new DrillHead(new Item.Properties().group(Main.itemGroup).maxStackSize(1).defaultMaxDamage(1000)).setRegistryName("drill_head"));
        //registry.register(new TestBattery(new Item.Properties().group(ItemGroup.MISC).maxStackSize(1)).setRegistryName("battery"));


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
