import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class World
{
	private Chunk[][] chunkArray;
	public static List<Biome> biomes = new ArrayList<Biome>();
	
	public World(int sizeX, int sizeZ)
	{
		WorldGen gen = new WorldGen(new Random().nextLong());
		chunkArray = new Chunk[sizeX][sizeZ];
		int amount = sizeX * sizeZ;
		int current = 0;
		
		for(int x = 0; x < sizeX; x++)
		{
			for(int z = 0; z < sizeZ; z++)
			{
				chunkArray[x][z] = new Chunk(x, z);
				gen.genChunk(chunkArray[x][z]);
				
				current++;
				if(current % 10000 == 0)
				{
					System.out.println(current + " / " + amount + " " + ((current / (float) amount) * 100) + "%");
				}
			}
		}
	}

	public Chunk[][] getChunkArray()
	{
		return chunkArray;
	}
	
	public Biome biomeAt(int x, int z)
	{
		return chunkArray[(int) (x / 16)][(int) (z / 16)].getBiomes()[x % 16][z % 16];
	}

	public int heightAt(int x, int z)
	{
		return chunkArray[(int) (x / 16)][(int) (z / 16)].getHeight()[x % 16][z % 16];
	}
	
	public float tempAt(int x, int z)
	{
		return chunkArray[(int) (x / 16)][(int) (z / 16)].getTemperature()[x % 16][z % 16];
	}
	
	public float moistAt(int x, int z)
	{
		return chunkArray[(int) (x / 16)][(int) (z / 16)].getMoist()[x % 16][z % 16];
	}
}
