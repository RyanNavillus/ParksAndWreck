package org.dyn4j.ex;

import org.dyn4j.dynamics.World;
import org.dyn4j.samples.SimulationBody;
import org.dyn4j.samples.SimulationFrame;

public class CarSimulation extends SimulationFrame{
	private static final long serialVersionUID = -6715546875088615532L;
	
	private SimulationBody car;

	public CarSimulation() {
		super("Car", 32.0);
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
