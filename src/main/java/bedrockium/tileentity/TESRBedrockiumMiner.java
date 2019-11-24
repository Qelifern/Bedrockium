package bedrockium.tileentity;

import bedrockium.Main;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;

public class TESRBedrockiumMiner extends TileEntityRenderer<TileEntityBedrockiumMiner> {

    private static final ResourceLocation texture = new ResourceLocation(Main.MOD_ID, "textures/blocks/miner_2.png");

    float pixel = 1F / 16F;
    float texPixel = 1F / 32F;

    float angle;

    @Override
    public void render(TileEntityBedrockiumMiner te, double x, double y, double z, float partialTicks, int destroyStage) {
        GlStateManager.pushMatrix();
        GlStateManager.disableLighting();
        GlStateManager.translated(x, y, z);
        Minecraft.getInstance().getTextureManager().bindTexture(texture);

        if (te.hasDrill()) {
            if (te.isMining()) {
                GlStateManager.translated(16 * pixel / 2, 0, 16 * pixel / 2);
                GlStateManager.rotatef(angle += 0.6F, 0.0F, 1.0F, 0.0F);
                GlStateManager.translated(-16 * pixel / 2, 0, -16 * pixel / 2);
            }
            GlStateManager.translated(0, 2 * pixel / 2, 0);
            drawCore(te, 10);
            GlStateManager.translated(0, -10 * pixel / 2, 0);
            drawCore(te, 12);
            GlStateManager.translated(0, -6 * pixel / 2, 0);
            drawCore(te, 14);
        }

        GlStateManager.enableLighting();
        GlStateManager.popMatrix();
    }

    private void drawCore(TileEntityBedrockiumMiner te, int i) {
        GlStateManager.pushMatrix();
        BufferBuilder buffer = Tessellator.getInstance().getBuffer();
        buffer.begin(7, DefaultVertexFormats.POSITION_TEX);


        //-Z NORTH
        buffer.pos(1 - i * pixel / 2, i * pixel / 2, 1 - i * pixel / 2).tex(6 * texPixel, 6 * texPixel).endVertex();
        buffer.pos(1 - i * pixel / 2, 1 - i * pixel / 2, 1 - i * pixel / 2).tex(6 * texPixel, 0 * texPixel).endVertex();
        buffer.pos(i * pixel / 2, 1 - i * pixel / 2, 1 - i * pixel / 2).tex(0 * texPixel, 0 * texPixel).endVertex();
        buffer.pos(i * pixel / 2, i * pixel / 2, 1 - i * pixel / 2).tex(0 * texPixel, 6 * texPixel).endVertex();

        //+Z SOUTH
        buffer.pos(i * pixel / 2, i * pixel / 2, i * pixel / 2).tex(6 * texPixel, 6 * texPixel).endVertex();
        buffer.pos(i * pixel / 2, 1 - i * pixel / 2, i * pixel / 2).tex(6 * texPixel, 0 * texPixel).endVertex();
        buffer.pos(1 - i * pixel / 2, 1 - i * pixel / 2, i * pixel / 2).tex(0 * texPixel, 0 * texPixel).endVertex();
        buffer.pos(1 - i * pixel / 2, i * pixel / 2, i * pixel / 2).tex(0 * texPixel, 6 * texPixel).endVertex();


        //-X WEST
        buffer.pos(1 - i * pixel / 2, i * pixel / 2, i * pixel / 2).tex(6 * texPixel, 6 * texPixel).endVertex();
        buffer.pos(1 - i * pixel / 2, 1 - i * pixel / 2, i * pixel / 2).tex(6 * texPixel, 0 * texPixel).endVertex();
        buffer.pos(1 - i * pixel / 2, 1 - i * pixel / 2, 1 - i * pixel / 2).tex(0 * texPixel, 0 * texPixel).endVertex();
        buffer.pos(1 - i * pixel / 2, i * pixel / 2, 1 - i * pixel / 2).tex(0 * texPixel, 6 * texPixel).endVertex();

        //+X EAST
        buffer.pos(i * pixel / 2, i * pixel / 2, 1 - i * pixel / 2).tex(6 * texPixel, 6 * texPixel).endVertex();
        buffer.pos(i * pixel / 2, 1 - i * pixel / 2, 1 - i * pixel / 2).tex(6 * texPixel, 0 * texPixel).endVertex();
        buffer.pos(i * pixel / 2, 1 - i * pixel / 2, i * pixel / 2).tex(0 * texPixel, 0 * texPixel).endVertex();
        buffer.pos(i * pixel / 2, i * pixel / 2, i * pixel / 2).tex(0 * texPixel, 6 * texPixel).endVertex();

        //-Y TOP
        buffer.pos(1 - i * pixel / 2, 1 - i * pixel / 2, 1 - i * pixel / 2).tex(6 * texPixel, 6 * texPixel).endVertex();
        buffer.pos(1 - i * pixel / 2, 1 - i * pixel / 2, i * pixel / 2).tex(6 * texPixel, 0 * texPixel).endVertex();
        buffer.pos(i * pixel / 2, 1 - i * pixel / 2, i * pixel / 2).tex(0 * texPixel, 0 * texPixel).endVertex();
        buffer.pos(i * pixel / 2, 1 - i * pixel / 2, 1 - i * pixel / 2).tex(0 * texPixel, 6 * texPixel).endVertex();

        //+Y BOTTOM
        buffer.pos(i * pixel / 2, i * pixel / 2, 1 - i * pixel / 2).tex(6 * texPixel, 6 * texPixel).endVertex();
        buffer.pos(i * pixel / 2, i * pixel / 2, i * pixel / 2).tex(6 * texPixel, 0 * texPixel).endVertex();
        buffer.pos(1 - i * pixel / 2, i * pixel / 2, i * pixel / 2).tex(0 * texPixel, 0 * texPixel).endVertex();
        buffer.pos(1 - i * pixel / 2, i * pixel / 2, 1 - i * pixel / 2).tex(0 * texPixel, 6 * texPixel).endVertex();


        Tessellator.getInstance().draw();
        GlStateManager.popMatrix();
    }

}
