
public class Main
{
	public static void main(String[] args)
	{
		System.out.println("Starting world gen...");
		int sizeX = 28;
		int sizeZ = 28;
		
		World world = new World(sizeX, sizeZ);
		new Frame(world, sizeX, sizeZ);
	}
}
