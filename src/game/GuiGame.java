package game;

import com.polaris.engine.App;
import com.polaris.engine.gui.GuiScreen;
import com.polaris.engine.render.Shader;
import com.polaris.engine.render.Texture;
import com.polaris.engine.render.TextureManager;

import com.polaris.engine.util.MathHelper;
import org.dyn4j.geometry.Vector2;
import org.lwjgl.opengl.*;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.glViewport;

/**
 * Created by Killian Le Clainche on 9/29/17.
 */
public class GuiGame extends GuiScreen<GameSettings>
{
	
	
	private static final String title = "PARKS AND WRECK";
	
	private Shader shader;
	private int texID;
	private int windowSize;
	private int time;
	
	private Background background;
	private World world;
	public static double[][] playerColors;

	private int frameBuffer;
	private int frameBufferTexture;
	
	private int renderBuffer;

	private ArrayList<ParkingSpot> parkingSpots;
	
	private float dist = 0;

	private static int animationCounter;
	
	private DecimalFormat formatter = new DecimalFormat("0.00");
	private double ticksExisted = 0;
	
	public GuiGame(App<GameSettings> app, Shader shader, int texID, int windowSize, int time, int frameBuffer, int frameBufferTexture, int renderBuffer)
	{
		super(app);
		
		this.shader = shader;
		this.texID = texID;
		this.windowSize = windowSize;
		this.time = time;
		
		this.frameBuffer = frameBuffer;
		this.frameBufferTexture = frameBufferTexture;
		
		this.renderBuffer = renderBuffer;
		
		background = new Background();
		
		//car = application.getTextureManager().genTexture("car", new File("resources/car.png"));
		//carFrame = application.getTextureManager().genTexture("carframe", new File("resources/carframe.png"));
		initializePhysics();
		application.getTextureManager().genTexture("car", new File("resources/car.png"));
		application.getTextureManager().genTexture("carframe", new File("resources/carframe.png"));
		application.getTextureManager().genTexture("carframeBroke", new File("resources/carframeBroke.png"));
		application.getTextureManager().genTexture("fire0", new File("resources/flame0.png"));
		application.getTextureManager().genTexture("fire1", new File("resources/flame1.png"));
		
		application.getTextureManager().genTexture("puddle_xsmall", new File("resources/puddle_xsmall.png"));
		application.getTextureManager().genTexture("puddle_small", new File("resources/puddle_small.png"));
		application.getTextureManager().genTexture("puddle_medium", new File("resources/puddle_medium.png"));
		application.getTextureManager().genTexture("puddle_large", new File("resources/puddle_large.png"));
		application.getTextureManager().genTexture("puddle_xlarge", new File("resources/puddle_xlarge.png"));

		application.getTextureManager().genTexture("track0", new File("resources/tiretrack0.png"));
		application.getTextureManager().genTexture("track1", new File("resources/tiretrack1.png"));
		application.getTextureManager().genTexture("track2", new File("resources/tiretrack2.png"));
		application.getTextureManager().genTexture("track3", new File("resources/tiretrack3.png"));
		application.getTextureManager().genTexture("track4", new File("resources/tiretrack4.png"));
		application.getTextureManager().genTexture("track5", new File("resources/tiretrack5.png"));


		Oil.setOilTextures(new Texture[] {application.getTextureManager().getTexture("puddle_xsmall"),
				                          application.getTextureManager().getTexture("puddle_small"),
			                              application.getTextureManager().getTexture("puddle_medium"),
			                              application.getTextureManager().getTexture("puddle_large"),
			                              application.getTextureManager().getTexture("puddle_xlarge")
		                                 });

		Track.setTrackTextures(new Texture[] {application.getTextureManager().getTexture("track0"),
				application.getTextureManager().getTexture("track1"),
				application.getTextureManager().getTexture("track2"),
				application.getTextureManager().getTexture("track3"),
				application.getTextureManager().getTexture("track4"),
				application.getTextureManager().getTexture("track5")
		});
		
		for(int i = 0; i < 5; i++)
			application.getTextureManager().genTexture("tiretrack" + i, new File("resources/tiretrack" + i + ".png"));

		playerColors = new double[4][3];
		for(int i = 0; i < playerColors.length; i++){
			playerColors[i] = generatePlayerColor(playerColors, i);
		}
	}
	
	public void init()
	{
		super.init();
		world = new World(this.gameSettings, application.getTextureManager());
	}
	
	private void initializePhysics() {
		
	}
	
	public void update(double delta)
	{
		super.update(delta);
		if(ticksExisted < 60)
		world.update(delta);
	}
	
	public void render(double delta)
	{
		
		if(World.tracks.size() > 0)
			World.renderTrack();
		
		dist+= delta * 40;
		ticksExisted += delta;
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, frameBuffer);
		glViewport(0, 0, 1920, 1080);


		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		
		GL11.glPushMatrix();
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, 1920, 1080, 0, -1, 1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		
		background.render(delta);

		gameSettings.getFont().bind();
		
		float shiftX = 1920 / 2 - gameSettings.getFont().getWidth(formatter.format(60 - ticksExisted) + "s") / 2f;
		GL11.glColor4f(1, 1, 0, 1);
		gameSettings.getFont().draw(formatter.format(60 - ticksExisted) + "s", shiftX, 50, 0, .5f);
		
