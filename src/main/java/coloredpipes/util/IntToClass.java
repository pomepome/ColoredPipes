package coloredpipes.util;

import java.util.ArrayList;
import java.util.List;

import buildcraft.transport.Pipe;
import coloredpipes.pipes.PipeItemsBlack;
import coloredpipes.pipes.PipeItemsBlue;
import coloredpipes.pipes.PipeItemsBrown;
import coloredpipes.pipes.PipeItemsCyan;
import coloredpipes.pipes.PipeItemsGray;
import coloredpipes.pipes.PipeItemsGreen;
import coloredpipes.pipes.PipeItemsLightBlue;
import coloredpipes.pipes.PipeItemsLightGray;
import coloredpipes.pipes.PipeItemsLightGreen;
import coloredpipes.pipes.PipeItemsMagenta;
import coloredpipes.pipes.PipeItemsOrange;
import coloredpipes.pipes.PipeItemsPink;
import coloredpipes.pipes.PipeItemsPurple;
import coloredpipes.pipes.PipeItemsRed;
import coloredpipes.pipes.PipeItemsWhite;
import coloredpipes.pipes.PipeItemsYellow;

public class IntToClass
{
	//色々めんどくさかったのでつくりました。パイプにダメージ値を設定する方法あるのかな？
	public static List<Class<? extends Pipe<?>>> list;

	public static void init()
	{
		list = new ArrayList<Class<? extends Pipe<?>>>();
		list.add(PipeItemsBlack.class);
		list.add(PipeItemsRed.class);
		list.add(PipeItemsGreen.class);
		list.add(PipeItemsBrown.class);
		list.add(PipeItemsBlue.class);
		list.add(PipeItemsPurple.class);
		list.add(PipeItemsCyan.class);
		list.add(PipeItemsLightGray.class);
		list.add(PipeItemsGray.class);
		list.add(PipeItemsPink.class);
		list.add(PipeItemsLightGreen.class);
		list.add(PipeItemsYellow.class);
		list.add(PipeItemsLightBlue.class);
		list.add(PipeItemsMagenta.class);
		list.add(PipeItemsOrange.class);
		list.add(PipeItemsWhite.class);
	}

	public static Class<? extends Pipe<?>> getClass(int id)
	{
		if(list == null)
		{
			init();
		}
		if(id > 15)
		{
			return null;
		}
		return list.get(id);
	}
}
