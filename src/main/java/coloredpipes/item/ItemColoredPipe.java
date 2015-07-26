package coloredpipes.item;

import java.util.List;

import buildcraft.core.BCCreativeTab;
import buildcraft.transport.BlockGenericPipe;
import buildcraft.transport.ItemPipe;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

public class ItemColoredPipe extends ItemPipe
{
	public ItemColoredPipe(BCCreativeTab creativeTab) {
		super(creativeTab);
	}
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean advanced)
	{
		String tip = StatCollector.translateToLocal("tip." + BlockGenericPipe.createPipe(stack.getItem()).getClass().getSimpleName());
		String[] lines = tip.split(",");
		for(int i = 0;i < lines.length;i++)
		{
			list.add(lines[i]);
		}
	}
}
