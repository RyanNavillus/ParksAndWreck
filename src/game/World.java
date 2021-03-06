package game;

import com.polaris.engine.render.Shader;
import com.polaris.engine.render.Texture;
import com.polaris.engine.render.TextureManager;

import java.io.File;
import sun.security.krb5.internal.crypto.crc32;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.dyn4j.collision.narrowphase.Penetration;
import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.dynamics.CollisionAdapter;
import org.dyn4j.dynamics.Force;
import org.dyn4j.geometry.Geometry;
import org.dyn4j.geometry.Mass;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Vector2;
import org.dyn4j.samples.SimulationBody;
import org.lwjgl.opengl.*;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;

/**
 * Created by Killian Le Clainche on 9/30/2017.
 */
public class World {
	public static Texture fireTexture;

	private static Shader colorShader;
	private static int texID;
	private static int windowSize;
	private static int trackFrameBuffer;
	private static int trackTexture;
	private static int renderBuffer;
	
	private static boolean boomMode = false;

	private float totalTime = 0;

	private Player[] players;
	
	private Date lastModeChange;

	private List<Car> staticCars;
	private Car[] playerCars;
	private int[] playerScores;
	private int[] displayScores;
	private List<Car> parkedCars;
	
	private List<ParkingSpot> parkingList;
	private ParkingSpot[] activeParking;

	private GameSettings gameSettings;
	private TextureManager textureManager;

	private org.dyn4j.dynamics.World physicsWorld;
	private double ticksToInitialize = 2;

	private static ArrayList<Oil> oils = new ArrayList<>();
	public static ArrayList<Track> tracks = new ArrayList<>();
	
	private boolean recentModeChange() {
		return (new Date().getTime() - lastModeChange.getTime()) < 200;
	}

	public World(GameSettings settings, TextureManager manager)
	{
		lastModeChange = new Date();
		staticCars = new ArrayList<>();
		playerCars = new Car[4];
		parkedCars = new ArrayList<>();

		parkingList = new ArrayList<>();
		activeParking = new ParkingSpot[4];
		playerScores = new int[4];
		displayScores = new int[4];

		players = new Player[] {new Player(), new Player(), new Player(), new Player()};

		gameSettings = settings;

		textureManager = manager;

		physicsWorld = new org.dyn4j.dynamics.World();
		physicsWorld.setGravity(org.dyn4j.dynamics.World.ZERO_GRAVITY);
		physicsWorld.addListener(new CustomCollision(this));

		fireTexture = textureManager.getTexture("fire0");

		parkingList.addAll(ParkingSpot.createParkingArea(200, 153, 9, 1));   //left
		parkingList.addAll(ParkingSpot.createParkingArea(1720 - ParkingSpot.HEIGHT, 153, 9, 3));   //right

		parkingList.addAll(ParkingSpot.createParkingArea(580, 200 + ParkingSpot.HEIGHT / 2, 9, 4));

		parkingList.addAll(ParkingSpot.createParkingArea(580, 880 - ParkingSpot.HEIGHT / 2 - ParkingSpot.HEIGHT, 9, 4));

		setupFrameBuffers();

		createWalls();

		if (playerCars[0] != null)
		{
			staticCars.add(playerCars[0]);
			staticCars.add(playerCars[1]);
			staticCars.add(playerCars[2]);
			staticCars.add(playerCars[3]);
		}
		Car car1 = new Car(-55, 110, 0, GuiGame.playerColors[0], textureManager);
		addPlayerCar(0, car1);
		assignParkingSpot(0, car1);
		Car car2 = new Car(-55, 970, 0, GuiGame.playerColors[1], textureManager);
		addPlayerCar(1, car2);
		assignParkingSpot(1, car2);
		Car car3 = new Car(1975, 110, 180, GuiGame.playerColors[2], textureManager);
		addPlayerCar(2, car3);
		assignParkingSpot(2, car3);
		Car car4 = new Car(1975, 970, 180, GuiGame.playerColors[3], textureManager);
		addPlayerCar(3, car4);
		assignParkingSpot(3, car4);

	}
	
