package de.vexplained.libraries.cvs_graphics_library.stdGraphics.dynObjects;

import java.awt.Color;
import java.awt.geom.Point2D;

import de.vexplained.libraries.cvs_graphics_library.util.GraphicsCalcUtils;

/**
 * @author vExplained
 *
 */
public class DynIntersectingLine extends DynLine
{
	public DynIntersectingLine(Color color, double x1, double y1, double x2, double y2)
	{
		super(color, x1, y1, x2, y2);
	}

	public DynIntersectingLine(Color color, Point2D tail, Point2D tip)
	{
		super(color, tail, tip);
	}

	/**
	 * Returns the angle of the line in <code>deg</code>.<br>
	 * Uses the fact that <code>tan(alpha)=m</code>
	 */
	public double getOrientation()
	{
		double offX = getTip().getX() - getTail().getX();
		double offY = getTip().getY() - getTail().getY();
		if (offX != 0)
		{
			return Math.atan(offY / offX);
		} else
		{
			return 90;
		}
	}

	public boolean doesIntersectWith(DynLine otherLine)
	{
		return GraphicsCalcUtils.doLinesIntersect(this, otherLine);
	}
}
