package coloredpipes.creativetabs;

import coloredpipes.ColoredPipes;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class CreativeTabOthers extends CreativeTabs
{
	public CreativeTabOthers()
	{
		super("ColoredPipes-Misc");
	}

	@Override
	public Item getTabIconItem()
	{
		return ColoredPipes.coloredBrushes;
	}

}
