package bedrockium.proxy;

import bedrockium.Main;
import bedrockium.gui.ScreenBedrockiumMiner;
import bedrockium.init.ModBlocks;
import bedrockium.tileentity.TESRBedrockiumMiner;
import bedrockium.tileentity.TileEntityBedrockiumMiner;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(Dist.CLIENT)
public class ClientProxy implements IProxy {

	@Override
	public void setup(FMLCommonSetupEvent event) {
		OBJLoader.INSTANCE.addDomain(Main.MOD_ID);
		ScreenManager.registerFactory(ModBlocks.MINER_CONTAINER, ScreenBedrockiumMiner::new);
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityBedrockiumMiner.class, new TESRBedrockiumMiner());

	}

	@Override
	public PlayerEntity getClientPlayer() {
		return Minecraft.getInstance().player;
	}

	@Override
	public World getClientWorld() {
		return Minecraft.getInstance().world;
	}

	@Override
	public World getServerWorld(int dim) {
        throw new IllegalStateException("Can't call this client-side!");
	}



}
