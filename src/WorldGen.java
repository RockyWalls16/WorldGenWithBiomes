import noise.SimplexOctave;

public class WorldGen
{
	private SimplexOctave heightGenerator;
	private SimplexOctave temperatureGenerator;
	private SimplexOctave oceanMoistGenerator;
	private SimplexOctave rainFallGenerator;
	
	private float seaMoist = 52;
	private float abyssMoist = 72;
	private float beachMoist = seaMoist - 1.2F;
	
	private Biome[][] biomeForGen = new Biome[24][24];
	private float[][] moistForGen = new float[24][24];
	private float[][] temperatureForGen = new float[24][24];
	private float[][] heightForGen = new float[24][24];
	
	public WorldGen(long seed)
	{
		heightGenerator = new SimplexOctave(seed, 3);
		oceanMoistGenerator = new SimplexOctave(seed * 4941316673415L, 8);
		temperatureGenerator = new SimplexOctave(seed * 8416866168168L, 12);
		rainFallGenerator = new SimplexOctave(seed * 2646815648614L, 12);
	}
	
	public void genChunk(Chunk chunk)
	{
		addBiomes(chunk);
		genHeightMap(chunk);
		smoothHeightMap(chunk);
		copyToChunk(chunk);
	}
	
	public void addBiomes(Chunk chunk)
	{	
		addOceansAndContinents(chunk);
		genTemperatureAndMoist(chunk);
		smoothClimate(chunk);
		placeBiomes(chunk);
	}
	
	public void genHeightMap(Chunk chunk)
	{
		for(int x = 0; x < 24; x++)
		{
			for(int z = 0; z < 24; z++)
			{
				int rX = chunk.getX() * 16 + x - 4;
				int rZ = chunk.getZ() * 16 + z - 4;
				
				Biome biome = biomeForGen[x][z];
				heightForGen[x][z] = (float) heightGenerator.noise(rX, rZ, 0.5, 0.008, biome.getMinHeight(), biome.getMaxHeight());
			}
		}
	}
	
	public void placeBiomes(Chunk chunk)
	{
		for(int x = 0; x < 24; x++)
		{
			for(int z = 0; z < 24; z++)
			{
				int rX = chunk.getX() * 16 + x - 4;
				int rZ = chunk.getZ() * 16 + z - 4;
				
				if(!biomeForGen[x][z].isWaterBiome() && biomeForGen[x][z] != Biome.BEACH)
				{
					biomeForGen[x][z] = Biome.bestBiomeFor(temperatureForGen[x][z], moistForGen[x][z]);
				}
			}
		}
	}
	
	public void smoothHeightMap(Chunk chunk)
	{
		for(int x = 0; x < 21; x++)
		{
			for(int z = 0; z < 21; z++)
			{
				float averageHeight = heightForGen[x][z];
				for(int x2 = 0; x2 <= 3; x2++)
				{
					for(int z2 = 0; z2 <= 3; z2++)
					{
						averageHeight += heightForGen[x + x2][z + z2];
					}
				}
				heightForGen[x][z] = averageHeight / 10.0F;
			}
		}
	}

	public void copyToChunk(Chunk chunk)
	{
		for(int x = 0; x < 16; x++)
		{
			for(int z = 0; z < 16; z++)
			{
				chunk.getHeight()[x][z] = (int) heightForGen[x + 4][z + 4];
				chunk.getBiomes()[x][z] = biomeForGen[x + 4][z + 4];
				chunk.getMoist()[x][z] = moistForGen[x + 4][z + 4];
				chunk.getTemperature()[x][z] = temperatureForGen[x + 4][z + 4];
			}
		}
	}
	
	public void addOceansAndContinents(Chunk chunk)
	{
		for(int x = 0; x < 24; x++)
		{
			for(int z = 0; z < 24; z++)
			{
				int rX = chunk.getX() * 16 + x - 4;
				int rZ = chunk.getZ() * 16 + z - 4;
				
				float moist = (float) oceanMoistGenerator.noise(rX, rZ, 0.5, 0.003, 1, 100);
				moistForGen[x][z] = moist;
				
				if(moist < beachMoist)
				{
					biomeForGen[x][z] = Biome.PLAIN;
				}
				else if(moist > abyssMoist)
				{
					biomeForGen[x][z] = Biome.ABYSS;
				}
				else if(moist > seaMoist)
				{
					biomeForGen[x][z] = Biome.OCEAN;
				}
				else
				{
					biomeForGen[x][z] = Biome.BEACH;
				}
			}
		}
	}
	
	public void genTemperatureAndMoist(Chunk chunk)
	{
		for(int x = 0; x < 24; x++)
		{
			for(int z = 0; z < 24; z++)
			{
				int rX = chunk.getX() * 16 + x - 4;
				int rZ = chunk.getZ() * 16 + z - 4;
				
				float temperature = (float) temperatureGenerator.noise(rX, rZ, 0.5, 0.004, 0.1, 100);
				temperatureForGen[x][z] = temperature;
				
				if(!biomeForGen[x][z].isWaterBiome() && biomeForGen[x][z] != Biome.BEACH)
				{
					float rainFall = (float) rainFallGenerator.noise(rX, rZ, 0.5, 0.004, 0.1, 100);
					double radTemp = temperature * Math.PI / 200.0F;
					moistForGen[x][z] = (float) Math.min(rainFall, (Math.sin(radTemp) * 100));
				}
			}
		}
	}
	
	public void smoothClimate(Chunk chunk)
	{
		for(int x = 0; x < 21; x++)
		{
			for(int z = 0; z < 21; z++)
			{
				float averageTemp = temperatureForGen[x][z];
				float averageMoist = moistForGen[x][z];
				for(int x2 = 0; x2 <= 3; x2++)
				{
					for(int z2 = 0; z2 <= 3; z2++)
					{
						averageTemp += temperatureForGen[x + x2][z + z2];
						averageMoist += moistForGen[x + x2][z + z2];
					}
				}
				temperatureForGen[x][z] = averageTemp / 10.0F;
				moistForGen[x][z] = averageMoist / 10.0F;
			}
		}
	}
}
