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

	private List<Car> staticCars;
	private Car[] playerCars;
	private int[] playerScores;
	private int[] displayScores;
	private List<Car> parkedCars;
	
	private List<ParkingSpot> parkingList;

	private GameSettings gameSettings;
	private TextureManager textureManager;

	private org.dyn4j.dynamics.World physicsWorld;
	private double ticksToInitialize = 2;

	private static ArrayList<Oil> oils = new ArrayList<>();
	public static ArrayList<Track> tracks = new ArrayList<>();

	public World(GameSettings settings, TextureManager manager)
	{
		staticCars = new ArrayList<>();
		playerCars = new Car[4];
		parkedCars = new ArrayList<>();

		parkingList = new ArrayList<>();
		playerScores = new int[4];
		displayScores = new int[4];

		players = new Player[] {new Player(), new Player(), new Player(), new Player()};

		gameSettings = settings;

		textureManager = manager;

		physicsWorld = new org.dyn4j.dynamics.World();
		physicsWorld.setGravity(org.dyn4j.dynamics.World.ZERO_GRAVITY);

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

		addPlayerCar(0, new Car(-55, 110, 0, GuiGame.playerColors[0], textureManager));
		addPlayerCar(1, new Car(-55, 970, 0, GuiGame.playerColors[1], textureManager));
		addPlayerCar(2, new Car(1975, 110, 180, GuiGame.playerColors[2], textureManager));
		addPlayerCar(3, new Car(1975, 970, 180, GuiGame.playerColors[3], textureManager));
	}

	private static class CustomCollision extends CollisionAdapter {

		@Override
		public boolean collision(Body body1, BodyFixture fixture1, Body body2, BodyFixture fixture2, Penetration penetration)
		{
			double damageSpeedThresh = 4;
			if (body1.getLinearVelocity().getMagnitude() > damageSpeedThresh ||
					body2.getLinearVelocity().getMagnitude() > damageSpeedThresh)
			{
				if (body1 instanceof Car) 
				{
					((Car)body1).damageCar();
					if (boomMode) {
						body1.applyImpulse(body2.getLinearVelocity().product(3));
					}
				}
				if (body2 instanceof Car) {
					((Car)body2).damageCar();
					if (boomMode) {
						body2.applyImpulse(body1.getLinearVelocity().product(3));
					}
				}
			}

			return true;
		}
	}

	private void addPlayerCar(int id, Car car)
	{
		playerCars[id] = car;
		physicsWorld.addBody(car);
		physicsWorld.addListener(new CustomCollision());
	}
	
	private void removePlayerCar(int id, Car car)
	{
		playerCars[id] = null;
		parkedCars.add(car);
		//physicsWorld.removeBody(car);
	}

	public void update(double delta)
	{
		final double force = 20000 * delta;
		
		if (gameSettings.uKey.isPressed()) {
			boomMode = true;
		}

		if (playerCars[3] != null && !playerCars[3].isRecent())
		{
			if (gameSettings.goKey.isPressed())
			{
				playerCars[3].thrust(force);
			}

			if (gameSettings.stopKey.isPressed())
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

			if (gameSettings.stop2Key.isPressed())
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
					if (playerCars[0] != null && !playerCars[0].isRecent())
					{
						if (players[i].controller.dUpButtonPressed())
						{
							playerCars[0].thrust(force);
						}

						if (players[i].controller.dDownButtonPressed())
						{
							playerCars[0].thrust(-force);
						}

						if (players[i].controller.dRightButtonPressed())
						{
							playerCars[0].myrotate(force);
						}

						if (players[i].controller.dLeftButtonPressed())
						{
							playerCars[0].myrotate(-force);
						}
					}

					if (players[i].controller.aButtonPressed())
					{
						playerCars[i].thrust(force);
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

		for (ParkingSpot spot : parkingList)
		{
			for (int i = 0; i < playerCars.length; i++)
			{
				Car car = playerCars[i];
				if (car != null && spot.containsCar(car))
				{
					if(spot.id == car.parkingSpotId)
					{
						double timeInterval = ((new Date().getTime()) - car.parkingStartTime.getTime()) / 1000.0; // Seconds spent in parking spot
						//System.out.println("Time interval: " + timeInterval + " :" + spot.id);
						if (timeInterval > 1)
						{
							System.out.println("Parked in spot: " + spot.id);
							
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
								addPlayerCar(0, new Car(-55, 110, 0, GuiGame.playerColors[0], textureManager));
								break;
							case 1:
								addPlayerCar(1, new Car(-55, 970, 0, GuiGame.playerColors[1], textureManager));
								break;
							case 2:
								addPlayerCar(2, new Car(1975, 110, 180, GuiGame.playerColors[2], textureManager));
								break;
							case 3:
								addPlayerCar(3, new Car(1975, 970, 180, GuiGame.playerColors[3], textureManager));
								break;
							default:
								break;
							}
						}
					}
					else
					{
						car.parkingSpotId = spot.id;
						car.parkingStartTime = new Date();
					}
				}
				else
				{
					if (car != null && spot.id == car.parkingSpotId)
					{
						car.parkingSpotId = 0;
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
	}

}
