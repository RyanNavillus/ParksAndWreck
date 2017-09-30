package game;

import com.polaris.engine.render.Shader;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import java.io.File;

/**
 * Created by Killian Le Clainche on 9/30/2017.
 */
public class Background
{
	
	private Shader colorShader;
	private int[] color;
	
	public Background()
	{
		colorShader = Shader.createShader(new File("shaders/crt.vert"), new File("shaders/crt.frag"));
		
		color = new int[3];
		color[0] = GL20.glGetUniformLocation(colorShader.getShaderId(), "red");
		color[1] = GL20.glGetUniformLocation(colorShader.getShaderId(), "green");
		color[2] = GL20.glGetUniformLocation(colorShader.getShaderId(), "blue");
	}
	
	public void render(double delta)
	{
		colorShader.bind();
		
		GL20.glUniform1f(color[0], .3f);
		GL20.glUniform1f(color[1], .3f);
		GL20.glUniform1f(color[2], .3f);
		
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glVertex2d(0, 0);
		GL11.glVertex2d(0, 1080);
		GL11.glVertex2d(1920, 1080);
		GL11.glVertex2d(1920, 0);
		GL11.glEnd();
		
		GL11.glColor3f(.1f, .6f, .1f);
		
		GL20.glUniform1f(color[0], .1f);
		GL20.glUniform1f(color[1], .6f);
		GL20.glUniform1f(color[2], .1f);
		
		GL11.glBegin(GL11.GL_QUADS);
		
		GL11.glVertex2d(0, 0);
		GL11.glVertex2d(0, 50);
		GL11.glVertex2d(100, 50);
		GL11.glVertex2d(100, 0);
		
		GL11.glVertex2d(100, 0);
		GL11.glVertex2d(100, 75);
		GL11.glVertex2d(1820, 75);
		GL11.glVertex2d(1820, 0);
		
		GL11.glVertex2d(1820, 0);
		GL11.glVertex2d(1820, 50);
		GL11.glVertex2d(1920, 50);
		GL11.glVertex2d(1920, 0);
		
		GL11.glVertex2d(0, 150);
		GL11.glVertex2d(0, 930);
		GL11.glVertex2d(200, 930);
		GL11.glVertex2d(200, 150);
		
		GL11.glVertex2d(1720, 150);
		GL11.glVertex2d(1720, 930);
		GL11.glVertex2d(1920, 930);
		GL11.glVertex2d(1920, 150);
		
		GL11.glVertex2d(0, 1030);
		GL11.glVertex2d(0, 1080);
		GL11.glVertex2d(100, 1080);
		GL11.glVertex2d(100, 1030);
		
		GL11.glVertex2d(100, 1005);
		GL11.glVertex2d(100, 1080);
		GL11.glVertex2d(1820, 1080);
		GL11.glVertex2d(1820, 1005);
		
		GL11.glVertex2d(1820, 1030);
		GL11.glVertex2d(1820, 1080);
		GL11.glVertex2d(1920, 1080);
		GL11.glVertex2d(1920, 1030);
		
		GL11.glEnd();
		
		GL20.glUniform1f(color[0], .8f);
		GL20.glUniform1f(color[1], .8f);
		GL20.glUniform1f(color[2], .8f);
		GL11.glColor3f(.8f, .8f, .8f);
		GL11.glBegin(GL11.GL_QUADS);
		
		GL11.glVertex2d(0, 150);
		GL11.glVertex2d(0, 160);
		GL11.glVertex2d(200, 160);
		GL11.glVertex2d(200, 150);
		
		GL11.glVertex2d(190, 160);
		GL11.glVertex2d(190, 920);
		GL11.glVertex2d(200, 920);
		GL11.glVertex2d(200, 160);
		
		GL11.glVertex2d(0, 920);
		GL11.glVertex2d(0, 930);
		GL11.glVertex2d(200, 930);
		GL11.glVertex2d(200, 920);
		
		GL11.glVertex2d(1720, 150);
		GL11.glVertex2d(1720, 160);
		GL11.glVertex2d(1920, 160);
		GL11.glVertex2d(1920, 150);
		
		GL11.glVertex2d(1720, 160);
		GL11.glVertex2d(1720, 920);
		GL11.glVertex2d(1730, 920);
		GL11.glVertex2d(1730, 160);
		
		GL11.glVertex2d(1720, 920);
		GL11.glVertex2d(1720, 930);
		GL11.glVertex2d(1920, 930);
		GL11.glVertex2d(1920, 920);
		
		GL11.glEnd();
		
		GL20.glUniform1f(color[0], .7f);
		GL20.glUniform1f(color[1], .7f);
		GL20.glUniform1f(color[2], .7f);
		GL11.glColor3f(.7f, .7f, .7f);
		GL11.glBegin(GL11.GL_QUADS);
		
		GL11.glVertex2d(0, 40);
		GL11.glVertex2d(0, 50);
		GL11.glVertex2d(100, 50);
		GL11.glVertex2d(100, 40);
		
		GL11.glVertex2d(100, 40);
		GL11.glVertex2d(100, 75);
		GL11.glVertex2d(120, 75);
		GL11.glVertex2d(120, 40);
		
		GL11.glVertex2d(120, 65);
		GL11.glVertex2d(120, 75);
		GL11.glVertex2d(1800, 75);
		GL11.glVertex2d(1800, 65);
		
		GL11.glVertex2d(1800, 40);
		GL11.glVertex2d(1800, 75);
		GL11.glVertex2d(1820, 75);
		GL11.glVertex2d(1820, 40);
		
		GL11.glVertex2d(1820, 40);
		GL11.glVertex2d(1820, 50);
		GL11.glVertex2d(1920, 50);
		GL11.glVertex2d(1920, 40);
		
		GL11.glEnd();
		
		GL20.glUniform1f(color[0], .9f);
		GL20.glUniform1f(color[1], .9f);
		GL20.glUniform1f(color[2], .9f);
		GL11.glColor3f(.9f, .9f, .9f);
		GL11.glBegin(GL11.GL_QUADS);
		
		GL11.glVertex2d(0, 1030);
		GL11.glVertex2d(0, 1040);
		GL11.glVertex2d(100, 1040);
		GL11.glVertex2d(100, 1030);
		
		GL11.glVertex2d(100, 1005);
		GL11.glVertex2d(100, 1040);
		GL11.glVertex2d(120, 1040);
		GL11.glVertex2d(120, 1005);
		
		GL11.glVertex2d(120, 1005);
		GL11.glVertex2d(120, 1015);
		GL11.glVertex2d(1800, 1015);
		GL11.glVertex2d(1800, 1005);
		
		GL11.glVertex2d(1800, 1005);
		GL11.glVertex2d(1800, 1040);
		GL11.glVertex2d(1820, 1040);
		GL11.glVertex2d(1820, 1005);
		
		GL11.glVertex2d(1820, 1030);
		GL11.glVertex2d(1820, 1040);
		GL11.glVertex2d(1920, 1040);
		GL11.glVertex2d(1920, 1030);
		
		GL11.glEnd();
		
		colorShader.unbind();
	}
	
}
