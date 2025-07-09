package de.grotefober.nbodysim.sim.physObjects;

import de.grotefober.nbodysim.sim.PhysicsConstants;
import de.grotefober.nbodysim.sim.PhysicsObject;
import de.grotefober.nbodysim.sim.Vector2D;
import de.grotefober.nbodysim.util.Vector2DUtils;

public class PhysPointMass extends PhysicsObject
{

	public PhysPointMass(double mass)
	{
		super(mass, new Vector2D.Double());
	}

	public PhysPointMass(double mass, Vector2D initialPosition)
	{
		super(mass, initialPosition, new Vector2D.Double());
	}

	public PhysPointMass(double mass, Vector2D initialPosition, Vector2D initialVelocity)
	{
		super(mass, initialPosition, initialVelocity);
	}

	@Override
	public double getGravPotential(Vector2D p)
	{
		return -PhysicsConstants.G * this.mass / this.getPosition().distance(p);
	}

	@Override
	public Vector2D calcExcertedForce(PhysicsObject other)
	{
		// r_2 - r_1 (=> sign determines direction)
		Vector2D diff = other.getPosition().subtract(this.getPosition());
		// (F = c / r^2 e_r = c / r^3 * r_vector)
		return diff.scale(PhysicsConstants.G * this.getMass() * other.getMass()
				/ Vector2DUtils.distCubed(other.getPosition(), this.getPosition())); // distCubed since we are scaling
																					 // the vector pointing from this to
																					 // the other object
	}

	@Override
	public Vector2D calcAcceleration(Vector2D force)
	{
		return force.scale(1d / getMass());
	}

	// @Override
	// public Vector2D calcAcceleration(PhysicsObject other)
	// {
	// // more efficient to omit factor this#getMass instead of using #calcExcertedForce
	// Vector2D diff = this.getPosition().subtract(other.getPosition());
	// // (F = c / r^2 e_r = c / r^3 * r_vector)
	// return diff.scale(PhysicsConstants.G * other.getMass()
	// / Vector2DUtils.distCubed(this.getPosition(), other.getPosition()));
	// }

}
