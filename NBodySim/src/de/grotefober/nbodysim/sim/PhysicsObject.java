package de.grotefober.nbodysim.sim;

public abstract class PhysicsObject implements PhysHeavyMass, PhysInertialMass
{

	/**
	 * The mass of this {@code PhysicsObject}
	 */
	protected double mass;

	/**
	 * The location of this {@code PhysicsObject}
	 */
	protected Vector2D location;

	/**
	 * The velocity of this {@code PhysicsObject}
	 */
	protected Vector2D velocity;

	/**
	 * The acceleration of this {@code PhysicsObject}
	 */
	protected Vector2D acceleration;

	/**
	 * Update this {@code PhysicsObject}'s acceleration using Newton's second law (F = m*a).
	 */
	public abstract void updateAcceleration(Vector2D force);

	/**
	 * Update this {@code PhysicsObject}'s velocity based on its current acceleration using the explicit Euler method
	 * (<a href="https://en.wikipedia.org/wiki/Euler_method">Wikipedia: Euler method</a>).
	 * 
	 * @param timeStep
	 *            the time step to integrate over, in seconds.
	 */
	public abstract void updateVelocity(double timeStep);

	/**
	 * Update this {@code PhysicsObject}'s position based on its current velocity using the explicit Euler method
	 * (<a href="https://en.wikipedia.org/wiki/Euler_method">Wikipedia: Euler method</a>).
	 * 
	 * @param timeStep
	 *            the time step to integrate over, in seconds.
	 */
	public abstract void updatePosition(double timeStep);

	public double getMass()
	{
		return mass;
	}

	public void setMass(double mass)
	{
		this.mass = mass;
	}

	public Vector2D getLocation()
	{
		return location;
	}

	public void setLocation(Vector2D location)
	{
		this.location = location;
	}

	public Vector2D getVelocity()
	{
		return velocity;
	}

	public void setVelocity(Vector2D velocity)
	{
		this.velocity = velocity;
	}

	public Vector2D getAcceleration()
	{
		return acceleration;
	}

	public void setAcceleration(Vector2D acceleration)
	{
		this.acceleration = acceleration;
	}

}
