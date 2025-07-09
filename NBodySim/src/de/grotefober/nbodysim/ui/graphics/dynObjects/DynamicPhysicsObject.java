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

	public DynamicPhysicsObject(DynObj dynamicObject, PhysObj physicsObject)
	{
		this.dynamicObject = dynamicObject;
		this.physicsShadow = physicsObject;
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
		dynamicObject.setX(physicsShadow.getPosition().getX());
		dynamicObject.setY(physicsShadow.getPosition().getY());
	}

}
