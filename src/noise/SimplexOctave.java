package noise;

/**
 * 
 * @author Valentin4311
 *
 */
public class SimplexOctave
{
	private SimplexNoise[] octaves;
	
	public SimplexOctave(long seed, int octaves)
	{
		this.octaves = new SimplexNoise[octaves];
		for(int i = 0; i < octaves; i++)
		{
			this.octaves[i] = new SimplexNoise(seed);
		}
	}
	
	public double noise(double x, double y, double persistence, double scale, double low, double high)
	{
	    double maxAmp = 0;
	    double amp = 1;
	    double freq = scale;
	    double noise = 0;


	    for(int i = 0; i < this.octaves.length; ++i)
	    {
	        noise += this.octaves[i].noise(x * freq, y * freq) * amp;
	        maxAmp += amp;
	        amp *= persistence;
	        freq *= 2;
	    }

	    noise /= maxAmp;

	    noise = noise * (high - low) / 2 + (high + low) / 2;

	    return noise;
	}
}
