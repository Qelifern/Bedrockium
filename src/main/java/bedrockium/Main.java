package bedrockium;

import bedrockium.config.Config;
import bedrockium.init.ModItems;
import bedrockium.proxy.ClientProxy;
import bedrockium.proxy.IProxy;
import bedrockium.proxy.ServerProxy;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Main.MOD_ID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class Main
{
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "bedrockium";

    public static IProxy proxy = DistExecutor.runForDist(() -> () -> new ClientProxy(), () -> () -> new ServerProxy());

    public Main() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);

        MinecraftForge.EVENT_BUS.register(this);

        Config.loadConfig();

    }

    @SubscribeEvent
    public static void config(ConfigChangedEvent.OnConfigChangedEvent event) {
        Config.loadConfig();
    }

    @SubscribeEvent
    public static void config(ModConfig.ConfigReloading event) {
        Config.loadConfig();
    }

    @SubscribeEvent
    public static void config(ModConfig.ModConfigEvent event) {
        Config.loadConfig();
    }

    private void setup(final FMLCommonSetupEvent event)
    {
        proxy.setup(event);
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        ModItems.register(event.getRegistry());
    }

}