	private static class DESTROY extends Body {
		
	}

	private static class CustomCollision extends CollisionAdapter {
		private World w;
		
		public CustomCollision(World w) {
			this.w = w;
		}

		@Override
		public boolean collision(Body body1, BodyFixture fixture1, Body body2, BodyFixture fixture2, Penetration penetration)
		{
			if (body1 instanceof DESTROY) {
				try {
				if (body2 instanceof Car) {
					w.explode((Car)body2);
				}
				}catch(Error e){}
			}
			if (body2 instanceof DESTROY) {
				try {
				if (body1 instanceof Car) {
					w.explode((Car)body1);
				}
				}catch(Error e){}
			}


			double damageSpeedThresh = 3;
			if (body1.getLinearVelocity().getMagnitude() > damageSpeedThresh ||
					body2.getLinearVelocity().getMagnitude() > damageSpeedThresh)
			{
				if (body1 instanceof Car) 
				{
					try {
					Car c1 = (Car)body1;
					c1.damageCar();
					if (boomMode) {
						Vector2 lv = body2.getLinearVelocity();
						if (!lv.isZero()) {
							c1.applyImpulse(lv.product(3));
						}
						if (c1.isDead()) w.explode(c1);
					}
					}
					catch (Error e)
					{}
				}
				if (body2 instanceof Car) {
					try {
					Car c2 = (Car)body2;
					c2.damageCar();
					if (boomMode) {
						Vector2 lv = body1.getLinearVelocity();
						if (!lv.isZero()) {
							c2.applyImpulse(lv.product(3));
						}
						if (c2.isDead()) w.explode(c2);
					}
					}
					catch (Error e)
					{}
				}
			}

			return true;
		}
	}

	private void addPlayerCar(int id, Car car)
	{
		playerCars[id] = car;
		physicsWorld.addBody(car);
	}
	
	private static boolean reverseMode = false;
	
	private void removePlayerCar(int id, Car car)
	{
		playerCars[id] = null;
		parkedCars.add(car);
	}
	
	private void explode(Car car) {
		boolean found = false;
		for (int i = 0; i < 4; i++) {
			if (car == playerCars[i]) {
				found = true;
		switch (i)
		{
			case 0: 
				Car newCar1 = new Car(-55, 110, 0, GuiGame.playerColors[0], textureManager);
				addPlayerCar(0, newCar1);
				activeParking[0].assignedCar = newCar1;
//				assignParkingSpot(0, newCar1);
				break;
			case 1:
				Car newCar2 = new Car(-55, 970, 0, GuiGame.playerColors[1], textureManager);
				addPlayerCar(1, newCar2);
				activeParking[1].assignedCar = newCar2;
//				assignParkingSpot(1, newCar2);
				break;
			case 2:
				Car newCar3 = new Car(1975, 110, 180, GuiGame.playerColors[2], textureManager);
				addPlayerCar(2, newCar3);
				activeParking[2].assignedCar = newCar3;
//				assignParkingSpot(2, newCar3);
				break;
			case 3:
				Car newCar4 = new Car(1975, 970, 180, GuiGame.playerColors[3], textureManager);
				addPlayerCar(3, newCar4);
				activeParking[3].assignedCar = newCar4;
//				assignParkingSpot(3, newCar4);
				break;
			default:
				break;
		}
			}
		}

		if (!found) {
			parkedCars.remove(car);
		}

		physicsWorld.removeBody(car);
	}

