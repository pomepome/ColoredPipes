package coloredpipes.item;

import java.util.List;

import buildcraft.BuildCraftTransport;
import buildcraft.core.BCCreativeTab;
import buildcraft.transport.BlockGenericPipe;
import buildcraft.transport.ItemPipe;
import coloredpipes.ColoredPipes;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class ItemColoredPipe extends ItemPipe
{
	public ItemColoredPipe(BCCreativeTab creativeTab)
	{
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
	@Override
	public boolean onEntityItemUpdate(EntityItem entityItem)
    {
		if(!ColoredPipes.discolored)
    	{
    		return false;
    	}
		int x,y,z;
		World w = entityItem.worldObj;
		if(w.isRemote)
		{
			return false;
		}
		x = (int)Math.floor(entityItem.posX);
		y = (int)Math.ceil(entityItem.posY - entityItem.getYOffset());
		z = (int)Math.floor(entityItem.posZ);
		int count = entityItem.getEntityItem().stackSize;
		if(w.getBlock((int)x, (int)y, (int)z).getMaterial() == Material.water || w.canLightningStrikeAt(x, y, z))
		{
			entityItem.setDead();
			ItemStack pipe = new ItemStack(BuildCraftTransport.pipeItemsCobblestone, count);
			EntityItem eItem = new EntityItem(w, x, y, z, pipe);
			eItem.motionX = 0;
			eItem.motionY = 0;
			eItem.motionZ = 0;
			w.spawnEntityInWorld(eItem);
		}
		return false;
    }
}
