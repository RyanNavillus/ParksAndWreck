package game;

import com.polaris.engine.render.Texture;
import com.polaris.engine.render.TextureManager;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;

public class Car {
	
	private double posX, posY;
	
	private double velX, velY;
	
	private double[] carColors = new double[3];

	private Texture car;
	private Texture carFrame;

	private ArrayList<Double[]> fires = new ArrayList<>();

	private double rotation;

	public Car(double startX, double startY, double startVelX, double startVelY, double startRotation, TextureManager manager)
	{
		posX = startX;
		posY = startY;
		
		velX = startVelX;
		velY = startVelY;

		car = manager.getTexture("car");
		carFrame = manager.getTexture("carframe");

		carColors[0] = Math.random();
		carColors[1] = Math.random();
		carColors[2] = Math.random();

		//System.out.println(carColors[1] + " " + carColors[1] + " " + carColors[2]);

		rotation = startRotation;

		generateFire();
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
		GL11.glPushMatrix();
		GL11.glTranslatef((float) (posX + (27 * 2.5 / 2)), (float) (posY + (44 * 2.5 / 2)), 0);
		GL11.glRotatef((float) rotation++, 0, 0, 1);
		GL11.glTranslatef((float) -(posX + 27 * 2.5 / 2), (float) -(posY + (44 * 2.5 / 2)), 0);

		GL11.glEnable(GL11.GL_TEXTURE_2D);

		GL11.glColor3f((float) carColors[0], (float) carColors[1], (float) carColors[2]);

		car.bind();

		GL11.glBegin(GL11.GL_QUADS);

		GL11.glTexCoord2d(0, 0);
		GL11.glVertex2d(0 + posX, 0 + posY);
		GL11.glTexCoord2d(0, 1);
		GL11.glVertex2d(0 + posX, 44 * 2.5 + posY);
		GL11.glTexCoord2d(1, 1);
		GL11.glVertex2d(27 * 2.5 + posX, 44 * 2.5 + posY);
		GL11.glTexCoord2d(1, 0);
		GL11.glVertex2d(27 * 2.5 + posX, 0 + posY);

		GL11.glEnd();

		GL11.glColor3f(1.0f, 1.0f, 1.0f);

		carFrame.bind();

		GL11.glBegin(GL11.GL_QUADS);

		GL11.glTexCoord2d(0, 0);
		GL11.glVertex2d(0 + posX, 0 + posY);
		GL11.glTexCoord2d(0, 1);
		GL11.glVertex2d(0 + posX, 0 + 44 * 2.5 + posY);
		GL11.glTexCoord2d(1, 1);
		GL11.glVertex2d(0 + 27 * 2.5 + posX, 0 + 44 * 2.5 + posY);
		GL11.glTexCoord2d(1, 0);
		GL11.glVertex2d(0 + 27 * 2.5 + posX, 0 + posY);

		GL11.glEnd();


		GL11.glColor3f(1.0f, 1.0f, 1.0f);
		
		World.fireTexture.bind();
		
		GL11.glBegin(GL11.GL_QUADS);

		for (int i = 0; i < fires.size(); i++){
			//System.out.println("fire");
			double x = fires.get(i)[0];
			double y =  fires.get(i)[1];
			
			GL11.glTexCoord2d(0, 0);
			GL11.glVertex2d(0 + posX + x, 0 + posY + y);
			GL11.glTexCoord2d(0, 1);
			GL11.glVertex2d(0 + posX + x, 20 + posY + y);
			GL11.glTexCoord2d(1, 1);
			GL11.glVertex2d(20 + posX + x, 20 + posY + y);
			GL11.glTexCoord2d(1, 0);
			GL11.glVertex2d(20 + posX + x, 0 + posY + y);
		}
		GL11.glEnd();
		GL11.glDisable(GL11.GL_TEXTURE_2D);

		GL11.glPopMatrix();
	}

	private void generateFire(){
		double x = Math.random() * (27 * 2.5 - 20);
		double y = 50;

		while (y > 40 && y < 60)
			y = Math.random() * (44 * 2.5 - 20);

		fires.add(new Double[] {x, y});
	}
	
	public double getPosX()
	{
		return posX;
	}
	
	public double getPosY()
	{
		return posY;
	}

	public void setRotation(double rotation){
		this.rotation = rotation;
	}
}
