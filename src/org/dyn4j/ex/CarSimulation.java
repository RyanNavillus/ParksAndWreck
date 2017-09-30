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

public class CarSimulation extends SimulationFrame {
	private static final long serialVersionUID = -6715546875088615532L;
	
	private SimulationBody car;

	private AtomicBoolean forwardThrustOn = new AtomicBoolean(false);
	private AtomicBoolean leftTurnOn = new AtomicBoolean(false);
	private AtomicBoolean rightTurnOn = new AtomicBoolean(false);
	private AtomicBoolean jump = new AtomicBoolean(false);

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
				case KeyEvent.VK_SPACE:
					jump.set(true);
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
				case KeyEvent.VK_SPACE:
					jump.set(false);
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
		car.setLinearDamping(0.20f);
		car.setAngularDamping(0.45f);
		this.world.addBody(car);
	}

	@Override
	protected void update(Graphics2D g, double elapsedTime) {
		super.update(g, elapsedTime);
		
		final double scale = this.scale;
		double force = 1000 * elapsedTime;

        final Vector2 r = new Vector2(car.getTransform().getRotation() + Math.PI * 0.5);
        final Vector2 rnorm = r.getNormalized();
        final Vector2 rrnorm = rnorm.getRightHandOrthogonalVector();
        final Vector2 c = car.getWorldCenter();
        
        // Circle the center
//        g.draw(new java.awt.geom.Ellipse2D.Double((c.x - 0.5/2) * scale, (c.y - 0.5/2) * scale, 0.5 * scale, 0.5 * scale));

        // Vector backward from center of car
//        g.draw(new Line2D.Double(c.x * scale, c.y * scale, (c.x - r.x) * scale, (c.y - r.y) * scale));

//        if (this.jump.get()) {
//        	force *= 100;
//        }

		// apply thrust
        if (this.forwardThrustOn.get()) {
        	Vector2 f = r.product(1.5 * force);
        	Vector2 p = c.sum(r.product(0.0));
        	
        	car.applyForce(f);
        	
//        	g.setColor(Color.ORANGE);
//        	g.draw(new Line2D.Double(p.x * scale, p.y * scale, (p.x - f.x) * scale, (p.y - f.y) * scale));
        } 

        // Linear movement
        Vector2 v = car.getLinearVelocity();
        if (!v.isZero()) {
        	Vector2 vx = new Vector2(rrnorm).multiply(v.dot(rrnorm));
        	Vector2 vy = new Vector2(rnorm).multiply(v.dot(rnorm));
        	
       		Vector2 f = new Vector2(v).setMagnitude(force).multiply(-1);
       		car.applyForce(f);

//        	g.draw(new Line2D.Double(c.x * scale, c.y * scale, (c.x - v.x) * scale, (c.y - v.y) * scale));
        	g.draw(new Line2D.Double(c.x * scale, c.y * scale, (c.x - vx.x) * scale, (c.y - vx.y) * scale));
//        	g.draw(new Line2D.Double(c.x * scale, c.y * scale, (c.x - vy.x) * scale, (c.y - vy.y) * scale));
        }

        double angle = 0;
        if (this.leftTurnOn.get()) {
        	angle = Math.PI / 5;
        }
        if (this.rightTurnOn.get()) {
        	angle = -Math.PI / 5;
        }
        
        Vector2 wh = c.sum(r.product(0.5));
//         Circle the front wheel
        g.draw(new java.awt.geom.Ellipse2D.Double((wh.x - 0.5/2) * scale, (wh.y - 0.5/2) * scale, 0.5 * scale, 0.5 * scale));
        Vector2 wh_v = car.getLinearVelocity(wh);
        if (!wh_v.isZero()) {
        	Vector2 myrr = new Vector2(rrnorm).rotate(angle);
        	Vector2 myr = new Vector2(rnorm).rotate(angle);

        	Vector2 vx = myrr.multiply(wh_v.dot(myrr));
        	Vector2 vy = myr.multiply(wh_v.dot(myr));

        	double mforce = force * 0.5;
        	if (vx.getMagnitude()*2 < mforce) {
        		mforce = vx.getMagnitude();
        	}

       		Vector2 f = new Vector2(v).setMagnitude(mforce).multiply(-1);
       		car.applyForce(f);
        	
        	if (vx.getMagnitude() > 1) {
        		Vector2 fx = new Vector2(vx).setMagnitude(force * 0.5).multiply(-1);
        		car.applyForce(fx, wh);
        	}

        	g.draw(new Line2D.Double(wh.x * scale, wh.y * scale, (wh.x - v.x) * scale, (wh.y - v.y) * scale));
        	g.draw(new Line2D.Double(wh.x * scale, wh.y * scale, (wh.x - vx.x) * scale, (wh.y - vx.y) * scale));
//        	g.draw(new Line2D.Double(wh.x * scale, wh.y * scale, (wh.x - vy.x) * scale, (wh.y - vy.y) * scale));
        }

        Vector2 bwh = c.sum(r.product(-0.5));
        // Circle the front wheel
//        g.draw(new java.awt.geom.Ellipse2D.Double((bwh.x - 0.5/2) * scale, (bwh.y - 0.5/2) * scale, 0.5 * scale, 0.5 * scale));
        Vector2 bwh_v = car.getLinearVelocity(bwh);
        if (!bwh_v.isZero()) {
        	Vector2 vx = new Vector2(rrnorm).multiply(bwh_v.dot(rrnorm));
        	Vector2 vy = new Vector2(rnorm).multiply(bwh_v.dot(rnorm));

        	double mforce = force * 0.5;
        	if (vx.getMagnitude() < mforce) {
        		mforce = vx.getMagnitude();
        	}

       		Vector2 f = new Vector2(v).setMagnitude(mforce).multiply(-1);
       		car.applyForce(f);

//        	g.draw(new Line2D.Double(bwh.x * scale, bwh.y * scale, (bwh.x - v.x) * scale, (bwh.y - v.y) * scale));
        	g.draw(new Line2D.Double(bwh.x * scale, bwh.y * scale, (bwh.x - vx.x) * scale, (bwh.y - vx.y) * scale));
//        	g.draw(new Line2D.Double(bwh.x * scale, bwh.y * scale, (bwh.x - vy.x) * scale, (bwh.y - vy.y) * scale));
        }

        

//        if (this.leftTurnOn.get()) {
//        	Vector2 f1 = r.product(force * 0.1).right();
//        	Vector2 f2 = r.product(force * 0.1).left();
//        	Vector2 p1 = c.sum(r.product(0.9));
//        	Vector2 p2 = c.sum(r.product(-0.9));
//        	
//        	// apply a force to the top going left
//        	car.applyForce(f1, p1);
//        	// apply a force to the bottom going right
//        	car.applyForce(f2, p2);
//        	
////        	g.setColor(Color.RED);
////        	g.draw(new Line2D.Double(p1.x * scale, p1.y * scale, (p1.x - f1.x) * scale, (p1.y - f1.y) * scale));
////        	g.draw(new Line2D.Double(p2.x * scale, p2.y * scale, (p2.x - f2.x) * scale, (p2.y - f2.y) * scale));
//        }

//        if (this.rightTurnOn.get()) {
//        	Vector2 f1 = r.product(force * 0.1).left();
//        	Vector2 f2 = r.product(force * 0.1).right();
//        	Vector2 p1 = c.sum(r.product(0.9));
//        	Vector2 p2 = c.sum(r.product(-0.9));
//        	
//        	// apply a force to the top going left
//        	car.applyForce(f1, p1);
//        	// apply a force to the bottom going right
//        	car.applyForce(f2, p2);
//        	
////        	g.setColor(Color.RED);
////        	g.draw(new Line2D.Double(p1.x * scale, p1.y * scale, (p1.x - f1.x) * scale, (p1.y - f1.y) * scale));
////        	g.draw(new Line2D.Double(p2.x * scale, p2.y * scale, (p2.x - f2.x) * scale, (p2.y - f2.y) * scale));
//        }
        
        // sideways damping
//        Vector2 v = car.getLinearVelocity();
//        if (!v.isZero()) {
//        	Vector2 rhov = r.getRightHandOrthogonalVector();
//        	Vector2 rnorm = rhov.multiply(v.dot(rhov)/v.getMagnitude());
//      	  	rnorm.multiply(-1);
////      	 v.dot(c);
////      	 System.out.println(rhov);
////      	 System.out.println(rnorm);
//        	car.applyForce(rnorm);
//        	Vector2 p = c.sum(r.product(0.0));
//        	g.draw(new Line2D.Double(p.x * scale, p.y * scale, (p.x - rnorm.x) * scale, (p.y - rnorm.y) * scale));
//        	g.draw(new Line2D.Double(p.x * scale, p.y * scale, (p.x - r.x) * scale, (p.y - r.y) * scale));
//        }

//        Vector2 vl = car.getLinearVelocity(new Vector2(0.9, 0.5));
////        double va = car.getAngularVelocity();
//        double theta = 0;
//        if (!vl.isZero()) {
//        	Vector2 frictionDir = r.getRightHandOrthogonalVector().rotate(theta);
//        	double sc = frictionDir.dot(vl) * frictionDir.getMagnitude();
//        	Vector2 f = frictionDir.multiply(sc);
//        	Vector2 p = c.sum(r.product(0.9));
//        	
//        	// apply a force to the bottom going right
//        	car.applyForce(f, p);
//        	
//        	g.draw(new Line2D.Double(p.x * scale, p.y * scale, (p.x - f.x) * scale, (p.y - f.y) * scale));
//        }
        
//        Vector2 velocity = car.getLinearVelocity();
//        velocity.multiply(0.98);
//        car.setLinearVelocity(velocity);
//
//        double av = car.getAngularVelocity();
//        av *= 0.98;
//        car.setAngularVelocity(av);
	}
}
