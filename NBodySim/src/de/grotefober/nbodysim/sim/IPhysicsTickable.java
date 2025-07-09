package de.grotefober.nbodysim.sim;

import de.vexplained.libraries.cvs_graphics_library.stdGraphics.ITickable;

public interface IPhysicsTickable extends ITickable
{
	/**
	 * <b>Use with caution!</b> This method is not intended to be used during simulation of multiple bodies. When doing
	 * so, update each property of all bodies in succession in the following order:
	 * <ol>
	 * <li>{@link #tickAcceleration(PhysicsManager)} of all objects,
	 * <li>{@link #tickVelocity(PhysicsManager)} of all objects,
	 * <li>{@link #tickPosition(PhysicsManager)} of all objects.
	 * </ol>
	 */
	public void tickPhysics(PhysicsManager physMan);

	/**
	 * Updates the acceleration vector of this object.
	 */
	public void tickAcceleration(PhysicsManager physMan);

	public void tickVelocity(PhysicsManager physMan);

	public void tickPosition(PhysicsManager physMan);

	/**
	 * @deprecated Not implemented. Use {@link #tickPhysics(PhysicsManager)} instead.
	 */
	@Override
	@Deprecated
	public default void tick()
	{
	}

}
