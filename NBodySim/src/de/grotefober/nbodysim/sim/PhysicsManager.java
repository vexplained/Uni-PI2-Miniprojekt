package de.grotefober.nbodysim.sim;

import java.util.Collections;
import java.util.List;

import de.grotefober.nbodysim.ui.graphics.PhysicsUniverse2D;
import de.grotefober.nbodysim.ui.graphics.dynObjects.DynamicPhysicsObject;
import de.vexplained.libraries.cvs_graphics_library.stdGraphics.ITickable;
import de.vexplained.libraries.cvs_graphics_library.stdGraphics.ObjectManager;

public class PhysicsManager extends ObjectManager
{

	/**
	 * Contains all {@link PhysicsObject}s of this' object's parent universe.
	 * <br>
	 * FIXME: Perhaps refactor to contain only reference to parent container / PhysicsController -> reduce duplicate
	 * memory (same List for every object)
	 */
	protected List<DynamicPhysicsObject> universe;

	public PhysicsManager(PhysicsUniverse2D physUniverse)
	{
		super(physUniverse);
		this.universe = Collections.synchronizedSet(physUniverse.getDynPhysicsObjects());
		this.universe = physUniverse.getDynPhysicsObjects();
	}

	@Override
	public void addToTickScheduler(ITickable toAdd)
	{
		if (toAdd instanceof DynamicPhysicsObject)
		{
			synchronized (universe)
			{
				universe.add((DynamicPhysicsObject) toAdd);
			}
		} else
		{
			super.addToTickScheduler(toAdd);
		}
	}

	// public void addToTickScheduler(DynamicPhysicsObject dynPhysObject)
	// {
	// objectManager.addToTickScheduler(dynPhysObject);
	// }
}
