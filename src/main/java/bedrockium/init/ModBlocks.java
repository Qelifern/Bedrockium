package bedrockium.init;

import bedrockium.Main;
import bedrockium.blocks.BlockBedrockiumMiner;
import bedrockium.container.ContainerBedrockiumMiner;
import bedrockium.tileentity.TileEntityBedrockiumMiner;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

public class ModBlocks {

    @ObjectHolder("bedrockium:miner")
    public static BlockBedrockiumMiner miner;

    @ObjectHolder("bedrockium:miner")
    public static TileEntityType<TileEntityBedrockiumMiner> MINER_TYPE;

    @ObjectHolder("bedrockium:miner")
    public static ContainerType<ContainerBedrockiumMiner> MINER_CONTAINER;

    public static void register(IForgeRegistry<Block> registry) {

        registry.register(new BlockBedrockiumMiner(Block.Properties.create(Material.IRON).hardnessAndResistance(2.4F).sound(SoundType.METAL)));

    }

    public static void registerTiles(IForgeRegistry<TileEntityType<?>> registry) {

        registry.register(TileEntityType.Builder.create(TileEntityBedrockiumMiner::new, ModBlocks.miner).build(null).setRegistryName("miner"));

    }

    public static void registerContainers(IForgeRegistry<ContainerType<?>> registry) {
        registry.register(IForgeContainerType.create((windowId, inv, data) -> {
            BlockPos pos = data.readBlockPos();
            return new ContainerBedrockiumMiner(windowId, Main.proxy.getClientWorld(), pos, inv, Main.proxy.getClientPlayer());
        }).setRegistryName("miner"));
    }


}
