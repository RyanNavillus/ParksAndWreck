package org.dyn4j.ex;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.concurrent.atomic.AtomicBoolean;

import org.dyn4j.dynamics.World;
import org.dyn4j.samples.SimulationBody;
import org.dyn4j.samples.SimulationFrame;

public class CarSimulation extends SimulationFrame{
	private static final long serialVersionUID = -6715546875088615532L;
	
	private SimulationBody car;

	private AtomicBoolean forwardThrustOn = new AtomicBoolean(false);
	private AtomicBoolean leftTurnOn = new AtomicBoolean(false);
	private AtomicBoolean rightTurnOn = new AtomicBoolean(false);

	private class CustomKeyListener extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent e) {
			switch (e.getKeyCode()) {
				case KeyEvent.VK_UP:
					forwardThrustOn.set(true);
					break;
				case KeyEvent.VK_LEFT:
					leftTurnOn.set(true);
					break;
				case KeyEvent.VK_RIGHT:
					rightTurnOn.set(true);
					break;
			}
			
		}
		
		@Override
		public void keyReleased(KeyEvent e) {
			switch (e.getKeyCode()) {
				case KeyEvent.VK_UP:
					forwardThrustOn.set(false);
					break;
				case KeyEvent.VK_LEFT:
					leftTurnOn.set(false);
					break;
				case KeyEvent.VK_RIGHT:
					rightTurnOn.set(false);
					break;
			}
		}
	}

	public CarSimulation() {
		super("Car", 32.0);

		KeyListener listener = new CustomKeyListener();
		this.addKeyListener(listener);
		this.canvas.addKeyListener(listener);
	}

	public static void main(String[] args) {
		CarSimulation simulation = new CarSimulation();
		simulation.run();
	}

	@Override
	protected void initializeWorld() {
		this.world.setGravity(World.ZERO_GRAVITY);

		// the car
		car = new Car();
		this.world.addBody(car);
	}
}
