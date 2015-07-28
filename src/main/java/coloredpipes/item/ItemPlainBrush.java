package coloredpipes.item;

import coloredpipes.ColoredPipes;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.Item;

public class ItemPlainBrush extends Item
{
	public ItemPlainBrush()
	{
		this.setUnlocalizedName("pbrush").setCreativeTab(ColoredPipes.instance.tab).setTextureName("coloredpipes:pbrush");
		GameRegistry.registerItem(this,"pbrush");
	}
}
