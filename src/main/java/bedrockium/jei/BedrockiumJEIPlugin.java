package bedrockium.jei;


import bedrockium.Main;
import bedrockium.init.ModBlocks;
import bedrockium.jei.categories.MinerRecipeCategory;
import bedrockium.jei.makers.MinerRecipeMaker;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

@JeiPlugin
public class BedrockiumJEIPlugin implements IModPlugin {

    public static final ResourceLocation MINER = new ResourceLocation(Main.MOD_ID, "miner");
    public static final ResourceLocation GUI_MINER = new ResourceLocation(Main.MOD_ID, "textures/gui/miner_jei.png");


    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(Main.MOD_ID, "plugin_bedrockium");
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registry) {
        registry.addRecipeCatalyst(new ItemStack(ModBlocks.miner), MINER);
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        final IJeiHelpers jeiHelpers = registry.getJeiHelpers();
        final IGuiHelper guiHelper = jeiHelpers.getGuiHelper();
        registry.addRecipeCategories(new MinerRecipeCategory(guiHelper));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registry) {
        IJeiHelpers jeiHelpers = registry.getJeiHelpers();

        registry.addRecipes(MinerRecipeMaker.getMinerRecipes(jeiHelpers), MINER);

    }

}
