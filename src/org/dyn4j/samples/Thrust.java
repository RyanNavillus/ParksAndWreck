/*
 * Copyright (c) 2010-2016 William Bittle  http://www.dyn4j.org/
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification, are permitted 
 * provided that the following conditions are met:
 * 
 *   * Redistributions of source code must retain the above copyright notice, this list of conditions 
 *     and the following disclaimer.
 *   * Redistributions in binary form must reproduce the above copyright notice, this list of conditions 
 *     and the following disclaimer in the documentation and/or other materials provided with the 
 *     distribution.
 *   * Neither the name of dyn4j nor the names of its contributors may be used to endorse or 
 *     promote products derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR 
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND 
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR 
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL 
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, 
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER 
 * IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT 
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.dyn4j.samples;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Line2D;
import java.util.concurrent.atomic.AtomicBoolean;

import org.dyn4j.collision.narrowphase.Penetration;
import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.dynamics.CollisionAdapter;
import org.dyn4j.geometry.Geometry;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Vector2;

/**
 * Moderately complex scene of a rocket that has propulsion at various points
 * to allow control.  Control is given by the left, right, up, and down keys
 * and applies forces when pressed.
 * @author William Bittle
 * @version 3.2.1
 * @since 3.2.0
 */
public class Thrust extends SimulationFrame {
	/** The serial version id */
	private static final long serialVersionUID = 3770932661470247325L;
	
	private int num_of_ships;

	/** The controlled ship */
	private Ship[] ships;
	
	// Some booleans to indicate that a key is pressed
	
	private AtomicBoolean[] forwardThrustOn;
	private AtomicBoolean[] reverseThrustOn;
	private AtomicBoolean[] leftThrustOn;
	private AtomicBoolean[] rightThrustOn;
	
	private static class StopContactListener extends CollisionAdapter {
		private Body b1, b2;
		
		public StopContactListener(Body b1, Body b2) {
			this.b1 = b1;
			this.b2 = b2;
		}
		@Override
		public boolean collision(Body body1, BodyFixture fixture1, Body body2, BodyFixture fixture2, Penetration penetration) {
			// the bodies can appear in either order

			if ((body1 == b1 && body2 == b2) ||
			    (body1 == b2 && body2 == b1)) {
				// its the collision we were looking for
				// do whatever you need to do here
				
				Vector2 impulse1 = null;
				Vector2 impulse2 = null;
				
				double minImpulse = 2;

				Object userData1 = fixture1.getUserData();
				if (userData1 != null && userData1 instanceof DynData) {
					DynData data1 = (DynData) userData1;
					if (data1.isHead()) {
						impulse1 = new Vector2(body1.getLinearVelocity());
						if (impulse1.getMagnitude() < minImpulse) {
							impulse1.setMagnitude(minImpulse);
						}
						body2.applyImpulse(impulse1);
					}
				}

				Object userData2 = fixture2.getUserData();
				if (userData2 != null && userData2 instanceof DynData) {
					DynData data2 = (DynData) userData2;
					if (data2.isHead()) {
						impulse2 = new Vector2(body2.getLinearVelocity()).multiply(1);
						if (impulse2.getMagnitude() < minImpulse) {
							impulse2.setMagnitude(minImpulse);
						}
						body1.applyImpulse(impulse2);
					}
				}
				
				return impulse1 == null && impulse2 == null;
			}
			return true;
		}
	}
	
