package game;

import com.polaris.engine.render.Texture;
import com.polaris.engine.render.TextureManager;

import sun.security.krb5.internal.crypto.crc32;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
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

		parkingList.addAll(ParkingSpot.createParkingArea(200, 68, 1, 1));   //left
		parkingList.addAll(ParkingSpot.createParkingArea(1720 - ParkingSpot.HEIGHT, 68, 9, 3));   //right

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
			addPlayerCar(0, new Car(-65, 55, 0, GuiGame.playerColors[0], textureManager));
			
			ticksToInitialize = 100;
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
		
		for (ParkingSpot spot : parkingList)
		{
			for (Car car : playerCars)
			{
				if (car != null && spot.containsCar(car))
				{
					if(spot.id == car.parkingSpotId)
					{
						double timeInterval = ((new Date().getTime()) - car.parkingStartTime.getTime()) / 1000.0; // Seconds spent in parking spot
						System.out.println("Time interval: " + timeInterval + " :" + spot.id);
						if (timeInterval > 1)
						{
							System.out.println("Parked in spot: " + spot.id);
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
	
}
