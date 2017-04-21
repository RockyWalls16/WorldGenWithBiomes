import java.awt.Color;

/**
 * Helper for colors
 * @author Valentin4311
 *
 */
public class ColorUtil
{
	public static Color lerp(Color x, Color y, float blending)
	{
		Color z = x;
		x = y;
		y = z;
		float inverse_blending = 1 - blending;

		float red =   x.getRed()   * blending   +   y.getRed()   * inverse_blending;
		float green = x.getGreen() * blending   +   y.getGreen() * inverse_blending;
		float blue =  x.getBlue()  * blending   +   y.getBlue()  * inverse_blending;

		//note that if i pass float values they have to be in the range of 0.0-1.0 
		//and not in 0-255 like the ones i get returned by the getters.
		return new Color (Math.max(Math.min(red / 255F, 1F), 0), Math.max(Math.min(green / 255F, 1F), 0), Math.max(Math.min(blue / 255F, 1F), 0));
	}
}