	/**
	 * Custom key adapter to listen for key events.
	 * @author William Bittle
	 * @version 3.2.1
	 * @since 3.2.0
	 */
	private class CustomKeyListener extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent e) {
			if (num_of_ships > 0) {
				switch (e.getKeyCode()) {
					case KeyEvent.VK_UP:
						forwardThrustOn[0].set(true);
						break;
					case KeyEvent.VK_DOWN:
						reverseThrustOn[0].set(true);
						break;
					case KeyEvent.VK_LEFT:
						leftThrustOn[0].set(true);
						break;
					case KeyEvent.VK_RIGHT:
						rightThrustOn[0].set(true);
						break;
			    }
			}
			if (num_of_ships > 1) {
				switch (e.getKeyCode()) {
					case KeyEvent.VK_W:
						forwardThrustOn[1].set(true);
						break;
					case KeyEvent.VK_S:
						reverseThrustOn[1].set(true);
						break;
					case KeyEvent.VK_A:
						leftThrustOn[1].set(true);
						break;
					case KeyEvent.VK_D:
						rightThrustOn[1].set(true);
						break;
			    }
			}
			
		}
		
		@Override
		public void keyReleased(KeyEvent e) {
			if (num_of_ships > 0) {
			switch (e.getKeyCode()) {
				case KeyEvent.VK_UP:
					forwardThrustOn[0].set(false);
					break;
				case KeyEvent.VK_DOWN:
					reverseThrustOn[0].set(false);
					break;
				case KeyEvent.VK_LEFT:
					leftThrustOn[0].set(false);
					break;
				case KeyEvent.VK_RIGHT:
					rightThrustOn[0].set(false);
					break;
			}
			}
			
			if (num_of_ships > 1) {
				switch (e.getKeyCode()) {
					case KeyEvent.VK_W:
						forwardThrustOn[1].set(false);
						break;
					case KeyEvent.VK_S:
						reverseThrustOn[1].set(false);
						break;
					case KeyEvent.VK_A:
						leftThrustOn[1].set(false);
						break;
					case KeyEvent.VK_D:
						rightThrustOn[1].set(false);
						break;
				}
			}
		}
	}
	
	/**
	 * Default constructor.
	 */
	public Thrust() {
		super("Thrust", 64.0);
		
		KeyListener listener = new CustomKeyListener();
		this.addKeyListener(listener);
		this.canvas.addKeyListener(listener);
	}
	
	/**
	 * Creates game objects and adds them to the world.
	 */
	protected void initializeWorld() {
		this.world.setGravity(new Vector2(0, 0));
		
		// create all your bodies/joints
		double offset = 5;
		
		// the bounds so we can keep playing
		SimulationBody l = new SimulationBody();
		l.addFixture(Geometry.createRectangle(1, offset * 15));
		l.translate(-offset, 0);
		l.setMass(MassType.INFINITE);
		this.world.addBody(l);
		
		SimulationBody r = new SimulationBody();
		r.addFixture(Geometry.createRectangle(1, offset * 3));
		r.translate(offset, 0);
		r.setMass(MassType.INFINITE);
		this.world.addBody(r);
		
		SimulationBody t = new SimulationBody();
		t.addFixture(Geometry.createRectangle(offset * 3, 1));
		t.translate(0, offset);
		t.setMass(MassType.INFINITE);
		this.world.addBody(t);
		
		SimulationBody b = new SimulationBody();
		b.addFixture(Geometry.createRectangle(offset * 3, 1));
		b.translate(0, -offset);
		b.setMass(MassType.INFINITE);
		this.world.addBody(b);
		
		// the ship
		this.num_of_ships = 2;
		this.ships = new Ship[this.num_of_ships];
		this.forwardThrustOn = new AtomicBoolean[num_of_ships];
		this.reverseThrustOn = new AtomicBoolean[num_of_ships];
		this.leftThrustOn = new AtomicBoolean[num_of_ships];
		this.rightThrustOn = new AtomicBoolean[num_of_ships];

		for (int i = 0; i < ships.length; i++) {
			this.ships[i] = new Ship();
			this.world.addBody(ships[i]);
			this.forwardThrustOn[i] = new AtomicBoolean(false);
			this.reverseThrustOn[i] = new AtomicBoolean(false);
			this.leftThrustOn[i] = new AtomicBoolean(false);
			this.rightThrustOn[i] = new AtomicBoolean(false);
			
			for (int j = 0; j < i; j++) {
				this.world.addListener(new StopContactListener(ships[i], ships[j]));
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see org.dyn4j.samples.SimulationFrame#update(java.awt.Graphics2D, double)
	 */
	@Override
	protected void update(Graphics2D g, double elapsedTime) {
		super.update(g, elapsedTime);
		
		final double scale = this.scale;
		final double force = 5000 * elapsedTime;
		
        final Vector2 r = new Vector2(ships[0].getTransform().getRotation() + Math.PI * 0.5);
        final Vector2 c = ships[0].getWorldCenter();
		
		// apply thrust
			
		if (num_of_ships > 0) {
            if (this.forwardThrustOn[0].get()) {
            	ships[0].thrust(g, force, scale);
            } 

            if (this.reverseThrustOn[0].get()) {
        	    ships[0].thrust(g, -force, scale);
            }

            if (this.leftThrustOn[0].get()) {
            	ships[0].rotate(g, force, scale);
            }

            if (this.rightThrustOn[0].get()) {
            	ships[0].rotate(g, -force, scale);
            }
		}

		if (num_of_ships > 1) {
            if (this.forwardThrustOn[1].get()) {
            	ships[1].thrust(g, force, scale);
            } 

            if (this.reverseThrustOn[1].get()) {
        	    ships[1].thrust(g, -force, scale);
            }

            if (this.leftThrustOn[1].get()) {
            	ships[1].rotate(g, force, scale);
            }

            if (this.rightThrustOn[1].get()) {
            	ships[1].rotate(g, -force, scale);
            }
		}
	}
	
	/**
	 * Entry point for the example application.
	 * @param args command line arguments
	 */
	public static void main(String[] args) {
		Thrust simulation = new Thrust();
		simulation.run();
	}
}
