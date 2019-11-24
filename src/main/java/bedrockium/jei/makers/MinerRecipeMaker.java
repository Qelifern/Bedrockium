package bedrockium.jei.makers;


import bedrockium.init.ModItems;
import bedrockium.jei.recipes.MinerRecipe;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.helpers.IStackHelper;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class MinerRecipeMaker {

	private MinerRecipeMaker() {

	}

	public static List<MinerRecipe> getMinerRecipes(IJeiHelpers helpers) {
		IStackHelper stackHelper = helpers.getStackHelper();


		List<MinerRecipe> recipes = new ArrayList<MinerRecipe>();

		ItemStack drill = new ItemStack(ModItems.drill_head);
		drill.setDamage(1);
		MinerRecipe recipe = new MinerRecipe(drill, new ItemStack(ModItems.bedrockium));
		recipes.add(recipe);

		return recipes;

	}

}
