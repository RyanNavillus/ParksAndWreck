package game;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import org.lwjgl.glfw.GLFW;

import com.sun.corba.se.spi.activation._ActivatorImplBase;
import com.sun.xml.internal.bind.v2.model.core.ID;
import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;

public class Controller {
	private int id;
	private Player player;
	
	
	public Controller(Player player) {
		this.player = player;
		id = player.id-1;
	}
	
	public static Controller createController(Player player) {
		boolean present = GLFW.glfwJoystickPresent(player.id - 1);
		String attached = present ? "Connected" : "Not Connected";
		System.out.println("Controller " + (player.id - 1) + ": " + attached);
		if (!present) 
		{
			return null;
		}
		Controller controller = new Controller(player);
		return controller;
	}
	
	/*
	 * 0 - Left x
	 * 1 - Left y
	 * 2 - Right x
	 * 3 - Right y
	 * 4 - Left trigger
	 * 5 - Right trigger
	 */
	public double getDirection() {
		FloatBuffer axes = GLFW.glfwGetJoystickAxes(id);
		
		if(axes.get(0) == 0.0 && axes.get(1) == 0.0)
			return 0;
		
		double angle = Math.toDegrees(Math.atan(axes.get(1) / axes.get(0)));
		if (axes.get(0) < 0 ) 
		{
			angle = 180 + angle;
		}
		if (axes.get(0) > 0 && axes.get(1) < 0)
		{
			angle = 360 + angle;
		}
		//System.out.println("Angle: " + angle + " degrees " + axes.get(1) + " " + axes.get(0));
		return angle;

	}
	
	/*
	 * 0 - A
	 * 1 - B
	 * 2 - X
	 * 3 - Y
	 * 4 - Left Bumper
	 * 5 - Right Bumper
	 * 6 - Back
	 * 7 - Start
	 * 8 - Left Trigger
	 * 9 - Right Trigger
	 * 10 - D-Pad Up
	 * 11 - D-Pad Left
	 * 12 - D-Pad Down
	 * 13 - D-Pad Right
	 */
	public boolean aButtonPressed() 
	{
		ByteBuffer buttons = GLFW.glfwGetJoystickButtons(id);
		if (buttons.get(0) == 1) {
			return true;
		}
		return false;
	}
	
	public boolean backButtonPressed() 
	{
		ByteBuffer buttons = GLFW.glfwGetJoystickButtons(id);
		if (buttons.get(6) == 1) {
			return true;
		}
		return false;
	}
	
	public boolean startButtonPressed() 
	{
		ByteBuffer buttons = GLFW.glfwGetJoystickButtons(id);
		if (buttons.get(7) == 1) {
			return true;
		}
		return false;
	}
	
	
}
