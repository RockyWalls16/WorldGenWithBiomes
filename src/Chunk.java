
public class Chunk
{
	private int x;
	private int z;
	private Biome[][] biomes = new Biome[16][16];
	private int[][] height = new int[16][16];
	private float[][] temperature = new float[16][16];
	private float[][] moist = new float[16][16];
	
	public Chunk(int x, int z)
	{
		this.x = x;
		this.z = z;
	}

	public int getX()
	{
		return x;
	}

	public int getZ()
	{
		return z;
	}

	public Biome[][] getBiomes()
	{
		return biomes;
	}

	public int[][] getHeight()
	{
		return height;
	}

	public float[][] getTemperature()
	{
		return temperature;
	}

	public float[][] getMoist()
	{
		return moist;
	}
}