	public void update(double delta)
	{
		final double force = 20000 * delta;
		
		// mode changes
		if (!recentModeChange() && gameSettings.uKey.isPressed()) {
			boomMode = !boomMode;
			lastModeChange = new Date();
		}

		if (!recentModeChange() && gameSettings.iKey.isPressed()) {
			reverseMode = !reverseMode;
			lastModeChange = new Date();
		}

		if (playerCars[3] != null && !playerCars[3].isRecent())
		{
			if (gameSettings.goKey.isPressed())
			{
				playerCars[3].thrust(force);
			}

			if (reverseMode && gameSettings.stopKey.isPressed())
			{
				playerCars[3].thrust(-force);
			}

			if (gameSettings.rightKey.isPressed())
			{
				playerCars[3].myrotate(force);
			}

			if (gameSettings.leftKey.isPressed())
			{
				playerCars[3].myrotate(-force);
			}
		}

		if (playerCars[2] != null && !playerCars[3].isRecent())
		{
			if (gameSettings.go2Key.isPressed())
			{
				playerCars[2].thrust(force);
			}

			if (reverseMode && gameSettings.stop2Key.isPressed())
			{
				playerCars[2].thrust(-force);
			}

			if (gameSettings.right2Key.isPressed())
			{
				playerCars[2].myrotate(force);
			}

			if (gameSettings.left2Key.isPressed())
			{
				playerCars[2].myrotate(-force);
			}
		}
			
		for(int i = 0; i < playerCars.length; i++)
		{
			if(playerCars[i] != null && players[i].controller != null)
			{
				if (!playerCars[i].isRecent())
				{
					if (players[i].controller.dUpButtonPressed())
					{
						playerCars[0].thrust(force);
					}

					if (reverseMode && players[i].controller.dDownButtonPressed())
					{
						playerCars[0].thrust(-force);
					}

					if (players[i].controller.dLeftButtonPressed())
					{
						playerCars[0].myrotate(force);
					}

					if (players[i].controller.dRightButtonPressed())
					{
						playerCars[0].myrotate(-force);
					}
					
					if (players[i].controller.aButtonPressed())
					{
						playerCars[i].thrust(force);
					}

					if (reverseMode && players[i].controller.bButtonPressed())
					{
						playerCars[i].thrust(-force);
					}

					double angle = players[i].controller.getDirection();
					
					if(angle != 0.0)
					{
						angle = angle / 180 * Math.PI;
						
						playerCars[i].myrotate2(force, angle);
						
//						double rotation = playerCars[i].getTransform().getRotation();
//						if(rotation < 0)
//							rotation += Math.PI * 2;
//						
//						double travel1 = angle - rotation;
//						double travel2 = rotation + angle;
//						
//						System.out.println(playerCars[i].getTransform().getRotation());
//						
//						if(Math.abs(travel1) < Math.abs(travel2))
//						{
//							playerCars[i].rotateAboutCenter(Math.min(travel1, 8 * Math.PI * delta) - 2 * Math.PI);
//						}
//						else
//						{
//							playerCars[i].rotateAboutCenter(-Math.min(travel2, 8 * Math.PI * delta));
//						}
					}
				}
			}
		}


		for (Car p : playerCars)
		{
			if (p == null)
				continue;

			p.update(delta);
		}

		for (Car s : staticCars)
			s.update(delta);

		physicsWorld.update(delta);

		for (ParkingSpot spot : activeParking)
		{
			for (int i = 0; i < playerCars.length; i++)
			{
				Car car = playerCars[i];
				if (car != null && spot.containsCar(car))
				{
					if(spot.assignedCar == car) {
						if (car.parkingStartTime != null)
						{
							double timeInterval = ((new Date().getTime()) - car.parkingStartTime.getTime()) / 300.0; // Seconds spent in parking spot
							//System.out.println("Time interval: " + timeInterval + " :" + spot.id);
							if (timeInterval > 1)
							{
								System.out.println("Parked in spot: " + spot.id);
								spot.setFull(true);
								
								removePlayerCar(i, car);
								
								//Immobilize car
	//							Mass mas = new Mass(new Vector2(0, 0), 10000, )
	//							car.setMass(MassType.INFINITE);
								Mass oldMass = car.getMass();
								car.setMass(new Mass(oldMass.getCenter(), oldMass.getMass() * 10, oldMass.getInertia() * 10));
								car.setIAmStatic();
								
								car.setLinearVelocity(car.getLinearVelocity().product(0.01));
								car.setAngularVelocity(car.getAngularVelocity() * 0.01);
								car.setAngularDamping(car.getAngularDamping() * 5);
								car.setLinearDamping(car.getLinearDamping() * 5);
								//Increase score of car.player
								playerScores[i] += 10;
								
								//Spawn a new car for player
								switch (i)
								{
								case 0: 
									Car newCar1 = new Car(-55, 110, 0, GuiGame.playerColors[0], textureManager);
									addPlayerCar(0, newCar1);
									assignParkingSpot(0, newCar1);
									break;
								case 1:
									Car newCar2 = new Car(-55, 970, 0, GuiGame.playerColors[1], textureManager);
									addPlayerCar(1, newCar2);
									assignParkingSpot(1, newCar2);
									break;
								case 2:
									Car newCar3 = new Car(1975, 110, 180, GuiGame.playerColors[2], textureManager);
									addPlayerCar(2, newCar3);
									assignParkingSpot(2, newCar3);
									break;
								case 3:
									Car newCar4 = new Car(1975, 970, 180, GuiGame.playerColors[3], textureManager);
									addPlayerCar(3, newCar4);
									assignParkingSpot(3, newCar4);
									break;
								default:
									break;
								}
							}
						}
						else
						{
							car.parkingStartTime = new Date();
						}
					}
				}
				else
				{
					if (car != null && spot.assignedCar == car)
					{
						car.parkingStartTime = null;
					}
				}
			}
		}
	}

