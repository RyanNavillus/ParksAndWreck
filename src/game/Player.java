package game;
import game.Controller;

public class Player {
	private static int playerCount = 0;
	public Controller controller;
	public int id;
	
	public Player() {
		Player.playerCount++;
		id = playerCount;
		controller = Controller.createController(this);
	}
}
