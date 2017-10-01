package game;

import com.polaris.engine.render.Texture;
import com.polaris.engine.render.TextureManager;

import org.dyn4j.dynamics.Body;
import org.dyn4j.geometry.Geometry;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Rectangle;
import org.dyn4j.geometry.Vector2;
import org.dyn4j.samples.SimulationBody;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Car extends Body
{
	private static final double SCALE = 32.0;
	private static final double width = 44 * 2.2, height = 27 * 2.2;
	private static final double halfWidth = 22 * 2.2, halfHeight = 13.5 * 2.2;
	
	private double[] carColors = new double[3];
	
	private Texture car;
	private Texture carFrame;
	private Texture carFrameBroke;

	private ArrayList<Double[]> fires = new ArrayList<>();
	
	public Date parkingStartTime;
	public int parkingSpotId;
	
	private boolean broken;
	private boolean leaking;

	private int counter = 0;

	public Car(double startX, double startY, double startRotation, double[] carColors, TextureManager manager)
	{
		super();

		double rotation = startRotation / 180 * Math.PI;
		
		this.translate(new Vector2(startX + 100, startY + 100).product(1/SCALE));
//		this.rotateAboutCenter(rotation);
//		Vector2 initVel = new Vector2(Math.cos(rotation), Math.sin(rotation));
//		setLinearVelocity(initVel);
		this.getLinearVelocity().multiply(0);
		
		setLinearDamping(2);
		setAngularDamping(0.5);
		
		double friction = 0.0;
		double bounce = 0.2;
		
		// might have to switch width and height
		addFixture(Geometry.createRectangle(width/SCALE, height/SCALE),  1, friction, bounce);

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

		broken = Math.random() * 9 < 1;
		leaking = Math.random() * 10 < 1 || broken;
	}
	
	public double getX() {
		return this.getWorldCenter().x * SCALE;
	}

	public double getY() {
		return this.getWorldCenter().y * SCALE;
	}
	
	public void update(double delta)
	{
		//setLinearVelocity(Math.cos(this.getTransform().getRotation()) * 1000, Math.sin(this.getTransform().getRotation()) * 1000);

		if(counter == 5){
			double angle = this.getTransform().getRotation();
			World.getTracks().add(new Track(this.getTransform().getTranslationX() + halfWidth - 20 * Math.abs(Math.sin(angle)), this.getTransform().getTranslationY() + halfHeight - 20 * Math.abs(Math.cos(angle)), angle, (int) (Math.random() * 6.0)));
			World.getTracks().add(new Track(this.getTransform().getTranslationX() + halfWidth + 10 * Math.abs(Math.sin(angle)), this.getTransform().getTranslationY() + halfHeight + 10 * Math.abs(Math.cos(angle)), angle, (int) (Math.random() * 6.0)));
			counter = 0;
		} else {
			counter++;
		}

		if (leaking && Math.random() * 6 < 1){
			double x = getX() + halfWidth/SCALE;
			double y = getY() + halfWidth/SCALE;

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
		}
	}
	
	public void render(double delta)
	{
		
		double posX = this.getX();
		double posY = this.getY();
		double rotation = this.getTransform().getRotation();
		
		GL11.glPushMatrix();
		
		GL11.glTranslatef((float) (posX), (float) (posY), 0);
		GL11.glRotatef((float) (rotation * 180 / Math.PI), 0, 0, 1);


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


		for (Double[] d : fires){
			double x = d[0];
			double y =  d[1];

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
		double x = Math.random() * (width - 30) + 5;
		double y = 70;

		while (y > (.3636 * height) && y < (.3636 * 2 * height))
			y = Math.random() * (height - 30) + 5;

		fires.add(new Double[] {x, y});
	}

	public void setBroken(boolean broken){
		this.broken = broken;
	}

	public double[] getCarColors(){
		return carColors;
	}

	public void thrust(double force) {
        final Vector2 r = new Vector2(this.getTransform().getRotation() + Math.PI * 0.5).left();
       	Vector2 f = r.product(force);
       	applyForce(f);
	}

	public void myrotate(double force) {
        final Vector2 r = new Vector2(this.getTransform().getRotation() + Math.PI * 0.5).left();
        final Vector2 c = this.getWorldCenter();

        Vector2 f1 = r.product(force * 0.05).right();
        Vector2 f2 = r.product(force * 0.05).left();
        Vector2 p1 = c.sum(r.product(0.9));
        Vector2 p2 = c.sum(r.product(-0.9));
        	
        // apply a force to the top going left
        this.applyForce(f1, p1);
        // apply a force to the bottom going right
        this.applyForce(f2, p2);
	}
}

