package game;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Killian Le Clainche on 9/30/2017.
 */
public class World
{
	
	private List<Car> staticCars;
	private List<Car> playerCars;
	
	private GameSettings gameSettings;
	
	public World(GameSettings settings)
	{
		staticCars = new ArrayList<>();
		playerCars = new ArrayList<>();
		
		gameSettings = settings;
	}
	
	public void update(double delta)
	{
	
	}
	
	public void render(double delta)
	{
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
