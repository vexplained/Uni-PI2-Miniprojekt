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
public class DynLine extends DynamicObject
{
	protected double x2, y2;

	/**
	 * @param color
	 * @param x
	 * @param y
	 */
	public DynLine(Color color, double x1, double y1, double x2, double y2)
	{
		super(color, x1, y1);
		this.x2 = x2;
		this.y2 = y2;
	}

	public DynLine(Color color, Point2D tail, Point2D tip)
	{
		this(color, tail.getX(), tail.getY(), tip.getX(), tip.getY());
	}

	/**
	 * @return the x2
	 */
	public double getX2()
	{
		return x2;
	}

	/**
	 * @param x2
	 *            the x2 to set
	 */
	public void setX2(double x2)
	{
		this.x2 = x2;
		invalidate();
	}

	/**
	 * @return the y2
	 */
	public double getY2()
	{
		return y2;
	}

	/**
	 * @param y2
	 *            the y2 to set
	 */
	public void setY2(double y2)
	{
		this.y2 = y2;
		invalidate();
	}

	public Point2D[] getBoundingBox()
	{
		return new Point2D[] { getTail(), getTip() };
	}

	public Point2D getTail()
	{
		return new Point2D.Double(x, y);
	}

	public Point2D getTip()
	{
		return new Point2D.Double(x2, y2);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void moveTo(double x, double y)
	{
		this.x2 += x - this.x;
		this.y2 += y - this.y;
		super.moveTo(x, y);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void moveBy(double deltaX, double deltaY)
	{
		this.x2 += deltaX;
		this.y2 += deltaY;
		super.moveBy(deltaX, deltaY);
	}

	@Override
	public boolean isInShape(double x, double y)
	{
		return false;
	}

	@Override
	protected void _draw(Graphics2D g2d)
	{
		g2d.setColor(color);
		g2d.draw(new Line2D.Double(x, y, x2, y2));
	}

}
