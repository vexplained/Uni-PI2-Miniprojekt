package de.grotefober.nbodysim.sim.physObjects;

import de.grotefober.nbodysim.sim.PhysicsObject;
import de.grotefober.nbodysim.sim.Vector2D;

/**
 * @deprecated Unimplemented. Left as an exercise for the intrigued ;)
 */
@Deprecated
public class PhysCircularMass extends PhysicsObject
{

	public PhysCircularMass(double mass)
	{
		super(mass);
		// TODO Auto-generated constructor stub
	}

	@Override
	public double getGravPotential(Vector2D p)
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Vector2D calcExcertedForce(PhysicsObject other)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Vector2D calcAcceleration(Vector2D force)
	{
		// TODO Auto-generated method stub
		return null;
	}

}