	public void render(double delta)
	{
		totalTime += delta;

		if (totalTime % .4 <= .2 && fireTexture.getName().equals("fire1")) {
			fireTexture = textureManager.getTexture("fire0");
		} else if (totalTime % .4 > .2 && fireTexture.getName().equals("fire0")) {
			fireTexture = textureManager.getTexture("fire1");
		}

		for (ParkingSpot parkingSpot : parkingList) {
			parkingSpot.render(delta);
		}

		renderTracks();

		GuiGame.renderScores(gameSettings, playerScores, displayScores);

		for(Track track : tracks){
			track.render(delta);
		}

		for(Oil oil : oils){
			oil.render(delta);
		}

		for(Car car : staticCars)
		{
			car.render(delta);
		}

		for (Car car : playerCars) 
		{
			if (car == null)
				continue;

			car.render(delta);
		}
		
		for (Car car : parkedCars) 
		{
			if (car == null)
			{
				continue;
			}
			car.render(delta);
		}
	}

	public Car[] getPlayerCars() {
		return playerCars;
	}

	public int[] getPlayerScores() {
		return playerScores;
	}

	public static ArrayList<Oil> getOils() {
		return oils;
	}

	public static ArrayList<Track> getTracks(){
		return tracks;
	}

	public static void renderTrack()
	{
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, trackFrameBuffer);
		glViewport(0, 0, 1920, 1080);

