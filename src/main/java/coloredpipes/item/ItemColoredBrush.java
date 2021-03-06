package coloredpipes.item;

import java.util.List;

import buildcraft.BuildCraftTransport;
import buildcraft.transport.BlockGenericPipe;
import buildcraft.transport.Pipe;
import buildcraft.transport.TileGenericPipe;
import buildcraft.transport.pipes.PipeItemsCobblestone;
import coloredpipes.ColoredPipes;
import coloredpipes.pipes.PipeItemsBlack;
import coloredpipes.util.IntToString;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockColored;
import net.minecraft.block.BlockGlass;
import net.minecraft.block.BlockHardenedClay;
import net.minecraft.block.BlockPane;
import net.minecraft.block.BlockStainedGlass;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class ItemColoredBrush extends Item
{
	private IIcon[] icons = new IIcon[16];
	public static final int maxUse = 64;
	private final String[] colors;

	public ItemColoredBrush()
	{
		this.setUnlocalizedName("cbrush").setCreativeTab(ColoredPipes.instance.tab).setContainerItem(this).setHasSubtypes(true).setMaxDamage(0).setMaxStackSize(1);
		GameRegistry.registerItem(this,"cbrush");
		colors = new String[]
		{
			"Black","Red","Green","Brown",
			"Blue","Purple","Cyan","Light Gray",
			"Gray","Pink","Light Green","Yellow",
			"Light Blue","Magenta","Orange","White"
		};
	}
	public static int getMaxUse()
	{
		return maxUse;
	}
	/*
	 * Crefting methods
	 */
	@Override
    public boolean doesContainerItemLeaveCraftingGrid(ItemStack itemStack)
    {
        return false;
    }
	public static void initItem(ItemStack item)
	{
		if(!item.hasTagCompound())
        {
        	NBTTagCompound tag = new NBTTagCompound();
        	tag.setByte("Damage", (byte)0);
        	item.setTagCompound(tag);
        }
	}
	@Override
    public ItemStack getContainerItem(ItemStack itemStack)
    {
        ItemStack newStack = itemStack.copy();

        byte damage = itemStack.getTagCompound().getByte("Damage");
        byte damaged = (byte)(damage + 1);

        if(damaged < maxUse)
        {
        	newStack.getTagCompound().setByte("Damage", damaged);
        }
        else
        {
        	newStack = new ItemStack(ColoredPipes.pBrush);
        }
        newStack.stackSize = 1;

        return newStack;
    }
	@SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List list)
    {
		for(int i = 0;i < 16;i++)
		{
			ItemStack is = new ItemStack(item, 1, i);
			initItem(is);
			list.add(is);
		}
    }
	@Override
	public void onCreated(ItemStack stack, World world, EntityPlayer player)
	{
		initItem(stack);
	}
	/*
	 * Icon methods
	 */
	@Override
    public IIcon getIconFromDamage(int damage)
    {
        return icons[damage];
    }
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister register)
    {
        for(int i = 0;i < 16;i++)
        {
        	icons[i] = register.registerIcon("coloredpipes:pbrush_" + i);
        }
    }

    /*
     * Durability bar methods
     */
    @Override
    public boolean showDurabilityBar(ItemStack stack)
    {
    	if(!stack.hasTagCompound())
    	{
    		return false;
    	}
        return stack.getTagCompound().getByte("Damage") > 0;
    }
    @Override
    public double getDurabilityForDisplay(ItemStack stack)
    {
    	double damage = (double)stack.getTagCompound().getByte("Damage");
        return (double)damage / (double)(maxUse + 1);
    }
    /*
     * Block clicked method
     */
    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
    {
    	int color = stack.getItemDamage();
    	Block pipeBlock = BuildCraftTransport.genericPipeBlock;
    	TileEntity tile = world.getTileEntity(x, y, z);
    	if(tile instanceof TileGenericPipe)
    	{
    		Pipe pipe = ((TileGenericPipe)tile).pipe;
    		if(pipe instanceof PipeItemsCobblestone)
    		{
    			world.setBlockToAir(x, y, z);
    			Pipe newPipe = BlockGenericPipe.createPipe(ColoredPipes.pipesColored[color]);
    			BlockGenericPipe.placePipe(newPipe, world, x, y, z, pipeBlock, 0, player);
    		}
    		else if(pipe instanceof PipeItemsBlack)
    		{
    			PipeItemsBlack newPipe = (PipeItemsBlack)pipe;
    			if(newPipe.getColor() == color)
    			{
    				return true;
    			}
    			newPipe.setColor(player, color);
    		}
    		else
    		{
    			return true;
    		}
    		brushDamaging(stack, player, color);
    	}
    	if(world.getBlock(x, y, z) instanceof BlockColored)
    	{
    		int blockCol = 0xf - world.getBlockMetadata(x, y, z);
    		if(color != blockCol)
    		{
    			brushDamaging(stack,player,color);
    			world.setBlockMetadataWithNotify(x, y, z, 0xf - color, 2);
    		}
    	}
    	if(world.getBlock(x, y, z) instanceof BlockHardenedClay)
    	{
    		brushDamaging(stack,player,color);
    		world.setBlock(x, y, z, Blocks.stained_hardened_clay);
    		world.setBlockMetadataWithNotify(x, y, z, 0xf - color, 2);
    	}
    	if(world.getBlock(x, y, z) instanceof BlockGlass)
    	{
    		brushDamaging(stack,player,color);
    		world.setBlock(x, y, z, Blocks.stained_glass);
    		world.setBlockMetadataWithNotify(x, y, z, 0xf - color, 2);
    	}
    	if(world.getBlock(x, y, z) instanceof BlockPane)
    	{
    		brushDamaging(stack,player,color);
    		world.setBlock(x, y, z, Blocks.stained_glass_pane);
    		world.setBlockMetadataWithNotify(x, y, z, 0xf - color, 2);
    	}
    	if(world.getBlock(x, y, z) instanceof BlockStainedGlass)
    	{
    		int blockCol = 0xf - world.getBlockMetadata(x, y, z);
    		if(color != blockCol)
    		{
    			brushDamaging(stack,player,color);
    			world.setBlock(x, y, z, Blocks.stained_glass);
    			world.setBlockMetadataWithNotify(x, y, z, 0xf - color, 2);
    		}
    	}
    	return true;
    }
	private void brushDamaging(ItemStack stack, EntityPlayer player, int color)
	{
		if(player.capabilities.isCreativeMode)
		{
			return;
		}
		if(getDurability(stack) + 1 < maxUse)
		{
			setDurability(stack,getDurability(stack) + 1);
		}
		else
		{
			ItemStack dye = new ItemStack(Items.dye, 1, color);
			if(ColoredPipes.autoRefill && useResource(player.inventory,dye,true))
			{
				setDurability(stack,0);
			}
			else
			{
				int currentIndex = player.inventory.currentItem;
				player.inventory.mainInventory[currentIndex] = new ItemStack(ColoredPipes.pBrush);
			}
		}
	}
    /*
     * ToolTip methods
     */
    @Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean advanced)
	{
    	list.add(getLocalizedString("cp.color") + ": " + getLocalizedString("cp.colors." + IntToString.idToName(stack.getItemDamage())));
    	list.add(getLocalizedString("cp.preLeft") + (maxUse - getDurability(stack)) + getLocalizedString("cp.left"));
	}
    public String getLocalizedString(String id)
    {
    	return StatCollector.translateToLocal(id);
    }
    /*
     * Inventory Util methods
     */
    public static boolean useResource(InventoryPlayer p,ItemStack toSearch,boolean use)
    {
    	if(use)
    	{
    		boolean enough = useResource(p,toSearch,false);
    		if(!enough)
    		{
    			return false;
    		}
    		for(int i = 0;i < p.mainInventory.length;i++)
    		{
    			ItemStack item = p.mainInventory[i];
    			if(item != null && item.getItem().equals(toSearch.getItem()))
    			{
    				if(item.stackSize >= toSearch.stackSize)
    				{
    					item.stackSize -= toSearch.stackSize;
    				}
    				else
    				{
    					toSearch.stackSize -= item.stackSize;
    					item.stackSize = 0;
    				}
    				if(item.stackSize == 0)
    				{
    					item = null;
    				}
    				if(toSearch.stackSize == 0)
    				{
    					break;
    				}
    			}
    		}
    		return true;
    	}
    	else
    	{
    		int count = 0;
    		for(int i = 0;i < p.mainInventory.length;i++)
    		{
    			ItemStack item = p.mainInventory[i];
    			if(item != null && item.getItem().equals(toSearch.getItem()))
    			{
    				count += item.stackSize;
    			}
    		}
    		return count >= toSearch.stackSize;
    	}
    }
    public static int getDurability(ItemStack stack)
    {
    	return stack.stackTagCompound.getByte("Damage");
    }
    public static void setDurability(ItemStack stack,int damage)
    {
    	stack.stackTagCompound.setByte("Damage",(byte)damage);
    }
    /*
     * EntityItem Update method
     */
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
		if(w.getBlock((int)x, (int)y, (int)z).getMaterial() == Material.water || w.canLightningStrikeAt(x, y, z))
		{
			entityItem.setDead();
			EntityItem eItem = new EntityItem(w, x, y, z, new ItemStack(ColoredPipes.pBrush));
			eItem.motionX = 0;
			eItem.motionY = 0;
			eItem.motionZ = 0;
			w.spawnEntityInWorld(eItem);
		}
		return false;
    }
}
