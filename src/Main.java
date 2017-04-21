
public class Main
{
	public static void main(String[] args)
	{
		System.out.println("Starting world gen...");
		
		//Map size in chunks
		int sizeX = 15;
		int sizeZ = 15;
		
		World world = new World(sizeX, sizeZ);
		new Frame(world, sizeX, sizeZ);
	}
}
