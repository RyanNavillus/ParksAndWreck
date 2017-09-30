package game;

import org.dyn4j.collision.Fixture;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.dynamics.World;
import org.dyn4j.geometry.Geometry;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Vector2;
import org.dyn4j.samples.SimulationBody;

import com.sun.xml.internal.ws.client.sei.ResponseBuilder.Body;

public class SimulationWorld {

	private World world;
	public SimulationWorld(String name) {
		world = new World();
		initializeWorld();
	}

	protected void initializeWorld() {
		this.world.setGravity(new Vector2(0, 0));
		
		// create all your bodies/joints
		
		// the bounds so we can keep playing
		SimulationBody l = new SimulationBody();
		l.addFixture(Geometry.createRectangle(1, 15));
		l.translate(-5, 0);
		l.setMass(MassType.INFINITE);
		this.world.addBody(l);
		
		SimulationBody r = new SimulationBody();
		r.addFixture(Geometry.createRectangle(1, 15));
		r.translate(5, 0);
		r.setMass(MassType.INFINITE);
		this.world.addBody(r);
		
		SimulationBody t = new SimulationBody();
		t.addFixture(Geometry.createRectangle(15, 1));
		t.translate(0, 5);
		t.setMass(MassType.INFINITE);
		this.world.addBody(t);
		
		SimulationBody b = new SimulationBody();
		b.addFixture(Geometry.createRectangle(15, 1));
		b.translate(0, -5);
		b.setMass(MassType.INFINITE);
		this.world.addBody(b);
		
		// the ship
		SimulationBody ship = new SimulationBody();
		ship.addFixture(Geometry.createRectangle(0.5, 1.5), 1, 0.2, 0.2);
		BodyFixture bf2 = ship.addFixture(Geometry.createEquilateralTriangle(0.5), 1, 0.2, 0.2);
		bf2.getShape().translate(0, 0.9);
		ship.translate(0.0, 2.0);
		ship.setMass(MassType.NORMAL);
		
		this.world.addBody(ship);
	}
	
	public void render() 
	{
		for (org.dyn4j.dynamics.Body body : world.getBodies()) 
		{
			if (body instanceof SimulationBody) 
			{
				SimulationBody simulationBody = (SimulationBody) body;
			}
		}
	}

}
