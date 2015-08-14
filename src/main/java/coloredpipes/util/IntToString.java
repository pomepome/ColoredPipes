package coloredpipes.util;

public class IntToString
{
	private static String[] nameTable;
	public static void Init()
	{
		nameTable = new String[16];
		nameTable[0] = "black";
		nameTable[1] = "red";
		nameTable[2] = "green";
		nameTable[3] = "brown";
		nameTable[4] = "blue";
		nameTable[5] = "purple";
		nameTable[6] = "cyan";
		nameTable[7] = "lgray";
		nameTable[8] = "gray";
		nameTable[9] = "pink";
		nameTable[10] = "lgreen";
		nameTable[11] = "yellow";
		nameTable[12] = "lblue";
		nameTable[13] = "magenta";
		nameTable[14] = "orange";
		nameTable[15] = "white";
	}
	public static String idToName(int i)
	{
		if(nameTable == null)
		{
			Init();
		}
		if(i > 15)
		{
			return "";
		}
		return nameTable[i];
	}
}
