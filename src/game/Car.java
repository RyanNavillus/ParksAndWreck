package game;

public class Car {
	
	private double posX, posY;
	
	private double velX, velY;
	
	private double[] carColors;
	
	public Car(double startX, double startY, double startVelX, double startVelY, double[] colors)
	{
		posX = startX;
		posY = startY;
		
		velX = startVelX;
		velY = startVelY;
		
		carColors = colors;
	}
	
	public void update(double delta)
	{
	
	}
	
	public void render(double delta)
	{
	
	}
	
	public double getPosX()
	{
		return posX;
	}
	
	public double getPosY()
	{
		return posY;
	}
}
