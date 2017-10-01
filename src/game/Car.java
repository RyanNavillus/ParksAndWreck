package game;

import com.polaris.engine.render.Texture;
import com.polaris.engine.render.TextureManager;

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
import java.util.List;

public class Car extends SimulationBody {
	
	private static final double height = 44 * 2.5, width = 27 * 2.5;
	private static final double halfHeight = 22 * 2.5, halfWidth = 13.5 * 2.5;
	
	private double[] carColors = new double[3];

	private Texture car;
	private Texture carFrame;
	private Texture carFrameBroke;

	private ArrayList<Double[]> fires = new ArrayList<>();
	
	private boolean broken;
	private boolean leaking;

	public Car(double startX, double startY, double startRotation, double[] carColors, TextureManager manager)
	{
		super();

		this.translate(new Vector2(startX, startY));
		this.rotateAboutCenter(startRotation / 180 * Math.PI);
		setLinearVelocity(Math.cos(startRotation / 180 * Math.PI) * 1000, Math.sin(startRotation / 180 * Math.PI) * 1000);
		
		double friction = 0.0;
		double bounce = 0.2;
		
		double posX = this.getTransform().getTranslationX();
		double posY = this.getTransform().getTranslationY();
		double rotation = this.getTransform().getRotation();
		// might have to switch height and width
		addFixture(Geometry.createRectangle(height, width),  1, friction, bounce);

		// this may or may not need to be changed
//		this.translate(0.0, 2.0);
		setMass(MassType.NORMAL);

		car = manager.getTexture("car");
		carFrame = manager.getTexture("carframe");
		carFrameBroke = manager.getTexture("carframeBroke");

		this.carColors = carColors;

//		this.setAngularDamping(10.0f);
//		this.setLinearDamping(3.0f);

		//for (int i = 0; i < 5; i++)
		//	generateFire();

		broken = false;
		leaking = Math.random() * 10 < 1;
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
		setLinearVelocity(Math.cos(this.getTransform().getRotation()) * 1000, Math.sin(this.getTransform().getRotation()) * 1000);

		if (leaking){
			double x = this.getTransform().getTranslationX() + halfWidth;
			double y = this.getTransform().getTranslationY() + halfHeight;
			boolean grew = false;

			for(Oil oil : World.getOils()){
				if (Math.abs(x - oil.getxPos()) < oil.scale && Math.abs(y - oil.getyPos()) < oil.scale ){
					oil.grow(5);
					grew = true;

					break;
				}
			}

			if (!grew)
				World.getOils().add(new Oil(x, y, Math.random() * 360));

			System.out.println(World.getOils().size());
		}
	}
	
	public void render(double delta)
	{
		
		double posX = this.getTransform().getTranslationX();
		double posY = this.getTransform().getTranslationY();
		double rotation = this.getTransform().getRotation();
		
		GL11.glPushMatrix();
		
		GL11.glTranslatef((float) (posX + halfWidth), (float) (posY + halfHeight), 0);
		GL11.glRotatef((float) (rotation * 180 / Math.PI - 90), 0, 0, 1);


		GL11.glEnable(GL11.GL_TEXTURE_2D);

		GL11.glColor3f((float) carColors[0], (float) carColors[1], (float) carColors[2]);

		car.bind();

		GL11.glBegin(GL11.GL_QUADS);

		GL11.glTexCoord2d(0, 0);
		GL11.glVertex2d(-halfWidth, -halfHeight);
		GL11.glTexCoord2d(0, 1);
		GL11.glVertex2d(-halfWidth, halfHeight);
		GL11.glTexCoord2d(1, 1);
		GL11.glVertex2d(halfWidth, halfHeight);
		GL11.glTexCoord2d(1, 0);
		GL11.glVertex2d(halfWidth, -halfHeight);

		GL11.glEnd();

		GL11.glColor3f(1.0f, 1.0f, 1.0f);

		if (!broken){
			carFrame.bind();
		} else {
			carFrameBroke.bind();
		}

		GL11.glBegin(GL11.GL_QUADS);
		
		GL11.glTexCoord2d(0, 0);
		GL11.glVertex2d(-halfWidth, -halfHeight);
		GL11.glTexCoord2d(0, 1);
		GL11.glVertex2d(-halfWidth, halfHeight);
		GL11.glTexCoord2d(1, 1);
		GL11.glVertex2d(halfWidth, halfHeight);
		GL11.glTexCoord2d(1, 0);
		GL11.glVertex2d(halfWidth, -halfHeight);

		GL11.glEnd();


		GL11.glColor3f(1.0f, 1.0f, 1.0f);
		
		World.fireTexture.bind();
		
		GL11.glBegin(GL11.GL_QUADS);


		for (int i = 0; i < fires.size(); i++){
			double x = fires.get(i)[0];
			double y =  fires.get(i)[1];

			GL11.glTexCoord2d(0, 0);
			GL11.glVertex2d(-halfWidth + x, -halfHeight + y);
			GL11.glTexCoord2d(0, 1);
			GL11.glVertex2d(-halfWidth + x, -halfHeight + y + 20);
			GL11.glTexCoord2d(1, 1);
			GL11.glVertex2d(-halfWidth + x + 20, -halfHeight + y + 20);
			GL11.glTexCoord2d(1, 0);
			GL11.glVertex2d(-halfWidth + x + 20, -halfHeight + y);
		}
		
		GL11.glEnd();
		
		GL11.glDisable(GL11.GL_TEXTURE_2D);

		GL11.glPopMatrix();
	}

	private void generateFire(){
		double x = Math.random() * (27 * 2.5 - 30) + 5;
		double y = 70;

		while (y > 40 && y < 80)
			y = Math.random() * (44 * 2.5 - 30) + 5;

		fires.add(new Double[] {x, y});
	}

	public void setBroken(boolean broken){
		this.broken = broken;
	}

	public double[] getCarColors(){
		return carColors;
	}
}
