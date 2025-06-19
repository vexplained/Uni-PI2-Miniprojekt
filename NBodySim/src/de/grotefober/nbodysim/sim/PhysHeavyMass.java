package de.grotefober.nbodysim.sim;

/**
 * An interface specifying that objects implementing this interface are "heavy", meaning they create a gravitational
 * field around them and thus excert a force on other heavy masses.
 */
public interface PhysHeavyMass
{
	/**
	 * Calculate the gravity potential at the given point p in space.
	 * 
	 * @param p
	 *            the point (as {@code Vector2D}) in space for which to calculate the gravitational potential
	 * @return the gravity potential at the specified point
	 */
	double getGravPotential(Vector2D p);

	/**
	 * Calculate the force excerted on a {@code PhysHeavyMass} by this {@code PhysHeavyMass} using Newton's third law.
	 * 
	 * @param other
	 *            the other mass
	 * @return the force vector resulting of the action-reaction principle.
	 */
	Vector2D calcExcertedForce(PhysicsObject other);
}