		GL11.glPushMatrix();
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, 1920, 1080, 0, -1, 1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);

		//RENDER THE TRACK HERE
		
		
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		glAlphaFunc(GL_GREATER, .05f);
		
		for( Track t : tracks){
			GL11.glPushMatrix();

			GL11.glTranslatef((float) (t.xPos + 16 / 2), (float) (t.yPos + 16 / 2), 0);
			GL11.glRotatef((float) (t.rotation * 180 / Math.PI - 90), 0, 0, 1);
			GL11.glTranslatef((float) -(t.xPos + 16 / 2), (float) -(t.yPos + 16 / 2), 0);

			Track.getTrackTextures()[t.texture].bind();
			
			GL11.glEnable(GL11.GL_BLEND);
			glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
			GL11.glColor4f(0, 0, 0, 1f);
			GL11.glBegin(GL11.GL_QUADS);
			GL11.glTexCoord2d(0, 0);
			GL11.glVertex2d(-8 + t.xPos, -8 + t.yPos);
			GL11.glTexCoord2d(0, 1);
			GL11.glVertex2d(-8 + t.xPos, 8 + t.yPos);
			GL11.glTexCoord2d(1, 1);
			GL11.glVertex2d(8 + t.xPos, 8 + t.yPos);
			GL11.glTexCoord2d(1, 0);
			GL11.glVertex2d(8 + t.xPos, -8 + t.yPos);
			GL11.glEnd();
			GL11.glDisable(GL11.GL_BLEND);


			GL11.glPopMatrix();
		}
		GL11.glDisable(GL11.GL_TEXTURE_2D);

		tracks = new ArrayList<>();

		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
	}

	private void renderTracks()
	{
		GL11.glEnable(GL11.GL_BLEND);
		
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, trackTexture);

		GL11.glBegin(GL11.GL_QUADS);
		GL11.glTexCoord2d(0, 1);
		GL11.glVertex2d(0, 0);
		GL11.glTexCoord2d(0, 0);
		GL11.glVertex2d(0, 1080);
		GL11.glTexCoord2d(1, 0);
		GL11.glVertex2d(1920, 1080);
		GL11.glTexCoord2d(1, 1);
		GL11.glVertex2d(1920, 0);
		GL11.glEnd();
		
		GL11.glDisable(GL11.GL_BLEND);
	}

	private void setupFrameBuffers()
	{
		colorShader = Shader.createShader(new File("shaders/normal.vert"), new File("shaders/normal.frag"));
		texID = GL20.glGetUniformLocation(colorShader.getShaderId(), "renderedTexture");
		windowSize = GL20.glGetUniformLocation(colorShader.getShaderId(), "window");

		trackFrameBuffer = GL30.glGenFramebuffers();
		trackTexture = GL11.glGenTextures();

		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, trackFrameBuffer);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, trackTexture);

		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, 1920, 1080, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, 0);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);

		GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0, GL11.GL_TEXTURE_2D, trackTexture, 0);

		GL20.glDrawBuffers(GL30.GL_COLOR_ATTACHMENT0);

		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);

		float[] g_quad_vertex_buffer_data = {
				0, 0, 0.0f,
				1920, 0, 0.0f,
				0,  1080, 0.0f,
				0,  1080, 0.0f,
				1920, 0, 0.0f,
				1920,  1080, 0.0f,
				};

		renderBuffer = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, renderBuffer);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, g_quad_vertex_buffer_data, GL15.GL_STATIC_DRAW);
	}

	private void createWalls()
	{
		double scale = Car.SCALE;

		double friction = 0.9;
		double bounce = 0.2;

		//left wall
		Body wall = new Body(1);
		wall.translate(100 / scale, (150 + (930 - 150) / 2) / scale);
		wall.addFixture(Geometry.createRectangle(200 / scale, (930 - 150) / scale), 20, friction, bounce);
		wall.setMass(MassType.INFINITE);

		physicsWorld.addBody(wall);

		//right wall
		wall = new Body(1);
		wall.translate(1820 / scale, (150 + (930 - 150) / 2) / scale);
		wall.addFixture(Geometry.createRectangle(200 / scale, (930 - 150) / scale), 20, friction, bounce);
		wall.setMass(MassType.INFINITE);

		physicsWorld.addBody(wall);

		//upper wall left
		wall = new Body(1);
		wall.translate(50 / scale, 25 / scale);
		wall.addFixture(Geometry.createRectangle(100 / scale, 50 / scale), 20, friction, bounce);
		wall.setMass(MassType.INFINITE);

		physicsWorld.addBody(wall);

		//upper wall middle

		wall = new Body(1);
		wall.translate((100 + 1720 / 2) / scale, (75 / 2) / scale);
		wall.addFixture(Geometry.createRectangle(1720 / scale, 75 / scale), 20, friction, bounce);
		wall.setMass(MassType.INFINITE);

		physicsWorld.addBody(wall);

		//upper wall right

		wall = new Body(1);
		wall.translate(1870 / scale, 25 / scale);
		wall.addFixture(Geometry.createRectangle(100 / scale, 50 / scale), 20, friction, bounce);
		wall.setMass(MassType.INFINITE);

		physicsWorld.addBody(wall);

		//lower wall left

		wall = new Body(1);
		wall.translate(50 / scale, 1055 / scale);
		wall.addFixture(Geometry.createRectangle(100 / scale, 50 / scale), 20, friction, bounce);
		wall.setMass(MassType.INFINITE);

		physicsWorld.addBody(wall);

		//lower wall middle

		wall = new Body(1);
		wall.translate((100 + 1720 / 2) / scale, (1080 - 75 / 2) / scale);
		wall.addFixture(Geometry.createRectangle(1720 / scale, 75 / scale), 20, friction, bounce);
		wall.setMass(MassType.INFINITE);

		physicsWorld.addBody(wall);

		//lower wall right

		wall = new Body(1);
		wall.translate((1920 - 50) / scale, 1055 / scale);
		wall.addFixture(Geometry.createRectangle(100 / scale, 50 / scale), 20, friction, bounce);
		wall.setMass(MassType.INFINITE);

		physicsWorld.addBody(wall);

		// destroy bounds

		double RIP = 0;
		double SHRED = 0;
		DESTROY DeStRoY;
		DeStRoY = new DESTROY();
		DeStRoY.translate((1920 + 400) / scale, -800 / scale);
		DeStRoY.addFixture(Geometry.createRectangle(400 / scale, 1920 + 1600 / scale), 1, RIP, SHRED);
		DeStRoY.setMass(MassType.INFINITE);

		physicsWorld.addBody(DeStRoY);

		DeStRoY = new DESTROY();
		DeStRoY.translate((-400) / scale, -800 / scale);
		DeStRoY.addFixture(Geometry.createRectangle(400 / scale, 1920 + 1600 / scale), 1, RIP, SHRED);
		DeStRoY.setMass(MassType.INFINITE);

		physicsWorld.addBody(DeStRoY);

//		DeStRoY = new DESTROY();
//		DeStRoY.translate((-400) / scale, -800 / scale);
//		DeStRoY.addFixture(Geometry.createRectangle(400 / scale, 1920 + 1600 / scale), 1, RIP, SHRED);
//		DeStRoY.setMass(MassType.INFINITE);
//
//		physicsWorld.addBody(DeStRoY);
//
//		DeStRoY = new DESTROY();
//		DeStRoY.translate((-400) / scale, -800 / scale);
//		DeStRoY.addFixture(Geometry.createRectangle(400 / scale, 1920 + 1600 / scale), 1, RIP, SHRED);
//		DeStRoY.setMass(MassType.INFINITE);
//
//		physicsWorld.addBody(DeStRoY);
	}

	private void assignParkingSpot(int id, Car car) {
		if(ParkingSpot.fullCount == ParkingSpot.count)
		{
			System.out.println("All spots filled");
		}
		
		int index = (int) (Math.random() * parkingList.size());
		ParkingSpot spot = parkingList.get(index);
		
		while(spot.getFull() || spot.getAssigned())
		{
			index = (int) (Math.random() * parkingList.size());
			spot = parkingList.get(index);
		}
		activeParking[id] = spot;
		spot.assignColor((float)car.getCarColors()[0], (float)car.getCarColors()[1], (float)car.getCarColors()[2]);
		spot.assignedCar = car;
		
	}
	
}
