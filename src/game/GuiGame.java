package game;

import com.polaris.engine.App;
import com.polaris.engine.gui.GuiScreen;
import com.polaris.engine.render.Shader;
import com.polaris.engine.render.Texture;
import com.polaris.engine.render.TextureManager;
import org.lwjgl.opengl.*;

import java.io.File;

/**
 * Created by Killian Le Clainche on 9/29/17.
 */
public class GuiGame extends GuiScreen<GameSettings>
{
	
	private Shader shader;
	private int texID;
	private int windowSize;
	
	private Texture car;
	private Texture carFrame;
	
	private Background background;
	private World world;
	
	private int frameBuffer;
	private int frameBufferTexture;
	
	private int renderBuffer;
	
	public GuiGame(App<GameSettings> app)
	{
		super(app);
		
		background = new Background();
		
		car = application.getTextureManager().genTexture("car", new File("resources/car.png"));
		carFrame = application.getTextureManager().genTexture("carframe", new File("resources/carframe.png"));
	}
	
	public void init()
	{
		super.init();
		
		world = new World(this.gameSettings);
		
		shader = Shader.createShader(new File("shaders/overlay.vert"), new File("shaders/overlay.frag"));
		
		texID = GL20.glGetUniformLocation(shader.getShaderId(), "renderedTexture");
		windowSize = GL20.glGetUniformLocation(shader.getShaderId(), "windowSize");
		
		
		frameBuffer = GL30.glGenFramebuffers();
		frameBufferTexture = GL11.glGenTextures();
		
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, frameBuffer);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, frameBufferTexture);
		
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGB, 1920, 1080, 0, GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE, 0);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
		
		GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0, GL11.GL_TEXTURE_2D, frameBufferTexture, 0);
		
		GL20.glDrawBuffers(GL30.GL_COLOR_ATTACHMENT0);
		
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		
		float[] g_quad_vertex_buffer_data = {
				0, 0, 0.0f,
				1920, 0, 0.0f,
				0,  1080, 0.0f,
				0,  1080, 0.0f,
				1920, 0, 0.0f,
				1920,  1080, 0.0f,
				};
		
		renderBuffer = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, renderBuffer);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, g_quad_vertex_buffer_data, GL15.GL_STATIC_DRAW);
	}
	
	public void update(double delta)
	{
		super.update(delta);
		world.update(delta);
	}
	
	public void render(double delta)
	{
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, frameBuffer);
		super.render(delta);
		
		GL11.glPushMatrix();
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, 1920, 1080, 0, -1, 1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		
		background.render(delta);
		
		world.render(delta);
		
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		
		car.bind();
		
		GL11.glBegin(GL11.GL_QUADS);
		
		GL11.glTexCoord2d(0, 0);
		GL11.glVertex2d(400, 400);
		GL11.glTexCoord2d(0, 1);
		GL11.glVertex2d(400, 488);
		GL11.glTexCoord2d(1, 1);
		GL11.glVertex2d(454, 488);
		GL11.glTexCoord2d(1, 0);
		GL11.glVertex2d(454, 400);
		
		GL11.glEnd();
		
		carFrame.bind();
		
		GL11.glBegin(GL11.GL_QUADS);
		
		GL11.glTexCoord2d(0, 0);
		GL11.glVertex2d(400, 400);
		GL11.glTexCoord2d(0, 1);
		GL11.glVertex2d(400, 400 + 44 * 2.5);
		GL11.glTexCoord2d(1, 1);
		GL11.glVertex2d(400 + 27 * 2.5, 400 + 44 * 2.5);
		GL11.glTexCoord2d(1, 0);
		GL11.glVertex2d(400 + 27 * 2.5, 400);
		
		GL11.glEnd();
		
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
		application.gl2d();
		
		GL11.glPushMatrix();
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, 1920, 1080, 0, -1, 1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		
		shader.bind();
		
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, frameBufferTexture);
		
		GL20.glUniform1i(texID, 0);
		GL20.glUniform2fv(windowSize, new float[]{gameSettings.getWindowWidth(), gameSettings.getWindowHeight()});
		
		GL20.glEnableVertexAttribArray(0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, renderBuffer);
		GL20.glVertexAttribPointer(
				0,                  // attribute 0. No particular reason for 0, but must match the layout in the shader.
				3,                  // size
				GL11.GL_FLOAT,           // type
				false,           // normalized?
				0,                  // stride
				0            // array buffer offset
		);
		
		// Draw the triangles !
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, 6); // 2*3 indices starting at 0 -> 2 triangles
		
		GL20.glDisableVertexAttribArray(0);
		
		shader.unbind();
		
	}
}
