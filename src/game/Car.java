package game;

import com.polaris.engine.render.Texture;
import com.polaris.engine.render.TextureManager;
import com.sun.xml.internal.bind.v2.schemagen.xmlschema.List;

import org.dyn4j.collision.Fixture;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.dynamics.Force;
import org.dyn4j.geometry.Geometry;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Rectangle;
import org.dyn4j.geometry.Vector2;
import org.dyn4j.samples.SimulationBody;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;

public class Car {
	
	private double posX, posY, height, width;
	
	private double velX, velY;
	
	private double[] carColors = new double[3];

	private Texture car;
	private Texture carFrame;

	private ArrayList<Double[]> fires = new ArrayList<>();

	public SimulationBody simulationBody;
	
	private double rotation;

	public Car(double startX, double startY, double startVelX, double startVelY, double startRotation, TextureManager manager)
	{
		posX = startX;
		posY = startY;
		
		velX = startVelX;
		velY = startVelY;

		car = manager.getTexture("car");
		carFrame = manager.getTexture("carframe");

		carColors[0] = 1 - Math.random()/2;
		carColors[1] = 1 - Math.random()/2;
		carColors[2] = 1 - Math.random()/2;

		//System.out.println(carColors[1] + " " + carColors[1] + " " + carColors[2]);

		//rotation = startRotation;

		simulationBody = new SimulationBody();
		simulationBody.addFixture(Geometry.createRectangle(80, 120),  1, 0.2, 0.2);
		simulationBody.setMass(MassType.NORMAL);
		
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
		java.util.List<BodyFixture> carFixtures = simulationBody.getFixtures();
		if (simulationBody.getFixtureCount() > 0)
		{
			BodyFixture carFixture = carFixtures.get(0);
			Rectangle carFrame = (Rectangle) carFixture.getShape(); //Will crash if car is not a rectangle
			Vector2[] vertices = carFrame.getVertices();
			double minX = Double.MAX_VALUE;
			double minY = Double.MAX_VALUE;
			double maxX = 0;
			double maxY = 0;
			for (Vector2 vector2 : vertices)
			{
				if (vector2.x < minX)
				{
					minX = vector2.x;
				}
				if (vector2.x > maxX)
				{
					maxX = vector2.x;
				}
				if (vector2.y < minY)
				{
					minY = vector2.y;
				}
				if (vector2.y > maxY)
				{
					maxY = vector2.y;
				}
			}
			posX = minX;
			posY = minY;
			height = maxY - minY;
			width = maxX - minX;
			rotation = carFrame.getRotation();
		}
	}
	
	public void render(double delta)
	{
		System.out.println("2 posX: " + posX + "; posY: " + posY + "; rotation: " + rotation + "; height: " + height + "; width: " + width); 
		GL11.glPushMatrix();
		GL11.glTranslatef((float) (posX + (width / 2)), (float) (posY + (height / 2)), 0);
		GL11.glRotatef((float) rotation, 0, 0, 1);
		GL11.glTranslatef((float) -(posX + width / 2), (float) -(posY + (height / 2)), 0);

		GL11.glEnable(GL11.GL_TEXTURE_2D);

		GL11.glColor3f((float) carColors[0], (float) carColors[1], (float) carColors[2]);

		car.bind();

		GL11.glBegin(GL11.GL_QUADS);

		GL11.glTexCoord2d(0, 0);
		GL11.glVertex2d(0 + posX, 0 + posY);
		GL11.glTexCoord2d(0, 1);
		GL11.glVertex2d(0 + posX, height + posY);
		GL11.glTexCoord2d(1, 1);
		GL11.glVertex2d(width + posX, height + posY);
		GL11.glTexCoord2d(1, 0);
		GL11.glVertex2d(width + posX, 0 + posY);

		GL11.glEnd();

		GL11.glColor3f(1.0f, 1.0f, 1.0f);

		carFrame.bind();

		GL11.glBegin(GL11.GL_QUADS);

		GL11.glTexCoord2d(0, 0);
		GL11.glVertex2d(0 + posX, 0 + posY);
		GL11.glTexCoord2d(0, 1);
		GL11.glVertex2d(0 + posX, 0 + height + posY);
		GL11.glTexCoord2d(1, 1);
		GL11.glVertex2d(0 + width + posX, 0 + height + posY);
		GL11.glTexCoord2d(1, 0);
		GL11.glVertex2d(0 + width + posX, 0 + posY);

		GL11.glEnd();

		GL11.glDisable(GL11.GL_TEXTURE_2D);

		GL11.glColor3f(1.0f, 1.0f, 0.0f);

		for (int i = 0; i < fires.size(); i++){
			//System.out.println("fire");
			double x = fires.get(i)[0];
			double y =  fires.get(i)[1];

			GL11.glBegin(GL11.GL_QUADS);
			GL11.glVertex2d(0 + posX + x, 0 + posY + y);
			GL11.glVertex2d(0 + posX + x, 20 + posY + y);
			GL11.glVertex2d(20 + posX + x, 20 + posY + y);
			GL11.glVertex2d(20 + posX + x, 0 + posY + y);
			GL11.glEnd();
		}

		GL11.glColor3f(1.0f, 1.0f, 1.0f);

		GL11.glBegin(GL11.GL_QUADS);
		GL11.glVertex2d(0 + posX, 60 + posY);
		GL11.glVertex2d(0 + posX, 80 + posY);
		GL11.glVertex2d(width + posX, 80 + posY);
		GL11.glVertex2d(width + posX, 60 + posY);
		GL11.glEnd();

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
