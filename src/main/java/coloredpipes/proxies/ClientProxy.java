package coloredpipes.proxies;

import static coloredpipes.ColoredPipes.*;

import buildcraft.BuildCraftTransport;
import buildcraft.transport.TransportProxyClient;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.MinecraftForgeClient;

public class ClientProxy extends CommonProxy
{
	@Override
	public void onInit(FMLInitializationEvent event)
	{
		for(int i = 0;i < 16;i++)
		{
			registerPipeRenderer(pipesColored[i]);
		}
	}
	@Override
	public void onPreInit(FMLPreInitializationEvent e)
	{
		for(int i = 0;i < 16;i++)
		{
			GameRegistry.addShapelessRecipe(
					new ItemStack(pipesColored[i]),
					BuildCraftTransport.pipeItemsCobblestone,
					new ItemStack(Items.dye,1,i)
			);
		}
	}
	@Override
	public void registerPipeRenderer(Item item)
	{
		MinecraftForgeClient.registerItemRenderer(item,TransportProxyClient.pipeItemRenderer);
	}
}
