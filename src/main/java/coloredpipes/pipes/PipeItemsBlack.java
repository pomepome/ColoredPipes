package coloredpipes.pipes;

import buildcraft.BuildCraftTransport;
import buildcraft.api.core.IIconProvider;
import buildcraft.api.transport.IPipe;
import buildcraft.api.transport.IPipeTile;
import buildcraft.transport.BlockGenericPipe;
import buildcraft.transport.Pipe;
import buildcraft.transport.PipeTransportItems;
import buildcraft.transport.pipes.PipeItemsStone;
import buildcraft.transport.pipes.events.PipeEventItem;
import coloredpipes.ColoredPipes;
import coloredpipes.icon.IconProviderPipes;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

public class PipeItemsBlack extends Pipe<PipeTransportItems>
{
	protected int color;
	public PipeItemsBlack(Item item)
	{
		super(new PipeTransportItems(), item);
		color = 0;
	}

	@Override
	public int getIconIndex(ForgeDirection arg0)
	{
		return color;
	}

	@Override
	public IIconProvider getIconProvider()
	{
		return IconProviderPipes.instance;
	}

	public void eventHandler(PipeEventItem.AdjustSpeed event)
	{
		event.handled = true;
	}

	public int getColor()
	{
		return color;
	}

	public boolean canPipeConnect(TileEntity tile, ForgeDirection side)
	{
		if(tile instanceof IPipeTile)
		{
			IPipe otherPipe = ((IPipeTile)tile).getPipe();
			if(otherPipe instanceof PipeItemsBlack)
			{
				return ((PipeItemsBlack)otherPipe).getColor() == color;//color変数が自分のものと同じときtrueを返します
			}
			if(otherPipe instanceof PipeItemsStone && !ColoredPipes.connectAny)
			{
				return false;
			}
		}
		return super.canPipeConnect(tile, side);//適当な値を返しておきます
	}
	public void setColor(EntityPlayer player,int toColored,int side)
	{
		Pipe<?> pipe = BlockGenericPipe.createPipe(ColoredPipes.pipesColored[toColored]);//Pipeオブジェクトを生成します
		int x = container.xCoord;
		int y = container.yCoord;
		int z = container.zCoord;
		Block pipeBlock = BuildCraftTransport.genericPipeBlock;
		getWorld().setBlockToAir(x, y, z);//ないと設置されないようです
		BlockGenericPipe.placePipe(pipe, getWorld(), x, y, z, pipeBlock, 0, player, ForgeDirection.getOrientation(side));//生成したPipeを設置します
	}
}
