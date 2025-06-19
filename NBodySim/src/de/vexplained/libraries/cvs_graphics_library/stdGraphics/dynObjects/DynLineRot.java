package de.vexplained.libraries.cvs_graphics_library.stdGraphics.dynObjects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

import de.vexplained.libraries.cvs_graphics_library.stdGraphics.DynamicObject;

/**
 * @author vExplained
 *
 */
public class DynLineRot extends DynamicObject
{
	private double length, angle;
	private double x2, y2;

	/**
	 * @param color
	 * @param x
	 * @param y
	 */
	public DynLineRot(Color color, double x1, double y1, double length, double angle)
	{
		super(color, x1, y1);
		this.length = length;
		this.angle = angle;
	}

	public double getLength()
	{
		return length;
	}

	public void setLength(double length)
	{
		this.length = length;
		invalidate();
	}

	public double getAngle()
	{
		return angle;
	}

	public void setAngle(double angle)
	{
		this.angle = angle;
		invalidate();
	}

	private void update()
	{
		x2 = Math.cos(angle) * length;
		y2 = Math.sin(angle) * length;
	}

	public Point2D getTail()
	{
		return new Point2D.Double(x, y);
	}

	public Point2D getTip()
	{
		update();
		return new Point2D.Double(x2, y2);
	}

	@Override
	public boolean isInShape(double x, double y)
	{
		return false;
	}

	@Override
	protected void _draw(Graphics2D g2d)
	{
		update();
		g2d.setColor(color);
		g2d.draw(new Line2D.Double(x, y, x2, y2));
	}

}
