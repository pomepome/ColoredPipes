package coloredpipes.handler;

import coloredpipes.ColoredPipes;
import coloredpipes.item.ItemColoredBrush;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.EntityInteractEvent;

public class EntityHandler
{
	@SubscribeEvent
	public void onInteract(EntityInteractEvent event)
	{
		EntityPlayer p = event.entityPlayer;
		if(p.worldObj.isRemote)
		{
			return;
		}
		ItemStack item = p.getCurrentEquippedItem().copy();
		if(item == null || !(item.getItem() instanceof ItemColoredBrush))
		{
			return;
		}
		int brushCol = item.getItemDamage();
		Entity target = event.target;
		if(target instanceof EntitySheep)
		{
			EntitySheep sheep = (EntitySheep)target;
			int sheepCol = 0xf -  sheep.getFleeceColor();
			if(sheepCol != brushCol)
			{
				sheep.setFleeceColor(0xf - brushCol);
				if(p.capabilities.isCreativeMode)
				{
					return;
				}
				int damage = item.stackTagCompound.getByte("Damage") + 1;
				if(damage < ItemColoredBrush.maxUse)
				{
					item.stackTagCompound.setByte("Damage", (byte)damage);
				}
				else
				{
					ItemStack dye = new ItemStack(Items.dye, 1, brushCol);
					if(ColoredPipes.autoRefill && ItemColoredBrush.useResource(p.inventory, dye, true))
					{
						item.stackTagCompound.setByte("Damage", (byte)0);
					}
					else
					{
						item = new ItemStack(ColoredPipes.pBrush);
					}
				}
				p.inventory.mainInventory[p.inventory.currentItem] = item;
				p.inventory.markDirty();
			}
		}
	}
}
