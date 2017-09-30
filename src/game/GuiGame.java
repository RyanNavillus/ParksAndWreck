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
	
	private Shader shader;
	private int texID;
	
	private Background background;
	private World world;
	
	private int frameBuffer;
	private int frameBufferTexture;
	
	public GuiGame(App<GameSettings> app)
	{
		super(app);
		
		background = new Background();
	}
	
	public void init()
	{
		super.init();
		
		world = new World(this.gameSettings);
		
		shader = Shader.createShader(new File("shaders/overlay.vert"), new File("shaders/overlay.frag"));
		
		
		frameBuffer = GL30.glGenFramebuffers();
		frameBufferTexture = GL11.glGenTextures();
		
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, frameBuffer);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, frameBufferTexture);
		
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGB, 720, 460, 0, GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE, 0);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
		
		GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0, GL11.GL_TEXTURE_2D, frameBufferTexture, 0);
		
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
	}
	
	public void update(double delta)
	{
		super.update(delta);
		world.update(delta);
	}
	
	public void render(double delta)
	{
		//GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, frameBuffer);
		super.render(delta);
		//GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		
		GL11.glPushMatrix();
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, 1920, 1080, 0, -1, 1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		
		background.render(delta);
		
		world.render(delta);
		
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(-1, 1, -1, 1, -1, 1);
		GL11.glPopMatrix();
		
		/*GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
		application.gl2d();
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		GL20.glUniform1i(texID, 0);
		
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glVertex2d(0, 0);
		GL11.glVertex2d(0, 1080);
		GL11.glVertex2d(1920, 1080);
		GL11.glVertex2d(1920, 0);
		GL11.glEnd();*/
		
	}
}
