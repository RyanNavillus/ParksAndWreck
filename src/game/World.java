package game;

import com.polaris.engine.render.Texture;
import com.polaris.engine.render.TextureManager;

import java.util.ArrayList;
import java.util.List;

import org.dyn4j.dynamics.Force;
import org.dyn4j.samples.SimulationBody;

/**
 * Created by Killian Le Clainche on 9/30/2017.
 */
public class World
{
	public static Texture fireTexture;
	
	private float totalTime = 0;
	
	private List<Car> staticCars;
	private List<Car> playerCars;
	
	private List<ParkingSpot> parkingList;
	
	private GameSettings gameSettings;
	private TextureManager textureManager;
		
	private org.dyn4j.dynamics.World physicsWorld;
	private double ticksToInitialize = 2;

	public World(GameSettings settings, TextureManager manager, org.dyn4j.dynamics.World physicsWorld)
	{
		staticCars = new ArrayList<>();
		playerCars = new ArrayList<>();
		parkingList = new ArrayList<>();
		
		gameSettings = settings;
		
		textureManager = manager;
				
		this.physicsWorld = physicsWorld;
		
		Car car = new Car(100, 100, 0, manager);
		//playerCars.add(car);
		//physicsWorld.addBody(car.simulationBody);
		
		car = new Car(100, 200, 45, manager);
		playerCars.add(car);
		physicsWorld.addBody(car.simulationBody);
		car.simulationBody.translate(100, 100);
		car.simulationBody.rotate(10);
		car.simulationBody.applyForce(new Force(100.0, 100.0));
				
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
	
	public void update(double delta)
	{

		if((ticksToInitialize -= delta) <= 0)
		{
			playerCars.add(new Car(-65, 55,  270, textureManager));
			
			playerCars.add(new Car(-65, 915,  270, textureManager));
			
			playerCars.add(new Car(1920, 55,  90, textureManager));
			
			playerCars.add(new Car(1920, 915,  90, textureManager));
			
			ticksToInitialize = 10;
		}
		
		for(Car p : playerCars)
			p.update(delta);
		
		for(Car s : staticCars)
			s.update(delta);
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
