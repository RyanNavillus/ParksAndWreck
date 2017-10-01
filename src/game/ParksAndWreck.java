package game;

import com.polaris.engine.App;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import java.io.IOException;
import java.net.URL;
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
		AudioInputStream introIn;
		AudioInputStream songIn;

		try {
			///introIn = AudioSystem.getAudioInputStream(new URL("file:///" + System.getProperty("user.dir") + "/" + "resources/Dire_Straits_Intro_02.wav"));
			songIn = AudioSystem.getAudioInputStream(new URL("file:///" + System.getProperty("user.dir") + "/" + "resources/Dire_Straits_Money_for_Nothing_Instrumental_Modified_01.wav"));
			Clip clip = AudioSystem.getClip();
			clip.loop(Clip.LOOP_CONTINUOUSLY);
			clip.open(songIn);
			clip.start();
			/*clip.open(introIn);
			LineListener listener = new LineListener() {
				
				@Override
				public void update(LineEvent event) {
					// TODO Auto-generated method stub
					System.out.println("Yo");
					if (!clip.isActive())
					{
						System.out.println("Yo");
						clip.close();
						try {
							clip.open(songIn);
							clip.loop(Clip.LOOP_CONTINUOUSLY);
							clip.start();
						} catch (LineUnavailableException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				}
			};
			
			clip.addLineListener(listener);
			clip.start();
			*/
			
		} catch (LineUnavailableException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();

		} catch (UnsupportedAudioFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

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
