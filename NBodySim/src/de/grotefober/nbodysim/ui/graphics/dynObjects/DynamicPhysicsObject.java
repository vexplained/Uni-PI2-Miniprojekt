package de.grotefober.nbodysim.ui.graphics.dynObjects;

import java.awt.Graphics2D;

import de.grotefober.nbodysim.sim.PhysicsObject;
import de.vexplained.libraries.cvs_graphics_library.stdGraphics.DynamicObject;
import de.vexplained.libraries.cvs_graphics_library.stdGraphics.IDynamicComponent;
import de.vexplained.libraries.cvs_graphics_library.stdGraphics.IDynamicContainer;

/**
 * Compound class for attaching a {@link DynamicObject} or any of its subclasses and its physics shadow (any class
 * extending {@link PhysicsObject}) to each other.
 */
public final class DynamicPhysicsObject<DynObj extends DynamicObject, PhysObj extends PhysicsObject>
		implements IDynamicComponent
{
	private final DynObj dynamicObject;
	private final PhysObj physicsObject;

	IDynamicContainer<DynamicPhysicsObject<DynObj, PhysObj>> parentCanvas;

	public DynamicPhysicsObject(DynObj dynamicObject, PhysObj physicsObject)
	{
		this.dynamicObject = dynamicObject;
		this.physicsObject = physicsObject;
	}

	public DynObj getDynamicObject()
	{
		return dynamicObject;
	}

	public PhysObj getPhysicsObject()
	{
		return physicsObject;
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isVisible()
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setVisible(boolean visible)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void draw(Graphics2D g2d)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void invalidate()
	{
		// TODO Auto-generated method stub

	}
}
