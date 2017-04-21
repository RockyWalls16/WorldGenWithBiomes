import noise.SimplexOctave;

/**
 * Generate world using different steps :
 * 1 - Generates a moist noist to determine oceans from continents
 * 2 - Generates temperatures and rainfall (moist2) on continents
 * 3 - Use temperatures and rainfall to determine which biome to place
 * 4 - Generate height map based on biome base height
 * 5 - Add height noise to add more variations to height
 * 6 - Smooth heights in order to reduce height gap between biomes
 * 
 * Disclaimer:
 *  - This algorithm is not optimized at all, I preferred to write something clear
 *  - This algorithm is far from being totally complete
 *  - This algorithm is "inspired" by minecraft generation in 1.7.2
 * @author Valentin4311
 *
 */
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
	
	/**
	 * Generate chunk data
	 * @param chunk
	 */
	public void genChunk(Chunk chunk)
	{
		addBiomes(chunk);
		genHeightMap(chunk);
		smoothHeightMap(chunk);
		copyToChunk(chunk);
	}
	
	/**
	 * Add biomes to the chunk by using a layer system
	 * @param chunk
	 */
	public void addBiomes(Chunk chunk)
	{	
		addOceansAndContinents(chunk);
		genTemperatureAndMoist(chunk);
		//smoothClimate(chunk); //TODO
		placeBiomes(chunk);
	}
	
	/**
	 * Generate height map using biome properties
	 * @param chunk
	 */
	public void genHeightMap(Chunk chunk)
	{
		for(int x = 0; x < 24; x++)
		{
			for(int z = 0; z < 24; z++)
			{
				int rX = chunk.getX() * 16 + x - 4;
				int rZ = chunk.getZ() * 16 + z - 4;
				
				Biome biome = biomeForGen[x][z];
				heightForGen[x][z] = (float) heightGenerator.noise(rX, rZ, 0.5, 0.002, biome.getMinHeight(), biome.getMaxHeight());
			}
		}
	}
	
	/**
	 * Place non water biomes
	 * @param chunk
	 */
	public void placeBiomes(Chunk chunk)
	{
		for(int x = 0; x < 24; x++)
		{
			for(int z = 0; z < 24; z++)
			{
				if(!biomeForGen[x][z].isWaterBiome() && biomeForGen[x][z] != Biome.BEACH)
				{
					biomeForGen[x][z] = Biome.bestBiomeFor(temperatureForGen[x][z], moistForGen[x][z]);
				}
			}
		}
	}
	
	/**
	 * Smooth height map in order to reduce gap between biomes of different heights
	 * @param chunk
	 */
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

	/**
	 * Copy world generator to chunk
	 * @param chunk
	 */
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
	
	/**
	 * Add oceans, abyss and continents based on moist
	 * @param chunk
	 */
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
				
				//Continents
				if(moist < beachMoist)
				{
					biomeForGen[x][z] = Biome.PLAIN;
				}
				//Deep water
				else if(moist > abyssMoist)
				{
					biomeForGen[x][z] = Biome.ABYSS;
				}
				//Water
				else if(moist > seaMoist)
				{
					biomeForGen[x][z] = Biome.OCEAN;
				}
				//Beach
				else
				{
					biomeForGen[x][z] = Biome.BEACH;
				}
			}
		}
	}
	
	/**
	 * Generate temperature layer and moist (continent moist) layer
	 * @param chunk
	 */
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
	
	/**
	 * TODO WIP
	 * Smooth climate to remove biome that are too small
	 * @param chunk
	 */
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
