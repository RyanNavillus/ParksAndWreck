package org.dyn4j.ex;

import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Line2D;
import java.util.concurrent.atomic.AtomicBoolean;

import org.dyn4j.dynamics.World;
import org.dyn4j.geometry.Geometry;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Vector2;
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
		
		double offset = 8.0;

		SimulationBody l = new SimulationBody();
		l.addFixture(Geometry.createRectangle(1, 15));
		l.translate(-offset, 0);
		l.setMass(MassType.INFINITE);
		this.world.addBody(l);

		SimulationBody r = new SimulationBody();
		r.addFixture(Geometry.createRectangle(1, 15));
		r.translate(offset, 0);
		r.setMass(MassType.INFINITE);
		this.world.addBody(r);
		
		SimulationBody t = new SimulationBody();
		t.addFixture(Geometry.createRectangle(15, 1));
		t.translate(0, offset);
		t.setMass(MassType.INFINITE);
		this.world.addBody(t);
		
		SimulationBody b = new SimulationBody();
		b.addFixture(Geometry.createRectangle(15, 1));
		b.translate(0, -offset);
		b.setMass(MassType.INFINITE);
		this.world.addBody(b);

		// the car
		car = new Car();
		this.world.addBody(car);
	}

	@Override
	protected void update(Graphics2D g, double elapsedTime) {
		super.update(g, elapsedTime);
		
		final double scale = this.scale;
		final double force = 1000 * elapsedTime;

        final Vector2 r = new Vector2(car.getTransform().getRotation() + Math.PI * 0.5);
        final Vector2 c = car.getWorldCenter();

		// apply thrust
        if (this.forwardThrustOn.get()) {
        	Vector2 f = r.product(force);
        	Vector2 p = c.sum(r.product(0.0));
        	
        	car.applyForce(f);
        	
//        	g.setColor(Color.ORANGE);
        	g.draw(new Line2D.Double(p.x * scale, p.y * scale, (p.x - f.x) * scale, (p.y - f.y) * scale));
        } 
        
        Vector2 velocity = car.getLinearVelocity();
        velocity.multiply(0.98);
        car.setLinearVelocity(velocity);
	}
}
