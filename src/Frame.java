import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class Frame extends JPanel
{
	private static final long serialVersionUID = 1L;

	private int sizeX;
	private int sizeZ;
	private World world;
	private int drawMode = 0;
	private JScrollPane scrollPane;
	private boolean displayEntireMap = false;
	private boolean showGrid = false;
	
	private static Color winter = new Color(204, 255, 255);
	private static Color grid = new Color(0,0,0, 0.5F);
	
	public Frame(World world, int sizeX, int sizeZ)
	{
		this.world = world;
		this.sizeX = sizeX;
		this.sizeZ = sizeZ;
		JFrame frame = new JFrame("World Gen");
		frame.setSize(500, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setPreferredSize(new Dimension(sizeX * 32, sizeZ * 32));
		scrollPane = new JScrollPane(this);
		frame.setContentPane(scrollPane);
		frame.setVisible(true);
		this.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent e)
			{
				super.mouseClicked(e);
				int x = (e.getX() / 2);
				int z = (e.getY() / 2);
				
				System.out.println("B: " + world.biomeAt(x, z) + " ∆: " + world.heightAt(x, z));
			}
			
		});
		frame.addKeyListener(new KeyListener()
		{
			@Override
			public void keyTyped(KeyEvent e){}
			
			@Override
			public void keyReleased(KeyEvent e){}
			
			@Override
			public void keyPressed(KeyEvent e)
			{
				if(e.getKeyChar() == 'h')
				{
					drawMode = 0;
					repaint();
				}
				if(e.getKeyChar() == 'c')
				{
					showGrid = !showGrid;
					repaint();
				}
				if(e.getKeyChar() == 'b')
				{
					drawMode = 1;
					repaint();
				}
				if(e.getKeyChar() == 't')
				{
					drawMode = 2;
					repaint();
				}
				if(e.getKeyChar() == 'm')
				{
					drawMode = 3;
					repaint();
				}
				if(e.getKeyChar() == 'g')
				{
					Frame.this.world = new World(sizeX, sizeZ);
					repaint();
				}
				if(e.getKeyChar() == 's')
				{
					try
					{
						displayEntireMap = true;
						repaint();
						BufferedImage image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
						printAll(image.getGraphics());
						FileOutputStream fos = new FileOutputStream(System.getProperty("user.home") + "/Desktop/map.png");
						ImageIO.write(image, "png", fos);
						fos.close();
						
						displayEntireMap = false;
						repaint();
					}
					catch(Exception ex)
					{
						ex.printStackTrace();
					}
				}
			}
		});
	}
	
	@Override
	public void paint(Graphics g)
	{
		super.paint(g);
		g.clearRect(0, 0, getWidth(), getHeight());
		
		int sX = displayEntireMap ? 0 : scrollPane.getHorizontalScrollBar().getValue() / 2;
		int sY = displayEntireMap ? 0 : scrollPane.getVerticalScrollBar().getValue() / 2;
		
		int sW = displayEntireMap ? sizeX * 16 : Math.min(sX + scrollPane.getHorizontalScrollBar().getWidth(), sizeX * 16);
		int sH = displayEntireMap ? sizeZ * 16 : Math.min(sY + scrollPane.getVerticalScrollBar().getHeight(), sizeZ * 16);
		
		for(int x = sX; x < sW; x++)
		{
			for(int z = sY; z < sH; z++)
			{
				Biome biome = world.biomeAt(x, z);
				
				
				if(drawMode == 1)
				{
					if(biome != null)
					{
						g.setColor(biome.getColor());
					}
					else
					{
						g.setColor(Color.BLACK);
					}
				}
				else if(drawMode == 0)
				{
					float height = world.heightAt(x, z) / 255.0F;
					g.setColor(new Color(height, height, height));
				}
				else if(drawMode == 2)
				{
					float temp = world.tempAt(x, z);
					g.setColor(ColorUtil.lerp(winter, Color.RED, temp / 100.0F));
				}
				else if(drawMode == 3)
				{
					float moist = world.moistAt(x, z);
					g.setColor(ColorUtil.lerp(Color.YELLOW, Color.BLUE, moist / 100.0F));
				}
				g.fillRect(x * 2, z * 2, 2, 2);
			}
		}
		
		if(showGrid)
		{
			g.setColor(grid);
			for(int x = 0; x < sizeX; x++)
			{
				g.drawLine(x * 32, 0, x * 32, getHeight());
			}
			
			for(int z = 0; z < sizeZ; z++)
			{
				g.drawLine(0, z * 32, getWidth(), z * 32);
			}
		}
	}
}
