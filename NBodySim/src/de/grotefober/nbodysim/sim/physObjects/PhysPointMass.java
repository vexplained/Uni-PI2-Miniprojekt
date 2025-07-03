package de.grotefober.nbodysim.sim.physObjects;

import de.grotefober.nbodysim.sim.PhysicsConstants;
import de.grotefober.nbodysim.sim.PhysicsObject;
import de.grotefober.nbodysim.sim.Vector2D;
import de.grotefober.nbodysim.sim.Vector2D.Double.Mutable;
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
		// FIXME F=ma => a = F/m nicht F*m
		return force.scale(getMass());
	}

	@Override
	public void updateAcceleration(Vector2D force)
	{
		this.setAcceleration(calcAcceleration(force));
	}

	@Override
	public void updateAcceleration(Vector2D[] forces)
	{
		Vector2D.Double.Mutable forceResult = new Vector2D.Double.Mutable();
		for (Vector2D force : forces)
		{
			forceResult.add(force);
		}
		this.setAcceleration(forceResult);
	}

	@Override
	public void updateVelocity(double timeStep)
	{
		// Vector2D vOld = (Vector2D) getVelocity().clone();

		// TODO optimize to use mutable operations?
		// Vector2D vNew = vOld.add(this.getAcceleration().scale(timeStep));
		((Mutable) this.velocity).add(this.acceleration.scale(timeStep));
	}

	@Override
	public void updatePosition(double timeStep)
	{
		((Mutable) this.location).add(this.velocity.scale(timeStep));
	}

	@Override
	public void tickPhysics(double timeSteps)
	{
		// TODO Auto-generated method stub

	}
}
