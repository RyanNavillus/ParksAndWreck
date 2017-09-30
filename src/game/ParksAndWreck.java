package game;

import com.polaris.engine.App;

/**
 * Created by Killian Le Clainche on 9/29/17.
 */
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
		super(true);
	}
	
	public void init()
	{
		super.init();
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
