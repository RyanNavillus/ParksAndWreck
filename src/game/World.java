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
		
		simulationWorld = new SimulationWorld("Parks and Wreck");
		
		//parkingList.add(new ParkingSpot(0, 0, false));
	}
	
	public void update(double delta)
	{
	
	}
	
	public void render(double delta)
	{
		for(ParkingSpot parkingSpot : parkingList)
		{
			//parkingSpot.render();
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
