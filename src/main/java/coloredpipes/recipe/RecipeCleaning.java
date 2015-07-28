package coloredpipes.recipe;

import java.util.List;

import coloredpipes.item.ItemColoredBrush;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapelessRecipes;

public class RecipeCleaning extends ShapelessRecipes
{
	private ItemStack recipeOutput;
	public RecipeCleaning(ItemStack dest, List list)
	{
		super(dest, list);
		recipeOutput = dest.copy();
	}
	public ItemStack getCraftingResult(InventoryCrafting inv)
    {
		for(int i = 0;i < inv.getSizeInventory();i++)
		{
			ItemStack is = inv.getStackInSlot(i);
			if(is != null && is.getItem() instanceof ItemColoredBrush)
			{
				inv.setInventorySlotContents(i,null);
			}
			inv.markDirty();
		}
        return this.recipeOutput.copy();
    }
}
