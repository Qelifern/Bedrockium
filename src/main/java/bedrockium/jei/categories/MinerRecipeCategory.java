package bedrockium.jei.categories;


import bedrockium.init.ModBlocks;
import bedrockium.jei.BedrockiumJEIPlugin;
import bedrockium.jei.recipes.MinerRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class MinerRecipeCategory extends MinerVariantCategory<MinerRecipe> {

	protected IDrawable background;
	protected IDrawable icon;
	protected String localizedName;
	protected final IDrawableAnimated arrow;

	public MinerRecipeCategory(IGuiHelper guiHelper) {
		super(guiHelper);
		this.background = guiHelper.createDrawable(BedrockiumJEIPlugin.GUI_MINER, 0, 0, 130, 68);
		this.icon = guiHelper.createDrawableIngredient(new ItemStack(ModBlocks.miner));
		this.localizedName = I18n.format("miner_recipes");
		this.arrow = guiHelper.drawableBuilder(BedrockiumJEIPlugin.GUI_MINER, 82, 128, 24, 17).buildAnimated(200, IDrawableAnimated.StartDirection.LEFT, false);
	}

	@Override
	public ResourceLocation getUid() {
		return BedrockiumJEIPlugin.MINER;
	}

	@Override
	public Class getRecipeClass() {
		return MinerRecipe.class;
	}

	@Nonnull
	@Override
	public String getTitle() {
		return localizedName;
	}

	@Nonnull
	@Override
	public IDrawable getBackground() {
		return background;
	}

	@Nonnull
	@Override
	public IDrawable getIcon() {
		return icon;
	}

	@Override
	public void setIngredients(MinerRecipe recipe, IIngredients ingredients) {
		ingredients.setInputs(VanillaTypes.ITEM, recipe.inputs);
		ingredients.setOutput(VanillaTypes.ITEM, recipe.output);
	}

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, MinerRecipe recipe, IIngredients ingredients) {
		IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
		guiItemStacks.init(0, true, 45, 36);
		guiItemStacks.init(2, false, 108, 21);
		guiItemStacks.addTooltipCallback((slotIndex, input, ingredient, tooltip) -> {
			if (slotIndex == 2) {
				tooltip.add("Chance to get between 1-3");
			}
		});
		guiItemStacks.set(ingredients);
	}

	@Override
	public void draw(MinerRecipe recipe, double mouseX, double mouseY) {
		this.arrow.draw(71, 21);
	}

	@Override
	public List<String> getTooltipStrings(MinerRecipe recipe, double mouseX, double mouseY) {
		List<String> tooltip = new ArrayList<String>();
		if (mouseX >= 2 && mouseX <= 15 && mouseY >= 0 && mouseY <= 47) {
			tooltip.add("Using: 80 FE/t");
		}
		return tooltip;
	}
}
