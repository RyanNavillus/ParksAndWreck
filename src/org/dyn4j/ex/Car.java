package org.dyn4j.ex;

import org.dyn4j.dynamics.World;
import org.dyn4j.samples.SimulationFrame;

public class Car extends SimulationFrame{
	private static final long serialVersionUID = -6715546875088615532L;

	public Car() {
		super("Car", 32.0);
	}

	public static void main(String[] args) {
		Car simulation = new Car();
		simulation.run();
	}

	@Override
	protected void initializeWorld() {
		this.world.setGravity(World.ZERO_GRAVITY);
		// TODO Auto-generated method stub

	}
}
