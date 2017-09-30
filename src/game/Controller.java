package game;

import org.lwjgl.glfw.GLFW.*;

public class Controller {
	private int id;
	private Player player;
	
	
	public Controller(Player player) {
		this.player = player;
		id = player.id;
		boolean hi = glfwJoystickPresent();
	}
	
	
}
