package de.vexplained.libraries.cvs_graphics_library.stdGraphics;

import java.awt.Color;
import java.awt.Stroke;

/**
 * @author vExplained
 *
 */
public abstract class DynamicTickableObject extends DynamicObject
{
	public DynamicTickableObject(Color color, double x, double y)
	{
		super(color, x, y);
	}

	public DynamicTickableObject(Color color, double x, double y, Stroke stroke)
	{
		super(color, x, y, stroke);
	}

	public abstract void tick();
}
