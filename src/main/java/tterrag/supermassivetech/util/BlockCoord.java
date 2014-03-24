package tterrag.supermassivetech.util;

public class BlockCoord
{

	public int x, y, z;

	public BlockCoord(int x, int y, int z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj instanceof BlockCoord)
		{
			BlockCoord coord = (BlockCoord) obj;

			return coord.x == this.x && coord.y == this.y && coord.z == this.z;
		}

		return false;
	}
}
