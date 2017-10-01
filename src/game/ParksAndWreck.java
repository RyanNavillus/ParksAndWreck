package game;

import com.polaris.engine.App;

import java.util.ArrayList;

import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.geometry.Geometry;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Vector2;
import org.dyn4j.samples.SimulationBody;
/**
 * Created by Killian Le Clainche on 9/29/17.
 */
import org.dyn4j.samples.SimulationFrame;
public class ParksAndWreck extends App<GameSettings>
{
		
	public static void main(String[] args)
	{
		ParksAndWreck game = new ParksAndWreck();

		App.start(game);
	}
	
	/**
	 * Protected constructor of the Application.
	 * I do this because I don't want people to see this constructor.
	 *
	 * @param debug Called from children constructors to determine whether the application should be debugging or
	 *              not in the console.
	 */
	protected ParksAndWreck()
	{
		super(false);
	}
	
	public void init()
	{
		super.init();
				
		//simulationWorld.run();
		// End Testing
		
		gameSettings.createFonts();
		gameSettings.getKeys();
		this.initGui(new GuiGame(this));
	}
	
	@Override
	protected GameSettings loadSettings()
	{
		return new GameSettings();
	}
	
}
