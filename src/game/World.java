package game;

import com.polaris.engine.render.Texture;
import com.polaris.engine.render.TextureManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.dyn4j.dynamics.Force;
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
	private static ArrayList<Track> tracks = new ArrayList<>();

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

		//parkingList.add(new ParkingSpot(0, 0, false));

		parkingList.addAll(ParkingSpot.createParkingArea(200, 153, 10, 1));   //left
		parkingList.addAll(ParkingSpot.createParkingArea(1720 - ParkingSpot.HEIGHT, 153, 10, 3));   //right

		//top
		//parkingList.addAll(ParkingSpot.createParkingArea(580, 200, 9, 2));
		//parkingList.addAll(ParkingSpot.createParkingArea(580, 200 + ParkingSpot.HEIGHT - 5, 9, 0));

		parkingList.addAll(ParkingSpot.createParkingArea(580, 200 + ParkingSpot.HEIGHT / 2, 9, 4));

		//bottom
		//parkingList.addAll(ParkingSpot.createParkingArea(580, 880 - ParkingSpot.HEIGHT * 2 + 5, 9, 2));
		//parkingList.addAll(ParkingSpot.createParkingArea(580, 880 - ParkingSpot.HEIGHT, 9, 0));

		parkingList.addAll(ParkingSpot.createParkingArea(580, 880 - ParkingSpot.HEIGHT / 2 - ParkingSpot.HEIGHT, 9, 4));
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
			
			addPlayerCar(0, new Car(-65, 60, 0, GuiGame.playerColors[0], textureManager));
			addPlayerCar(1, new Car(-65, 915, 0, GuiGame.playerColors[1], textureManager));
			addPlayerCar(2, new Car(1950, 60, 180, GuiGame.playerColors[2], textureManager));
			addPlayerCar(3, new Car(1950, 915, 180, GuiGame.playerColors[3], textureManager));
			
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

		for(Track track : tracks){
			track.render(delta);
		}

		for(Oil oil : oils){
			oil.render(delta);
		}

		GuiGame.renderScores(gameSettings, playerScores);
		
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

	public static ArrayList<Track> getTracks(){
		return tracks;
	}
	
}
