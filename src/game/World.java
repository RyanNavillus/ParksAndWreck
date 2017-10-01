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
	private List<Car> playerCars;
	
	private List<ParkingSpot> parkingList;
	
	private GameSettings gameSettings;
	private TextureManager textureManager;
		
	private org.dyn4j.dynamics.World physicsWorld;
	private double ticksToInitialize = 2;

	public World(GameSettings settings, TextureManager manager)
	{
		staticCars = new ArrayList<>();
		playerCars = new ArrayList<>();
		parkingList = new ArrayList<>();
		
		players = new Player[] {new Player(), new Player(), new Player(), new Player()};
		
		gameSettings = settings;
		
		textureManager = manager;
		
		physicsWorld = new org.dyn4j.dynamics.World();
		physicsWorld.setGravity(new Vector2(0, 0));
				
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
	
	private void addPlayerCar(Car car)
	{
		playerCars.add(car);
		physicsWorld.addBody(car);
	}
	
	public void update(double delta)
	{
		if((ticksToInitialize -= delta) <= 0)
		{
			staticCars.addAll(playerCars);
			
			addPlayerCar(new Car(-65, 55,  0, textureManager));
			
			ticksToInitialize = 10;
		}
		
		if(playerCars.size() > 0 && players[0].controller.getDirection() != Double.NaN)
		{
			//System.out.println(players[0].controller.getDirection() + " BLALDSLDASL " + )
			playerCars.get(0).rotateAboutCenter(-(players[0].controller.getDirection() / 180 * Math.PI) - playerCars.get(0).getTransform().getRotation());
		}
		
		for(Car p : playerCars)
			p.update(delta);
		
		for(Car s : staticCars)
			s.update(0);
		
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
		
		for(Car car : staticCars)
		{
			car.render(delta);
		}
		
		for(Car car : playerCars)
		{
			car.render(delta);
		}
	}
	
}
