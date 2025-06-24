package de.grotefober.nbodysim.sim;

import java.util.ArrayList;

// TODO: Refactor to record (performance improvement? Smaller instantiation overhead)
public abstract class PhysicsObject implements PhysHeavyMass, PhysInertialMass
{

	/**
	 * Contains all {@link PhysicsObject}s of this' object's parent universe.
	 * <br>
	 * FIXME: Perhaps refactor to contain only reference to parent container / PhysicsController -> reduce duplicate
	 * memory (same List for every object)
	 */
	protected ArrayList<PhysicsObject> parentUniverse;

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
	 * Update this {@code PhysicsObject}'s acceleration using Newton's second law (F = m*a).
	 */
	public abstract void updateAcceleration(Vector2D[] forces);

	/**
	 * Update this {@code PhysicsObject}'s velocity based on its current acceleration using the explicit Euler method
	 * (<a href="https://en.wikipedia.org/wiki/Euler_method">Wikipedia: Euler method</a>).
	 * 
	 * TODO: Perhaps refactor to use more accurate approximation, e.g. predictor-corrector method.
	 * Or using the midpoint-method / improved explicit Euler-method:
	 * <a href=
	 * "https://de.wikipedia.org/wiki/Explizites_Euler-Verfahren#Verbessertes_explizites_Euler-Verfahren">Wiki</a>
	 * <img style="background: white" src=
	 * "https://wikimedia.org/api/rest_v1/media/math/render/svg/954d4d6d4db5080168559eb2741838c6b81e03d7"></img>
	 * <br>
	 * Note: In the image above, h is <code>timeStep</code>.
	 * 
	 * @param timeStep
	 *            the time step to integrate over, in seconds.
	 */
	public abstract void updateVelocity(double timeStep);

	/**
	 * Update this {@code PhysicsObject}'s position based on its current velocity using the explicit Euler method
	 * (<a href="https://en.wikipedia.org/wiki/Euler_method">Wikipedia: Euler method</a>).
	 * 
	 * TODO: Perhaps refactor to use more accurate approximation, e.g. predictor-corrector method.
	 * 
	 * @param timeStep
	 *            the time step to integrate over, in seconds.
	 */
	public abstract void updatePosition(double timeStep);

	/**
	 * Updates all physics-related properties in the correct order using the given time delta <code>timeStep</code>.
	 */
	public abstract void tickPhysics(double timeSteps);

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
