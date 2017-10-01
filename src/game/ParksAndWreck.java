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
		/*AudioInputStream introIn;
		AudioInputStream songIn;

		try {
			///introIn = AudioSystem.getAudioInputStream(new URL("file:///" + System.getProperty("user.dir") + "/" + "resources/Dire_Straits_Intro_02.wav"));
			songIn = AudioSystem.getAudioInputStream(new URL("file:///" + System.getProperty("user.dir") + "/" + "resources/Dire_Straits_Money_for_Nothing_Instrumental_Modified_01.wav"));
			Clip clip = AudioSystem.getClip();
			clip.loop(Clip.LOOP_CONTINUOUSLY);
			clip.open(songIn);
			clip.start();
			
		/*} catch (LineUnavailableException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();

		} catch (UnsupportedAudioFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/

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
		this.initGui(new GuiMainMenu(this));
	}
	
	@Override
	protected GameSettings loadSettings()
	{
		return new GameSettings();
	}
	
}
