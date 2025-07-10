package de.grotefober.nbodysim.ui.graphics.dynObjects;

import java.awt.Graphics2D;

import de.grotefober.nbodysim.sim.IPhysicsTickable;
import de.grotefober.nbodysim.sim.PhysicsManager;
import de.grotefober.nbodysim.sim.PhysicsObject;
import de.vexplained.libraries.cvs_graphics_library.stdGraphics.DynamicObject;
import de.vexplained.libraries.cvs_graphics_library.stdGraphics.IDynamicComponent;
import de.vexplained.libraries.cvs_graphics_library.stdGraphics.IDynamicContainer;

/**
 * Compound class for attaching a {@link DynamicObject} or any of its subclasses and its physics shadow (any class
 * extending {@link PhysicsObject}) to each other.
 */
public final class DynamicPhysicsObject<DynObj extends DynamicObject, PhysObj extends PhysicsObject>
		implements IDynamicComponent, IPhysicsTickable
{
	private final DynObj dynamicObject;
	private final PhysObj physicsShadow;

	IDynamicContainer<? extends IDynamicComponent> parentCanvas;

	private final double distanceFactor;

	/**
	 * For initial position, velocity and acceleration, the <code>physicsObject</code>'s values are preserved and the
	 * <code>dynamicObject</code> is synchronized to match the physicsObject's position corrected by the
	 * <code>distanceFactor</code>.
	 * <br>
	 * The relation between the position of the dynamic object and the physics object is as follows:
	 *
	 * <pre>
	 * dynamicObject#position = (1 / distanceFactor) * physicsObject#position
	 * </pre>
	 * 
	 * @param distanceFactor
	 *            the factor to multiply distances by. It may be used if the distance vectors represent pixel values
	 *            instead of physical distances.
	 */
	public DynamicPhysicsObject(DynObj dynamicObject, PhysObj physicsObject, double distanceFactor)
	{
		this.dynamicObject = dynamicObject;
		this.physicsShadow = physicsObject;
		this.distanceFactor = distanceFactor;

		syncDynamicObject();
	}

	public DynObj getDynamicObject()
	{
		return dynamicObject;
	}

	public PhysObj getPhysicsObject()
	{
		return physicsShadow;
	}

	@Override
	public void setParentCanvas(IDynamicContainer parent)
	{
		this.parentCanvas = parent;
		this.dynamicObject.setParentCanvas(parent);
	}

	@Override
	public IDynamicContainer getParentCanvas()
	{
		return this.dynamicObject.getParentCanvas();
	}

	@Override
	public boolean isVisible()
	{
		return this.dynamicObject.isVisible();
	}

	@Override
	public void setVisible(boolean visible)
	{
		this.dynamicObject.setVisible(visible);
	}

	@Override
	public void draw(Graphics2D g2d)
	{
		this.dynamicObject.draw(g2d);
	}

	@Override
	public void invalidate()
	{
		this.dynamicObject.invalidate();
	}

	@Override
	public void tickPhysics(PhysicsManager physMan)
	{
		this.physicsShadow.tickPhysics(physMan);
	}

	@Override
	public void tickAcceleration(PhysicsManager physMan)
	{
		this.physicsShadow.tickAcceleration(physMan);
	}

	@Override
	public void tickVelocity(PhysicsManager physMan)
	{
		this.physicsShadow.tickVelocity(physMan);
	}

	@Override
	public void tickPosition(PhysicsManager physMan)
	{
		this.physicsShadow.tickPosition(physMan);
	}

	/**
	 * Updates the dynamic object's position to reflect the state of its physics shadow.
	 */
	public void syncDynamicObject()
	{
		dynamicObject.setX(physicsShadow.getPosition().getX() / distanceFactor);
		dynamicObject.setY(physicsShadow.getPosition().getY() / distanceFactor);
	}

	public double getDistanceFactor()
	{
		return distanceFactor;
	}

}
