package coloredpipes;

import buildcraft.core.BCCreativeTab;
import buildcraft.transport.BlockGenericPipe;
import buildcraft.transport.ItemPipe;
import buildcraft.transport.Pipe;
import buildcraft.transport.TransportProxy;
import coloredpipes.item.ItemColoredPipe;
import coloredpipes.proxies.CommonProxy;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

@Mod(modid = "ColoredPipes",name="ColoredPipes",version="1.0",dependencies="required-after:BuildCraft|Transport@[7.0.3,)")
public class ColoredPipes
{
	@Mod.Instance
	public static ColoredPipes instance;
	@SidedProxy(clientSide="coloredpipes.proxies.ClientProxy",serverSide="coloredpipes.proxies.CommonProxy")
	public static CommonProxy proxy;

	public BCCreativeTab tab;

	/*
	 * Pipes
	 */
	public static Item pipesColored[];

	@EventHandler
	public void init(FMLInitializationEvent e)
	{
		proxy.onInit(e);
	}
	@EventHandler
	public void preInit(FMLPreInitializationEvent e)
	{
		pipesColored = new Item[16];
		tab = new BCCreativeTab("ColoredPipes");
		registerColoredPipes();
		tab.setIcon(new ItemStack(pipesColored[10]));
		proxy.onPreInit(e);
	}
	public void registerColoredPipes()
	{
		for(int i = 0;i < 16;i++)
		{
			pipesColored[i] = registerPipe(IntToClass.getClass(i),"cp_"+IntToString.idToName(i));
		}
	}
	public Item registerPipe(Class<? extends Pipe<?>> pipeClass,String name)
	{
		//BlockGenericPipe.registerPipeと同じ内容です
		ItemPipe pipe = new ItemColoredPipe(tab);
		pipe.setUnlocalizedName(name);
		BlockGenericPipe.pipes.put(pipe, pipeClass);
		GameRegistry.registerItem(pipe, name);
		Pipe<?> pipeDummy = BlockGenericPipe.createPipe(pipe);//ダミーパイプを作ります(描画用)
		if(pipeDummy != null)
		{
			pipe.setPipeIconIndex(pipeDummy.getIconIndexForItem());
			TransportProxy.proxy.setIconProviderFromPipe(pipe, pipeDummy);
		}
		return pipe;
	}
}
