package de.grotefober.nbodysim.sim;

import de.grotefober.nbodysim.sim.Vector2D.Double.Mutable;

// TODO: Refactor to record (performance improvement? Smaller instantiation overhead)
public abstract class PhysicsObject implements PhysHeavyMass, PhysInertialMass, IPhysicsTickable
{

	// /**
	// * Contains all {@link PhysicsObject}s of this' object's parent universe.
	// * <br>
	// * FIXME: Perhaps refactor to contain only reference to parent container / PhysicsController -> reduce duplicate
	// * memory (same List for every object)
	// */
	// protected ArrayList<PhysicsObject> parentUniverse;

	/**
	 * The mass of this {@code PhysicsObject}
	 */
	protected double mass;

	/**
	 * The position of this {@code PhysicsObject}
	 */
	protected Vector2D position;

	/**
	 * The velocity of this {@code PhysicsObject}
	 */
	protected Vector2D velocity;

	/**
	 * The acceleration of this {@code PhysicsObject}
	 */
	protected Vector2D acceleration;

	/**
	 * If {@code true}, this object stays fixed in space, i.e. its acceleration, speed or position will not be affected
	 * by forces applied on this object by surrounding {@link PhysicsObject}s. Nonetheless, if its acceleration or
	 * velocity values are set manually, this object may move upon calling
	 */
	protected boolean isFixed;

	/**
	 * Update this {@code PhysicsObject}'s acceleration using Newton's second law (F = m*a).
	 */
	public void updateAcceleration(Vector2D force)
	{
		this.setAcceleration(calcAcceleration(force));
	}

	/**
	 * Update this {@code PhysicsObject}'s acceleration using Newton's second law (F = m*a).
	 */
	public void updateAcceleration(Vector2D[] forces)
	{
		Vector2D.Double.Mutable forceResult = new Vector2D.Double.Mutable();
		for (Vector2D force : forces)
		{
			forceResult.add(force);
		}
		this.setAcceleration(calcAcceleration(forceResult));
	}

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
	public void updateVelocity(double timeStep)
	{
		// Vector2D vOld = (Vector2D) getVelocity().clone();

		// TODO optimize to use mutable operations?
		// Vector2D vNew = vOld.add(this.getAcceleration().scale(timeStep));
		((Mutable) this.velocity).add(this.acceleration.scale(timeStep));
	}

	/**
	 * Update this {@code PhysicsObject}'s position based on its current velocity using the explicit Euler method
	 * (<a href="https://en.wikipedia.org/wiki/Euler_method">Wikipedia: Euler method</a>).
	 * 
	 * TODO: Perhaps refactor to use more accurate approximation, e.g. predictor-corrector method.
	 * 
	 * @param timeStep
	 *            the time step to integrate over, in seconds.
	 */
	public void updateLocation(double timeStep)
	{
		((Mutable) this.position).add(this.velocity.scale(timeStep));
	}

	/**
	 * Updates the acceleration vector of this object, if it is not declared as fixed (see {@link #isFixed()}).
	 */
	@Override
	public void tickAcceleration(PhysicsManager physMan)
	{
		if (isFixed)
		{
			return;
		}

		Vector2D.Double.Mutable forceResult = new Vector2D.Double.Mutable();
		for (PhysicsObject obj : physMan.getPhysicsShadows())
		{
			forceResult.add(this.calcExcertedForce(obj));
		}

		updateAcceleration(forceResult);
	}

	@Override
	public void tickVelocity(PhysicsManager physMan)
	{
		updateVelocity(physMan.getSimulationTimeStep());

	}

	@Override
	public void tickPosition(PhysicsManager physMan)
	{
		updateLocation(physMan.getSimulationTimeStep());
	}

	@Override
	public void tickAll(PhysicsManager physMan)
	{
		tickAcceleration(physMan);
		tickVelocity(physMan);
		tickPosition(physMan);
	}

	public double getMass()
	{
		return mass;
	}

	public void setMass(double mass)
	{
		this.mass = mass;
	}

	public Vector2D getPosition()
	{
		return position;
	}

	public void setPosition(Vector2D position)
	{
		this.position = position;
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

	/**
	 * If {@code true}, this object stays fixed in space, i.e. its acceleration, speed or position will not be affected
	 * by forces applied on this object by surrounding {@link PhysicsObject}s. Nonetheless, if its acceleration or
	 * velocity values are set manually, this object may move upon calling
	 */
	public boolean isFixed()
	{
		return isFixed;
	}

	/**
	 * @see #isFixed()
	 */
	public void setFixed(boolean isFixed)
	{
		this.isFixed = isFixed;
	}
}
