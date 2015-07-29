package coloredpipes.proxies;

import static coloredpipes.ColoredPipes.*;

import buildcraft.BuildCraftTransport;
import buildcraft.transport.TransportProxyClient;
import coloredpipes.ColoredPipes;
import coloredpipes.item.ItemColoredBrush;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Blocks;
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
		GameRegistry.addRecipe(new ItemStack(ColoredPipes.pBrush),
				" W",
				"S ",
				'W',
				new ItemStack(Blocks.wool, 1, 32767),
				'S',
				Items.stick
				);
		for(int i = 0;i < 16;i++)
		{
			ItemStack pb = new ItemStack(ColoredPipes.coloredBrushes,1,i);
			ItemColoredBrush.initItem(pb);

			GameRegistry.addShapelessRecipe(pb,
					ColoredPipes.pBrush,
					new ItemStack(Items.dye, 1, i),
					Items.water_bucket
			);
			GameRegistry.addShapelessRecipe(
					new ItemStack(pipesColored[i]),
					BuildCraftTransport.pipeItemsCobblestone,
					pb
			);
			GameRegistry.addShapelessRecipe(new ItemStack(BuildCraftTransport.pipeItemsCobblestone),
				pipesColored[i],
				Items.water_bucket
			);

		}
		ColoredPipes.addCleanRecipe(new ItemStack(ColoredPipes.pBrush),
				new ItemStack(ColoredPipes.coloredBrushes,1,32767),
				Items.water_bucket);
	}
	@Override
	public void registerPipeRenderer(Item item)
	{
		MinecraftForgeClient.registerItemRenderer(item,TransportProxyClient.pipeItemRenderer);
	}
}
