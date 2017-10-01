package game;

import com.polaris.engine.render.Texture;
import com.polaris.engine.render.TextureManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.Force;
import org.dyn4j.geometry.Geometry;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Vector2;
import org.dyn4j.samples.SimulationBody;

/**
 * Created by Killian Le Clainche on 9/30/2017.
 */
public class World
{
	public static Texture fireTexture;
	
	private float totalTime = 0;
	
	private Player[] players;
	
	private List<Car> staticCars;
	private Car[] playerCars;
	private int[] playerScores;
	
	private List<ParkingSpot> parkingList;
	
	private GameSettings gameSettings;
	private TextureManager textureManager;
		
	private org.dyn4j.dynamics.World physicsWorld;
	private double ticksToInitialize = 2;

	private static ArrayList<Oil> oils = new ArrayList<>();

	public World(GameSettings settings, TextureManager manager)
	{
		staticCars = new ArrayList<>();
		playerCars = new Car[4];
		parkingList = new ArrayList<>();
		playerScores = new int[4];
		
		players = new Player[] {new Player(), new Player(), new Player(), new Player()};
		
		gameSettings = settings;
		
		textureManager = manager;
		
		physicsWorld = new org.dyn4j.dynamics.World();
		physicsWorld.setGravity(org.dyn4j.dynamics.World.ZERO_GRAVITY);
				
		fireTexture = textureManager.getTexture("fire0");

		parkingList.addAll(ParkingSpot.createParkingArea(200, 153, 10, 1));   //left
		parkingList.addAll(ParkingSpot.createParkingArea(1720 - ParkingSpot.HEIGHT, 153, 10, 3));   //right

		parkingList.addAll(ParkingSpot.createParkingArea(580, 200 + ParkingSpot.HEIGHT / 2, 9, 4));

		parkingList.addAll(ParkingSpot.createParkingArea(580, 880 - ParkingSpot.HEIGHT / 2 - ParkingSpot.HEIGHT, 9, 4));
		
		createWalls();
	}
	
	private void addPlayerCar(int id, Car car)
	{
		playerCars[id] = car;
		physicsWorld.addBody(car);
	}
	
	public void update(double delta)
	{
		if((ticksToInitialize -= delta) <= 0)
		{
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
			
			ticksToInitialize = 10;
		}
		
		if(playerCars[0] != null && players[0].controller != null && players[0].controller.getDirection() != Double.NaN)
		{
			//System.out.println(players[0].controller.getDirection() + " BLALDSLDASL " + )
			playerCars[0].rotateAboutCenter(-(players[0].controller.getDirection() / 180 * Math.PI) - playerCars[0].getTransform().getRotation());
		}
		
		for(Car p : playerCars){
			if (p == null)
				continue;

			p.update(delta);
		}
		
		for(Car s : staticCars)
			s.update(delta);
		
		physicsWorld.update(delta);
	}
	
	public void render(double delta)
	{
		totalTime += delta;
		
		if(totalTime % .4 <= .2 && fireTexture.getName().equals("fire1"))
		{
			fireTexture = textureManager.getTexture("fire0");
		}
		else if(totalTime % .4 > .2 && fireTexture.getName().equals("fire0"))
		{
			fireTexture = textureManager.getTexture("fire1");
		}
		
		for(ParkingSpot parkingSpot : parkingList)
		{
			parkingSpot.render(delta);
		}

		for(Oil oil : oils){
			oil.render(delta);
		}
		
		for(Car car : staticCars)
		{
			car.render(delta);
		}
		
		for(Car car : playerCars)
		{
			if (car == null)
				continue;

			car.render(delta);
		}
	}

	public Car[] getPlayerCars(){
		return playerCars;
	}

	public int[] getPlayerScores(){
		return playerScores;
	}

	public static ArrayList<Oil> getOils(){
		return oils;
	}
	
	private void createWalls()
	{
		//left wall
		Body wall = new Body(1);
		wall.translate(100, 150 + (930 - 150) / 2);
		wall.addFixture(Geometry.createRectangle(200, 930 - 150), 20, 1, 1);
		wall.setMass(MassType.INFINITE);
		
		physicsWorld.addBody(wall);
		
		//right wall
		wall = new Body(1);
		wall.translate(1820, 150 + (930 - 150) / 2);
		wall.addFixture(Geometry.createRectangle(200, 930 - 150), 20, 1, 1);
		wall.setMass(MassType.INFINITE);
		
		physicsWorld.addBody(wall);
		
		
		//upper wall left
		wall = new Body(1);
		wall.translate(50, 25);
		wall.addFixture(Geometry.createRectangle(100, 50), 20, 1, 1);
		wall.setMass(MassType.INFINITE);
		
		physicsWorld.addBody(wall);
		
		//upper wall middle
		
		wall = new Body(1);
		wall.translate(100 + 1720 / 2, 75 / 2);
		wall.addFixture(Geometry.createRectangle(1720, 75), 20, 1, 1);
		wall.setMass(MassType.INFINITE);
		
		physicsWorld.addBody(wall);
		
		//upper wall right
		
		wall = new Body(1);
		wall.translate(1870, 25);
		wall.addFixture(Geometry.createRectangle(100, 50), 20, 1, 1);
		wall.setMass(MassType.INFINITE);
		
		physicsWorld.addBody(wall);
		
		//lower wall left
		
		wall = new Body(1);
		wall.translate(50, 1055);
		wall.addFixture(Geometry.createRectangle(100, 50), 20, 1, 1);
		wall.setMass(MassType.INFINITE);
		
		physicsWorld.addBody(wall);
		
		//lower wall middle
		
		wall = new Body(1);
		wall.translate(100 + 1720 / 2, 1080 - 75 / 2);
		wall.addFixture(Geometry.createRectangle(1720, 75), 20, 1, 1);
		wall.setMass(MassType.INFINITE);
		
		physicsWorld.addBody(wall);
		
		//lower wall right
		
		wall = new Body(1);
		wall.translate(1920 - 50, 1055);
		wall.addFixture(Geometry.createRectangle(100, 50), 20, 1, 1);
		wall.setMass(MassType.INFINITE);
		
		physicsWorld.addBody(wall);
	}
	
}
