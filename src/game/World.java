package game;

import com.polaris.engine.render.Texture;
import com.polaris.engine.render.TextureManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Killian Le Clainche on 9/30/2017.
 */
public class World
{
	public static Texture fireTexture;
	
	private float totalTime = 0;
	
	private List<Car> staticCars;
	private Car[] playerCars;
	private int[] playerScores;
	
	private List<ParkingSpot> parkingList;
	
	private GameSettings gameSettings;
	private TextureManager textureManager;
	
	public World(GameSettings settings, TextureManager manager)
	{
		staticCars = new ArrayList<>();
		playerCars = new Car[4];
		parkingList = new ArrayList<>();
		playerScores = new int[4];
		
		gameSettings = settings;
		
		textureManager = manager;
		
		fireTexture = textureManager.getTexture("fire0");

		/*Car car = new Car(100, 100, 0, 0, 0, manager);
		playerCars[0] = car;
		car = new Car(100, 200, 0, 0, 45, manager);
		playerCars[1] = car;*/

		for(int i = 0; i < playerCars.length; i++){
			playerCars[i] = new Car(100 + i*200, 200, 0, 0, 45, GuiGame.playerColors[i], manager);;
		}

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

	public Car[] getPlayerCars(){
		return playerCars;
	}

	public int[] getPlayerScores(){
		return playerScores;
	}
	
}
