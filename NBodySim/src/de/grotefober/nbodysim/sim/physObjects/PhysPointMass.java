package de.grotefober.nbodysim.sim.physObjects;

import de.grotefober.nbodysim.sim.PhysicsConstants;
import de.grotefober.nbodysim.sim.PhysicsObject;
import de.grotefober.nbodysim.sim.Vector2D;
import de.grotefober.nbodysim.util.Vector2DUtils;

public class PhysPointMass extends PhysicsObject
{

	@Override
	public double getGravPotential(Vector2D p)
	{
		return -PhysicsConstants.G * this.mass / this.getLocation().distance(p);
	}

	@Override
	public Vector2D calcExcertedForce(PhysicsObject other)
	{
		Vector2D diff = this.getLocation().subtract(other.getLocation());
		return diff.scale(PhysicsConstants.G * this.getMass() * other.getMass()
				/ Vector2DUtils.distCubed(this.getLocation(), other.getLocation()));
	}

	@Override
	public Vector2D calcAcceleration(Vector2D force)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateAcceleration(Vector2D force)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void updateVelocity(double timeStep)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void updatePosition(double timeStep)
	{
		// TODO Auto-generated method stub

	}

}
