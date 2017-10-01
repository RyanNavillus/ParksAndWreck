package game;

import com.polaris.engine.render.Texture;
import com.polaris.engine.render.TextureManager;

import org.dyn4j.collision.narrowphase.Penetration;
import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.dynamics.CollisionAdapter;
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

	public static final double SCALE = 32.0;
	private static final double width = 44 * 2.2, height = 27 * 2.2;
	private static final double halfWidth = 22 * 2.2, halfHeight = 13.5 * 2.2;
	
	private double[] carColors = new double[3];
	
	private boolean isSMatic = false;
	
	private Texture car;
	private Texture carFrame;
	private Texture carFrameBroke;

	private ArrayList<Double[]> fires = new ArrayList<>();
	
	public Date madeTime;
	public Date hitTime;
	public Date parkingStartTime;
	public int parkingSpotId;
	
	private boolean broken;
	private boolean leaking;

	private int counter = 0;
	
	public boolean isRecent()
	{
		return (new Date().getTime() - madeTime.getTime()) < 500;
	}

	private int health;

	public Car(double startX, double startY, double startRotation, double[] carColors, TextureManager manager)
	{
		super();
		
		hitTime = new Date();
		madeTime = new Date();

		double rotation = startRotation / 180 * Math.PI;
		
		this.translate(new Vector2(startX, startY).product(1/SCALE));
		this.rotateAboutCenter(rotation);
		Vector2 initVel = new Vector2(Math.cos(rotation), Math.sin(rotation)).multiply(75);
		setLinearVelocity(initVel);
//		this.getLinearVelocity().multiply(0);
		
		setLinearDamping(3.5);
		setAngularDamping(1.3);
		
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

		health = 100;

		if(broken){
			for (int i = 0; i < 17; i++) {
				damageCar();
			}
		}
	}
	
	public double getX() {
		return this.getWorldCenter().x * SCALE;
	}

	public double getY() {
		return this.getWorldCenter().y * SCALE;
	}
	
	public void setIAmStatic() {
		this.isSMatic = true;
	}
	
	public boolean iAmStatic() {
		return this.isSMatic;
	}
	
	public void update(double delta)
	{

		if (!iAmStatic()) {
			if(counter == 5){
				double angle = this.getTransform().getRotation();
				World.getTracks().add(new Track(getX() + halfWidth/SCALE - 20 * Math.abs(Math.sin(angle)), getY() + halfHeight/SCALE - 20 * Math.abs(Math.cos(angle)), angle, (int) (Math.random() * 6.0)));
				World.getTracks().add(new Track(getX() + halfWidth/SCALE + 10 * Math.abs(Math.sin(angle)), getY() + halfHeight/SCALE + 10 * Math.abs(Math.cos(angle)), angle, (int) (Math.random() * 6.0)));
				counter = 0;
			} else {
				counter++;
			}
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
		GL11.glRotatef((float) Math.ceil((rotation * 180 / Math.PI) / 9.0) * 9.0f, 0, 0, 1);


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
		double x = 50;
		double y = Math.random() * (height - 30) + 5;

		while (x > 30 && x < 70)
			x = Math.random() * (width - 30) + 5;

		System.out.println(x + " " + y);

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
        
        Vector2 orthV = new Vector2(this.getLinearVelocity()).project(new Vector2(r));
//        System.out.println(orthV);
        
        double mag = orthV.getMagnitude();
        mag = Math.max(mag, 3);
        
        Vector2 f1 = r.product(force * mag / 100).right();
        Vector2 f2 = r.product(force * mag / 400).left();
        Vector2 p1 = c.sum(r.product(0.9));
        Vector2 p2 = c.sum(r.product(-0.9));
        	
        // apply a force to the top going left
        this.applyForce(f1, p1);
        // apply a force to the bottom going right
        this.applyForce(f2, p2);
	}
	
	public void myrotate2(double force, double iniangle) {
        final Vector2 r = new Vector2(this.getTransform().getRotation() + Math.PI * 0.5).left().getNormalized().product(1);
        
        Vector2 right = new Vector2(1, 0).rotate(-iniangle);

        double angle = r.getAngleBetween(right);

		double limit = Math.PI / 3;
		if (angle > limit) angle = limit;
		if (angle < -limit) angle = -limit;
		angle = angle / limit;
        System.out.println(angle);
		myrotate(force * angle);
	}
	
	public boolean recentlyHit() {
		return (new Date().getTime() - hitTime.getTime()) < 200;
	}

	public void damageCar(){
		if (recentlyHit()) {
			return;
		}
		hitTime = new Date();
		
		health -= 4;

		if (health < 0)
			health = 0;

		if (health < 50)
			broken = true;

		while (5 - Math.ceil(health / 20.0) > fires.size()){
			generateFire();
		}
	}
}

