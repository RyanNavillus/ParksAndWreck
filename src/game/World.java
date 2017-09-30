package game;

import com.polaris.engine.render.TextureManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Killian Le Clainche on 9/30/2017.
 */
public class World
{
	
	private List<Car> staticCars;
	private List<Car> playerCars;
	
	private List<ParkingSpot> parkingList;
	
	private GameSettings gameSettings;
	private TextureManager textureManager;
	
	private SimulationWorld simulationWorld;
	
	public World(GameSettings settings, TextureManager manager)
	{
		staticCars = new ArrayList<>();
		playerCars = new ArrayList<>();
		parkingList = new ArrayList<>();
		
		gameSettings = settings;
		
		textureManager = manager;
<<<<<<< HEAD
		
		simulationWorld = new SimulationWorld("Parks and Wreck");
		
=======

		Car car = new Car(100, 100, 0, 0, 0, manager);
		playerCars.add(car);
		car = new Car(100, 200, 0, 0, 45, manager);
		playerCars.add(car);

>>>>>>> e3c137cfc5a8df264017344b5096031f8412e250
		//parkingList.add(new ParkingSpot(0, 0, false));

		parkingList.addAll(ParkingSpot.createParkingArea(200, 153, 10, 1));   //left
		parkingList.addAll(ParkingSpot.createParkingArea(1720 - ParkingSpot.HEIGHT, 153, 10, 3));   //right

		//top
		parkingList.addAll(ParkingSpot.createParkingArea(440, 200, 9, 2));
		parkingList.addAll(ParkingSpot.createParkingArea(440, 200 + ParkingSpot.HEIGHT - 5, 9, 0));

		//bottom
		parkingList.addAll(ParkingSpot.createParkingArea(440, 880 - ParkingSpot.HEIGHT * 2 + 5, 9, 2));
		parkingList.addAll(ParkingSpot.createParkingArea(440, 880 - ParkingSpot.HEIGHT, 9, 0));
	}
	
	public void update(double delta)
	{
	
	}
	
	public void render(double delta)
	{
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
