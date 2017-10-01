package org.dyn4j.samples;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;

import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.geometry.Geometry;
import org.dyn4j.geometry.Mass;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Vector2;

public class Ship extends SimulationBody {

	public Ship() {
		super();
		
		double friction = 0.5;
		double bounce = 0.5;
		
		this.addFixture(Geometry.createRectangle(0.5, 1.5), 1, friction, bounce);
		this.addFixture(Geometry.createEquilateralTriangle(0.25), 1, friction, bounce);

		BodyFixture head = this.addFixture(Geometry.createRectangle(0.5, 0.1), 1, friction, bounce);
		head.getShape().translate(0, 0.8);
		head.setUserData(new DynData());

		this.translate(0.0, 2.0);
		this.setMass(MassType.NORMAL);
//		this.setMass(new Mass(new Vector2(0, 0), 1, 0.5));

		this.setAngularDamping(5.0f);
		this.setLinearDamping(2.0f);
	}
	
	public void thrust(Graphics2D g, double force, double scale) {
        final Vector2 r = new Vector2(this.getTransform().getRotation() + Math.PI * 0.5);
        final Vector2 c = this.getWorldCenter();

       	Vector2 f = r.product(force);
       	Vector2 p = c.sum(r.product(-0.9));
        	
       	applyForce(f);
       	
//       	g.setColor(Color.ORANGE);
//       	g.draw(new Line2D.Double(p.x * scale, p.y * scale, (p.x - f.x) * scale, (p.y - f.y) * scale));
	}

	public void rotate(Graphics2D g, double force, double scale) {
        final Vector2 r = new Vector2(this.getTransform().getRotation() + Math.PI * 0.5);
        final Vector2 c = this.getWorldCenter();

        Vector2 f1 = r.product(force * 0.05).right();
        Vector2 f2 = r.product(force * 0.05).left();
        Vector2 p1 = c.sum(r.product(0.9));
        Vector2 p2 = c.sum(r.product(-0.9));
        	
        // apply a force to the top going left
        this.applyForce(f1, p1);
        // apply a force to the bottom going right
        this.applyForce(f2, p2);
        	
        g.setColor(Color.RED);
//        g.draw(new Line2D.Double(p1.x * scale, p1.y * scale, (p1.x - f1.x) * scale, (p1.y - f1.y) * scale));
//        g.draw(new Line2D.Double(p2.x * scale, p2.y * scale, (p2.x - f2.x) * scale, (p2.y - f2.y) * scale));
	}
}
