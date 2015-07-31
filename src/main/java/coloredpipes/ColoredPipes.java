package coloredpipes;

import java.util.ArrayList;
import java.util.List;

import buildcraft.core.BCCreativeTab;
import buildcraft.transport.BlockGenericPipe;
import buildcraft.transport.ItemPipe;
import buildcraft.transport.Pipe;
import buildcraft.transport.TransportProxy;
import coloredpipes.item.ItemColoredBrush;
import coloredpipes.item.ItemColoredPipe;
import coloredpipes.item.ItemPlainBrush;
import coloredpipes.proxies.CommonProxy;
import coloredpipes.recipe.RecipeCleaning;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.entity.player.EntityInteractEvent;

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

	/*
	 * Brush Items
	 */
	public static Item pBrush;
	public static Item coloredBrushes;

	/*
	 * Misc
	 */
	public static boolean connectAny;
	public static boolean autoRefill;
	public static boolean discolored;

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
		pBrush = new ItemPlainBrush();
		coloredBrushes = new ItemColoredBrush();
		tab.setIcon(new ItemStack(pipesColored[10]));
		loadConfig(new Configuration(e.getSuggestedConfigurationFile()));
		MinecraftForge.EVENT_BUS.register(this);
		proxy.onPreInit(e);
	}
	@EventHandler
	public void postInit(FMLPostInitializationEvent e)
	{
		proxy.onPostInit(e);
	}
	public void loadConfig(Configuration config)
	{
		config.load();
		connectAny = config.getBoolean("connectAny", "pipes", false, "whether Colored Pipes will connect to stone pipes or not");
		autoRefill = config.getBoolean("autoRefill", "misc", false, "wether Colored Brush will refeill automatically when run out of dies or not");
		discolored = config.getBoolean("discolored", "misc", false, "wether Colored Brush or Colored Pipes will become discolored for water");
		config.save();
	}
	public void registerColoredPipes()
	{
		for(int i = 0;i < 16;i++)
		{
			pipesColored[i] = registerPipe(IntToClass.getClass(i),"cp_"+IntToString.idToName(i));
		}
	}
	public ItemStack makeColoredBrush(int color)
	{
		ItemStack result = new ItemStack(coloredBrushes,1,color);
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setByte("Damage",(byte)0);
		return result;
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
	public static void addCleanRecipe(ItemStack dest,Object... objs)
	{
		List<ItemStack> recipeItems = new ArrayList<ItemStack>();
		for(Object obj : objs)
		{
			if(obj instanceof ItemStack)
			{
				recipeItems.add((ItemStack)obj);
			}
			if(obj instanceof Block)
			{
				recipeItems.add(new ItemStack((Block)obj));
			}
			if(obj instanceof Item)
			{
				recipeItems.add(new ItemStack((Item)obj));
			}
		}
		GameRegistry.addRecipe(new RecipeCleaning(dest,recipeItems));
	}
	/*
	 * Event handlers
	 */
	@SubscribeEvent
	public void onInteract(EntityInteractEvent event)
	{
		Entity target = event.target;
		EntityPlayer p = event.entityPlayer;
		if(target != null && target instanceof EntitySheep)
		{
			EntitySheep sheep = (EntitySheep)target;
			ItemStack is = p.getCurrentEquippedItem().copy();
			if(is != null && is.getItem() == coloredBrushes)
			{
				int brushColor = is.getItemDamage();
				int sheepColor = 0xf - sheep.getFleeceColor();
				if(brushColor != sheepColor)
				{
					sheep.setFleeceColor(0xf - brushColor);
					if(p.capabilities.isCreativeMode)
					{
						return;
					}
					int brushDamage = ItemColoredBrush.getDurability(is) + 1;
					if(brushDamage < ItemColoredBrush.getMaxUse())
					{
						ItemColoredBrush.setDurability(is, brushDamage);
					}
					else
					{
						ItemStack dye = new ItemStack(Items.dye, 1, brushColor);
						if(autoRefill && ItemColoredBrush.useResource(p.inventory, dye, true))
						{
							ItemColoredBrush.setDurability(is, 0);
						}
						else
						{
							is = new ItemStack(pBrush);
						}
					}
				}
				p.inventory.mainInventory[p.inventory.currentItem] = is;
			}
		}
	}
}
