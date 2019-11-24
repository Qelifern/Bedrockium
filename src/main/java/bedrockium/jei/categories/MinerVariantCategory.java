package bedrockium.jei.categories;

import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.category.IRecipeCategory;

public abstract class MinerVariantCategory<T> implements IRecipeCategory<T> {

    protected static final int inputSlot = 3;
    protected static final int energySlot = 1;
    protected static final int outputSlot = 2;
    protected static final int drillSlot = 0;

    public MinerVariantCategory(IGuiHelper guiHelper) {

    }
}
