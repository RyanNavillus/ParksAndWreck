package org.dyn4j.ex;

import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.geometry.Geometry;
import org.dyn4j.geometry.MassType;
import org.dyn4j.samples.SimulationBody;

public class Car extends SimulationBody {
	
	public Car() {
		this.addFixture(Geometry.createRectangle(0.5, 1.5), 1, 0.2, 0.2);
		BodyFixture bf2 = this.addFixture(Geometry.createEquilateralTriangle(0.5), 1, 0.2, 0.2);
		bf2.getShape().translate(0, 0.9);
		this.translate(0.0, 2.0);
		this.setMass(MassType.NORMAL);

//		this.setAngularDamping(0.9f);
//		this.setLinearDamping(0.5f);
	}

}
