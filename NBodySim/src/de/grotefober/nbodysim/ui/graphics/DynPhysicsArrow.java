package de.grotefober.nbodysim.ui.graphics;

import java.awt.Color;
import java.awt.Graphics2D;

import de.grotefober.nbodysim.sim.PhysicsManager;
import de.grotefober.nbodysim.sim.PhysicsObject;
import de.grotefober.nbodysim.sim.Vector2D;
import de.vexplained.libraries.cvs_graphics_library.stdGraphics.dynObjects.DynArrow;

/**
 * {@link DynArrow} which automatically reflects changes in its linked physics object's acceleration or speed, depending
 * on which property is selected.
 * <br>
 * TODO: maybe refactor to make cleaner; generalize
 */
public class DynPhysicsArrow extends DynArrow
{

	public enum ELinkedProperty
	{
		ACCELERATION, VELOCITY
	}

	private PhysicsObject linkedObject;
	private ELinkedProperty linkedProperty;
	private PhysicsManager physMan;
	private double scaleFactor;

	public DynPhysicsArrow(Color color, double x, double y, double x2, double y2, PhysicsObject linkedObj,
			ELinkedProperty linkedProperty, PhysicsManager physMan)
	{
		this(color, x, y, x2, y2, linkedObj, linkedProperty, physMan, 3000D);
	}

	public DynPhysicsArrow(Color color, double x, double y, double x2, double y2, PhysicsObject linkedObj,
			ELinkedProperty linkedProperty, PhysicsManager physMan, double scaleFactor)
	{
		super(color, x, y, x2, y2);
		this.linkedObject = linkedObj;
		this.linkedProperty = linkedProperty;
		this.physMan = physMan;
		this.scaleFactor = scaleFactor;
	}

	@Override
	protected void _draw(Graphics2D g2d)
	{
		// Update arrow position & direction to reflect physics object
		// Position should be auto-updated by DynCompoundObject
		// TODO: add check whether parent is actually of type DynCompoundObject?
		Vector2D vec = switch (linkedProperty)
		{
		// TODO: this is untidy; ideally, this should not depend on physMan
		case ACCELERATION -> linkedObject.getAcceleration();
		case VELOCITY -> linkedObject.getVelocity();
		};
		vec = vec.scale(scaleFactor / physMan.DISTANCE_FACTOR);

		System.out.println(vec);
		vec = vec.add(linkedObject.getPosition().scale(1 / physMan.DISTANCE_FACTOR));

		this.setTip(vec.getX(), vec.getY());
		super._draw(g2d);
	}
}
