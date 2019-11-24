package bedrockium.config;

import bedrockium.Main;
import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.commons.lang3.tuple.Pair;

import java.nio.file.Path;

import static net.minecraftforge.fml.Logging.CORE;

@Mod.EventBusSubscriber
public class Config {
    public static final ForgeConfigSpec clientSpec;
    public static final Config.Client CLIENT;
    public static final ForgeConfigSpec serverSpec;
    public static final Config.Server SERVER;

    static {
        final Pair<Server, ForgeConfigSpec> serverSpecPair = new ForgeConfigSpec.Builder().configure(Config.Server::new);
        serverSpec = serverSpecPair.getRight();
        SERVER = serverSpecPair.getLeft();

        final Pair<Client, ForgeConfigSpec> clientSpecPair = new ForgeConfigSpec.Builder().configure(Config.Client::new);
        clientSpec = clientSpecPair.getRight();
        CLIENT = clientSpecPair.getLeft();
    }

    public static void loadConfig(ForgeConfigSpec spec, Path path) {
        Main.LOGGER.debug("Loading config file {}", path);

        final CommentedFileConfig configData = CommentedFileConfig.builder(path)
                .sync()
                .autosave()
                .writingMode(WritingMode.REPLACE)
                .build();

        Main.LOGGER.debug("Built TOML config for {}", path.toString());
        configData.load();
        Main.LOGGER.debug("Loaded TOML config file {}", path.toString());
        spec.setConfig(configData);
    }

    public static class Server {
        public final ForgeConfigSpec.IntValue minerMaxProgress;
        public final ForgeConfigSpec.IntValue minerPowerUsage;

        Server(ForgeConfigSpec.Builder builder) {
            builder.comment("Server configuration settings")
                    .push("server");
            minerMaxProgress = builder
                    .comment(" Lower value means faster mining (ticks). Default: 12000. Range: 2-72000 (MUST BE WITHIN RANGE)").defineInRange("miner_max_progress", 12000, 2, 72000);

            minerPowerUsage = builder
                    .comment(" Amount of power (FE) to use while mining. Default: 80. Range: 0-1000000 (MUST BE WITHIN RANGE)").defineInRange("miner_power_usage", 80, 0, 1000000);

            builder.pop();
        }
    }

    public static class Client {
        public final ForgeConfigSpec.BooleanValue displayInformationOnTools;

        Client(ForgeConfigSpec.Builder builder) {
            builder.comment("Client only settings")
                    .push("client");
            displayInformationOnTools = builder
                    .comment(" Choose to display the information. Default: true.").define("display_info", true);


            builder.pop();
        }
    }

    public static void loadConfig() {
        Config.loadConfig(Config.clientSpec, FMLPaths.CONFIGDIR.get().resolve("bedrockium-client.toml"));
        Config.loadConfig(Config.serverSpec, FMLPaths.CONFIGDIR.get().resolve("bedrockium.toml"));
    }

    @SubscribeEvent
    public static void onLoad(final ModConfig.Loading configEvent) {
        loadConfig();
        Main.LOGGER.debug("Loaded {} config file {}", Main.MOD_ID, configEvent.getConfig().getFileName());
    }

    @SubscribeEvent
    public static void onFileChange(final ModConfig.ConfigReloading configEvent) {
        loadConfig();
        Main.LOGGER.fatal(CORE, "{} config just got changed on the file system!", Main.MOD_ID);
    }

}
