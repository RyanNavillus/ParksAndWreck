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
	
	/*
	 GL11.glPushMatrix();
	 GL11.glTranslate3f(CENTER_X, CENTER_Y, CENTER_Z);
	 GL11.glRotate3f(ROTATION, ROTATION_X, ROTATION_Y, ROTATION_Z);
	 RENDER WHEELS
	 RENDER CAR + COLOR
	 SETUP FOR RENDERING FIRE ANIMATION ON TOP OF CARS
	GL11.glPopMatrix();
	 
	 */
	
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
