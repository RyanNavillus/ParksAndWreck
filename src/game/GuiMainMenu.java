package game;

import com.polaris.engine.App;
import com.polaris.engine.gui.GuiScreen;
import com.polaris.engine.options.Key;
import com.polaris.engine.render.Draw;
import com.polaris.engine.render.Shader;
import com.polaris.engine.render.Texture;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.*;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import static org.lwjgl.opengl.GL11.glViewport;

/**
 * Created by Killian Le Clainche on 10/1/2017.
 */
public class GuiMainMenu extends GuiScreen<GameSettings>
{
	private static final String title = "PARKS AND WRECK";
	
	private static final double SCENE_LENGTH = 4.5;
	private double ticksExisted = 0;
	
	private Texture boilermake;
	
	private Shader shader;
	private int texID;
	private int windowSize;
	private int time;
	
	private int frameBuffer;
	private int frameBufferTexture;
	
	private int renderBuffer;
	
	private Clip clip;
	
	private Key startGame;
	
	public GuiMainMenu(App<GameSettings> app)
	{
		super(app);
		
		boilermake = application.getTextureManager().genTexture("boilermake", new File("resources/boilermake.png"));
	}
	
	public void init()
	{
		super.init();
		
		startGame = GameSettings.getKey(GLFW.GLFW_KEY_ENTER);
		
		shader = Shader.createShader(new File("shaders/overlay.vert"), new File("shaders/overlay.frag"));
		
		texID = GL20.glGetUniformLocation(shader.getShaderId(), "renderedTexture");
		windowSize = GL20.glGetUniformLocation(shader.getShaderId(), "window");
		time = GL20.glGetUniformLocation(shader.getShaderId(), "time");
		
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
		
		AudioInputStream introIn;
		AudioInputStream songIn;
		
		try {
			///introIn = AudioSystem.getAudioInputStream(new URL("file:///" + System.getProperty("user.dir") + "/" + "resources/Dire_Straits_Intro_02.wav"));
			songIn = AudioSystem.getAudioInputStream(new URL("file:///" + System.getProperty("user.dir") + "/" + "resources/Dire_Straits_Money_for_Nothing_Instrumental.wav"));
			clip = AudioSystem.getClip();
			clip.loop(Clip.LOOP_CONTINUOUSLY);
			clip.open(songIn);
			clip.start();
			
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
	}
	
	public void close()
	{
		super.close();
	}
	
	public void render(double delta)
	{
		
		if(startGame.isPressed())
		{
			if (ticksExisted < SCENE_LENGTH + .5)
			{
				ticksExisted = SCENE_LENGTH + .5;
			}
			else application.initGui(new GuiGame(application, shader, texID, windowSize, time, frameBuffer, frameBufferTexture, renderBuffer));
		}
		
		super.render(delta);
		
		GL11.glPushMatrix();
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, 1920, 1080, 0, -1, 1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		
		ticksExisted += delta;
		
		if(ticksExisted < SCENE_LENGTH)
		{
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glColor4d(1, 1, 1, (ticksExisted < 1.5 ? ticksExisted / 1.5 : ticksExisted > 3 ? 1 - (ticksExisted - 3.5) / 1.5 : 1));
			
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			boilermake.bind();
			
			GL11.glBegin(GL11.GL_QUADS);
			GL11.glTexCoord2d(0, 0);
			GL11.glVertex2d(960 - 128, 540 - 128);
			GL11.glTexCoord2d(0, 1);
			GL11.glVertex2d(960 - 128, 540 + 128);
			GL11.glTexCoord2d(1, 1);
			GL11.glVertex2d(960 + 128, 540 + 128);
			GL11.glTexCoord2d(1, 0);
			GL11.glVertex2d(960 + 128, 540 - 128);
			GL11.glEnd();
		}
		else
		{
			GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, frameBuffer);
			glViewport(0, 0, 1920, 1080);
			
			
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
			
			GL11.glPushMatrix();
			GL11.glMatrixMode(GL11.GL_PROJECTION);
			GL11.glLoadIdentity();
			GL11.glOrtho(0, 1920, 1080, 0, -1, 1);
			GL11.glMatrixMode(GL11.GL_MODELVIEW);
			
			GL11.glColor4f(.1f, .1f, .1f, 1f);
			
			GL11.glBegin(GL11.GL_QUADS);
			GL11.glVertex2d(0, 0);
			GL11.glVertex2d(0, 1080);
			GL11.glVertex2d(1920, 1080);
			GL11.glVertex2d(1920, 0);
			GL11.glEnd();
			
			gameSettings.getFont().bind();
			
			//title
			GL11.glColor4f(1f, 1f, 1f, 1f);
			float shiftX = 1920 / 2 - gameSettings.getFont().getWidth(title) / (2f / 1.5f);
			gameSettings.getFont().draw(title, shiftX, 250, 0, 1.5f);
			
			shiftX = 1920 / 2 - gameSettings.getFont().getWidth("PLAY GAME") / (2f / .25f);
			gameSettings.getFont().draw("PLAY GAME", shiftX, 600, 0, .25f);
			
			gameSettings.getFont().unbind();
			
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
			GL20.glUniform2f(windowSize, gameSettings.getWindowWidth(), gameSettings.getWindowHeight());
			GL20.glUniform1f(time, (System.currentTimeMillis() % 100000) / 500f);
			
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
}
