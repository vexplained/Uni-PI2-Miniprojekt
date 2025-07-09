package de.grotefober.nbodysim.sim;

/**
 * An interface specifying that objects implementing this interface are inert, meaning they resist to changes in
 * velocity depending on their mass.
 */
public interface PhysInertialMass
{
	/**
	 * Calculate the acceleration vector excerted on this inertial mass.
	 * 
	 * @return the acceleration excerted on this physics object.
	 */
	Vector2D calcAcceleration(Vector2D force);

	// public Vector2D calcAcceleration(PhysicsObject other);
}
