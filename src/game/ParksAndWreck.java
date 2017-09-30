package game;

import org.lwjgl.glfw.GLFW;

import com.polaris.engine.App;

import java.util.ArrayList;
/**
 * Created by Killian Le Clainche on 9/29/17.
 */
public class ParksAndWreck extends App<GameSettings>
{
	
	private ArrayList<Player> players;
	
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
		
		//Testing
		int maximumPlayers = 4;
		players = new ArrayList<Player>();
		for (int i = 0; i < maximumPlayers; i++) 
		{
			Player nextPlayer = new Player();
			if (nextPlayer != null)
			{
				players.add(nextPlayer);
			}
		}

		int numberOfPlayers = players.size();
		
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
