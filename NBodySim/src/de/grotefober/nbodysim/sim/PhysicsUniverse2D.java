package de.grotefober.nbodysim.sim;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.vexplained.libraries.cvs_graphics_library.stdGraphics.DynamicCanvas;

public class PhysicsUniverse2D extends DynamicCanvas
{
	private static final long serialVersionUID = 7569088216700449776L;

	private List<PhysicsObject> physicsObjects;

	public PhysicsUniverse2D()
	{
		this(Color.BLACK);
	}

	public PhysicsUniverse2D(Color bgColor)
	{
		super();

		this.physicsObjects = Collections.synchronizedList(new ArrayList<PhysicsObject>());
	}

	public List<PhysicsObject> getPhysicsObjects()
	{
		return physicsObjects;
	}

	public void removeAllPhysicsObjects()
	{
		// FIXME Problem: PhysicsObject sollte nicht von DynObj erben müssen
		// Irgendwie mit DynObj und "PhysicsShadow" machen?
		super.removeAllObjects(physicsObjects);
		synchronized (physicsObjects)
		{
			physicsObjects.clear();
		}
	}

	public void removeAllPhysicsObjects(List<PhysicsObject> listToRemove)
	{
		synchronized (physicsObjects)
		{
			for(PhysicsObject obj : listToRemove)
			{
				if(!)
			}
		}
	}
}
