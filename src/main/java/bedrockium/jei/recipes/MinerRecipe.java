package bedrockium.jei.recipes;

import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class MinerRecipe {
	/* Recipe */
	public List<ItemStack> inputs;
	public final ItemStack output;

	public MinerRecipe(ItemStack input, ItemStack output) {
		List<ItemStack> inputList = new ArrayList<ItemStack>();
		inputList.add(input);
		this.inputs = inputList;
		this.output = output;
	}
}
