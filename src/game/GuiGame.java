package game;

import com.polaris.engine.App;
import com.polaris.engine.gui.GuiScreen;
import com.polaris.engine.render.Shader;
import org.lwjgl.opengl.*;

import java.io.File;

/**
 * Created by Killian Le Clainche on 9/29/17.
 */
public class GuiGame extends GuiScreen<GameSettings>
{
	
	private Background background;
	private World world;
	
	public GuiGame(App<GameSettings> app)
	{
		super(app);
		
		background = new Background();
	}
	
	public void init()
	{
		super.init();
		
		world = new World(this.gameSettings);
	}
	
	public void render(double delta)
	{
		super.render(delta);
		
		GL11.glPushMatrix();
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, 1920, 1080, 0, -1, 1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		
		background.render(delta);
		
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(-1, 1, -1, 1, -1, 1);
		GL11.glPopMatrix();
		
	}
}