		//title
		GL11.glColor4d(.25f, .25f, .25f, 1);
		shiftX = 1920 / 2 - gameSettings.getFont().getWidth(title) / 2f;
		gameSettings.getFont().draw(title, shiftX, 600, 0, 1f);

		gameSettings.getFont().unbind();
		
		world.render(delta);
		
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

	public static void renderScores(GameSettings gameSettings, int[] playerScores, int[] displayScores){
		GL11.glPushMatrix();

		GL11.glTranslated(0f, 0f, .5f);

		gameSettings.getFont().bind();

		float shiftX;
		float shiftY;

		for (int i = 0; i < playerScores.length; i++){
			if (playerScores[i] > displayScores[i] && animationCounter % 2 == 0){
				displayScores[i]++;
			}
		}

		int displacementFrequency = displayScores[0] != 0 ? 100 / displayScores[0] : 100;
		if (displacementFrequency < 5)
			displacementFrequency = 5;

		//scores
		int randomOffset = Math.random() * displacementFrequency < 1 ? 5 * playerScores[0] / 40 : 0;
		randomOffset = Math.random() * 2 < 1 ? randomOffset : -randomOffset;
		setColorToCar(0);
		shiftX = 470 - gameSettings.getFont().getWidth(Integer.toString(displayScores[0])) / 2f - randomOffset;
		randomOffset = Math.random() * 2 < 1 ? randomOffset : -randomOffset;
		shiftY = 200 - randomOffset;
		gameSettings.getFont().draw(Integer.toString(displayScores[0]), shiftX, shiftY, 0, .5f);

		displacementFrequency = displayScores[1] != 0 ? 100 / displayScores[1] : 100;
		if (displacementFrequency < 5)
			displacementFrequency = 5;

		randomOffset = Math.random() * displacementFrequency < 1 ? 5 * playerScores[1] / 20 : 0;
		randomOffset = Math.random() * 2 < 1 ? randomOffset : -randomOffset;
		setColorToCar(1);
		shiftX = 1500 - gameSettings.getFont().getWidth(Integer.toString(displayScores[1])) / 2f - randomOffset;
		gameSettings.getFont().draw(Integer.toString(displayScores[1]), shiftX, 200, 0, .5f);

		displacementFrequency = displayScores[2] != 0 ? 100 / displayScores[2] : 100;
		if (displacementFrequency < 5)
			displacementFrequency = 5;

		randomOffset = Math.random() * displacementFrequency < 1 ? 5 * playerScores[2] / 20 : 0;
		randomOffset = Math.random() * 2 < 1 ? randomOffset : -randomOffset;
		setColorToCar(2);
		shiftX = 470 - gameSettings.getFont().getWidth(Integer.toString(displayScores[2])) / 2f - randomOffset;
		gameSettings.getFont().draw(Integer.toString(displayScores[2]), shiftX, 950, 0, .5f);

		displacementFrequency = displayScores[3] != 0 ? 100 / displayScores[3] : 100;
		if (displacementFrequency < 5)
			displacementFrequency = 5;

		randomOffset = Math.random() * displacementFrequency < 1 ? 5 * playerScores[3] / 20 : 0;
		randomOffset = Math.random() * 2 < 1 ? randomOffset : -randomOffset;
		setColorToCar(3);
		shiftX = 1500 - gameSettings.getFont().getWidth(Integer.toString(displayScores[3])) / 2f - randomOffset;
		gameSettings.getFont().draw(Integer.toString(displayScores[3]), shiftX, 950, 0, .5f);

		gameSettings.getFont().unbind();

		GL11.glPopMatrix();

		animationCounter++;
		if (animationCounter == 11)
			animationCounter = 1;
	}

	private static void setColorToCar(int player){
		double[] colors = playerColors[player];
		double offset =  - Math.random() / 4;

		offset = Math.random() * 50 < 1 ? offset * 2 : offset;

		double r = colors[0] - offset;
		double g = colors[1] - offset;
		double b = colors[2] - offset;

		if(r > 1)
			r = 1;

		if (g > 1)
			g = 1;

		if (b > 1)
			b = 1;

		GL11.glColor4d(r, g, b, 0.1);
	}

	private double[] generatePlayerColor(double[][] colors, int size){
		
		boolean foundColor = false;
		int red = 0, green = 0, blue = 0, maxDiff = 0;
		
		while(!foundColor)
		{
			foundColor = true;
			red = MathHelper.random(256);
			green = MathHelper.random(256);
			blue = MathHelper.random(256);
			
			maxDiff = Math.max(Math.abs(red - green), Math.abs(red - blue));
			maxDiff = Math.max(maxDiff, Math.abs(green - blue));
			
			if(maxDiff < 128)
			{
				foundColor = false;
				continue;
			}
			
			for(int i = 0; i < size; i++)
			{
				maxDiff = (int) Math.abs(colors[i][0] * 255 - red);
				maxDiff += (int) Math.abs(colors[i][1] * 255 - green);
				maxDiff += (int) Math.abs(colors[i][2] * 255 - blue);
				if(maxDiff < 275)
				{
					foundColor = false;
				}
			}
			
		}

		return new double[] {red / 255.0, green / 255.0, blue / 255.0};
	}
}
