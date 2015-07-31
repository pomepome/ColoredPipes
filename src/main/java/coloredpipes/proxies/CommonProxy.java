package coloredpipes.proxies;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraft.item.Item;

public class CommonProxy
{
	public void onInit(FMLInitializationEvent e){}
	public void onPreInit(FMLPreInitializationEvent e){}
	public void onPostInit(FMLPostInitializationEvent e){}
	public void registerPipeRenderer(Item item){}
}
