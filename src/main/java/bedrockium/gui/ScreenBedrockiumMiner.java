package bedrockium.gui;

import bedrockium.Main;
import bedrockium.container.ContainerBedrockiumMiner;
import bedrockium.util.StringHelper;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ScreenBedrockiumMiner extends ContainerScreen<ContainerBedrockiumMiner> {

    public ResourceLocation GUI = new ResourceLocation(Main.MOD_ID + ":" +"textures/gui/miner.png");
    PlayerInventory playerInv;
    ITextComponent name;

    public ScreenBedrockiumMiner(ContainerBedrockiumMiner t, PlayerInventory inv, ITextComponent name) {
        super(t, inv, name);
        playerInv = inv;
        this.name = name;
        this.ySize = 174;
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        this.renderBackground();
        super.render(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        //drawString(Minecraft.getInstance().fontRenderer, "Energy: " + container.getEnergy(), 10, 10, 0xffffff);
        this.minecraft.fontRenderer.drawString(this.playerInv.getDisplayName().getUnformattedComponentText(), 7, this.ySize - 93, 4210752);
        this.minecraft.fontRenderer.drawString(name.getUnformattedComponentText(), this.xSize / 2 - this.minecraft.fontRenderer.getStringWidth(name.getUnformattedComponentText()) / 2, 6, 4210752);

        int actualMouseX = mouseX - ((this.width - this.xSize) / 2);
        int actualMouseY = mouseY - ((this.height - this.ySize) / 2);
        if(actualMouseX >= 10 && actualMouseX <= 22 && actualMouseY >= 12 && actualMouseY <= 58) {
            int energy = ((ContainerBedrockiumMiner)this.container).getEnergy();
            int capacity = ((ContainerBedrockiumMiner)this.container).getCapacity();
            this.renderTooltip(StringHelper.displayEnergy(energy, capacity), actualMouseX, actualMouseY);
        }

        if(actualMouseX >= 78 && actualMouseX <= 102 && actualMouseY >= 38 && actualMouseY <= 50) {
            this.renderTooltip(((ContainerBedrockiumMiner)this.container).getProgress(), actualMouseX, actualMouseY);
        }

    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(GUI);
        int relX = (this.width - this.xSize) / 2;
        int relY = (this.height - this.ySize) / 2;
        this.blit(relX, relY, 0, 0, this.xSize, this.ySize);

        int i;

        i = ((ContainerBedrockiumMiner)this.container).getPowerScaled(46);
        this.blit(guiLeft + 10, guiTop + 58 - i, 176, 63 - i, 12, i);

        i = ((ContainerBedrockiumMiner)this.container).getCookScaled(24);
        this.blit(guiLeft + 78, guiTop + 32, 176, 0, i + 1, 17);

    }



}
