package bedrockium.event;

import bedrockium.items.BedrockiumPick;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EventHandler {

    @SubscribeEvent
    public static void onBlockBreakEvent(final BlockEvent.BreakEvent event) {
        BedrockiumPick.onBlockBreak(event);
    }

}
