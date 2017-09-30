package game;

import com.polaris.engine.options.Input;
import com.polaris.engine.options.Settings;
import com.polaris.engine.options.WindowMode;
import org.lwjgl.glfw.GLFW;

import java.io.File;

import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;

/**
 * Created by Killian Le Clainche on 9/29/17.
 */
public class GameSettings extends Settings
{
	
	public void init(Input i)
	{
		super.init(i);
		
		this.setWindowWidth(1920);
		this.setWindowHeight(1000);
	}
	
	public void createFonts()
	{
		//gameFont = Font.createFont(new File("font/copper.ttf"), 128);
	}
	
	public void getKeys()
	{
		
		/*this.jumpKey = new Key[]{input.getKey(GLFW.GLFW_KEY_W), input.getKey(GLFW.GLFW_KEY_I), input.getKey(GLFW.GLFW_KEY_T), input.getKey(GLFW.GLFW_KEY_UP)};
		this.smashKey = new Key[]{input.getKey(GLFW.GLFW_KEY_S), input.getKey(GLFW.GLFW_KEY_K), input.getKey(GLFW.GLFW_KEY_G), input.getKey(GLFW.GLFW_KEY_DOWN)};
		this.leftKey = new Key[]{input.getKey(GLFW.GLFW_KEY_A), input.getKey(GLFW.GLFW_KEY_J), input.getKey(GLFW.GLFW_KEY_F), input.getKey(GLFW.GLFW_KEY_LEFT)};
		this.rightKey = new Key[]{input.getKey(GLFW.GLFW_KEY_D), input.getKey(GLFW.GLFW_KEY_L), input.getKey(GLFW.GLFW_KEY_H), input.getKey(GLFW.GLFW_KEY_RIGHT)};
		this.beamKey = new Key[]{input.getKey(GLFW.GLFW_KEY_E), input.getKey(GLFW.GLFW_KEY_O), input.getKey(GLFW.GLFW_KEY_Y), input.getKey(GLFW.GLFW_KEY_RIGHT_SHIFT)};
		this.superKey = new Key[]{input.getKey(GLFW.GLFW_KEY_Q), input.getKey(GLFW.GLFW_KEY_U), input.getKey(GLFW.GLFW_KEY_R), input.getKey(GLFW.GLFW_KEY_SLASH)};*/
	}
	
}
