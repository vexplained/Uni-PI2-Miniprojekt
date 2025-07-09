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
		Vector2D diff = this.getPosition().subtract(other.getPosition());
		return diff.scale(PhysicsConstants.G * this.getMass() * other.getMass()
				/ Vector2DUtils.distCubed(this.getPosition(), other.getPosition()));
	}

	@Override
	public Vector2D calcAcceleration(Vector2D force)
	{
		return force.scale(1d / getMass());
	}

}
