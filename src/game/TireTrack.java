package game;

import javafx.scene.shape.Line;

/**
 * Created by Killian Le Clainche on 9/30/2017.
 */
public class TireTrack
{
	
	private Line trackLine;
	
	public TireTrack(Car car)
	{
		trackLine = new Line();
		
		trackLine.setStartX(car.getPosX());
		trackLine.setStartY(car.getPosY());
	}
	
	public void render(double delta)
	{
	
	}
	
}
