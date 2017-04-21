import java.awt.Color;

/**
 * Enum containing all biomes
 * @author Valentin4311
 *
 */
public enum Biome
{
	ABYSS(new Color(0, 0, 128), 10, 13, 0, 0, 0, 0, 0),
	OCEAN(Color.BLUE, 30, 35, 0, 0, 0, 0, 0),
	BEACH(new Color(230, 190, 40), 45, 48, 0, 0, 0, 0, 0),
	
	PLAIN(Color.GREEN, 50, 54, 25, 60, 0, 35, 3),
	DESERT(Color.YELLOW, 52, 58, 60, 100, 0, 35, 5),
	FOREST(new Color(0, 176, 0), 50, 54, 50, 100, 35, 50, 4),
	DEEP_FOREST(new Color(0, 128, 0), 50, 54, 50, 100, 50, 75, 4),
	HILLS(new Color(120, 200, 120), 60, 90, 35, 50, 35, 75, 2),
	JUNGLE(Color.ORANGE, 60, 70, 80, 100, 75, 100, 6),
	SNOW(Color.WHITE, 53, 60, 0, 35, 35, 75, 5),
	SNOW_MOUNTAIN(new Color(235, 235, 235), 60, 140, 0, 25, 0, 35, 4);
	
	private Color color;
	private int minHeight;
	private int maxHeight;
	private int minTemp;
	private int maxTemp;
	private int minRainFall;
	private int maxRainFall;
	private int priority;
	
	/**
	 * Create a new biome
	 * @param color, display color
	 * @param minHeight, min height of the biome
	 * @param maxHeight, max height of the biome
	 * @param minTemp, min temperature
	 * @param maxTemp, max temperature
	 * @param minRainFall, min moist
	 * @param maxRainFall, max moist
	 * @param priority, if multiple biomes suits for a climate, world select the higher priority
	 */
	Biome(Color color, int minHeight, int maxHeight, int minTemp, int maxTemp, int minRainFall, int maxRainFall, int priority)
	{
		World.biomes.add(this);
		this.color = color;
		this.minHeight = minHeight;
		this.maxHeight = maxHeight;
		this.minTemp = minTemp;
		this.maxTemp = maxTemp;
		this.minRainFall = minRainFall;
		this.maxRainFall = maxRainFall;
		this.priority = priority;
	}

	public Color getColor()
	{
		return color;
	}
	
	public int getMinHeight()
	{
		return minHeight;
	}

	public int getMaxHeight()
	{
		return maxHeight;
	}

	public int getMinTemp()
	{
		return minTemp;
	}

	public int getMaxTemp()
	{
		return maxTemp;
	}

	public int getMinRainFall()
	{
		return minRainFall;
	}

	public int getMaxRainFall()
	{
		return maxRainFall;
	}

	public boolean isWaterBiome()
	{
		return this == ABYSS || this == OCEAN;
	}
	
	public boolean isAffectedByClimate()
	{
		return this != ABYSS && this != OCEAN && this != BEACH;
	}
	
	/**
	 * Find the most suited biome for a certain temperature & moist
	 * @param temperature
	 * @param moist
	 * @return most suited biome for climate
	 */
	public static Biome bestBiomeFor(float temp, float rainFall)
	{
		Biome best = null;
		for(Biome choice : World.biomes)
		{
			if(choice.isAffectedByClimate() && temp >= choice.minTemp && temp <= choice.maxTemp && rainFall >= choice.minRainFall && rainFall <= choice.maxRainFall)
			{
				if(best != null)
				{
					if(choice.priority > best.priority)
					{
						choice = best;
					}
				}
				else
				{
					best = choice;
				}
			}
		}
		
		return best == null ? Biome.FOREST : best;
	}
}
