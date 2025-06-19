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
public class DynLineConnector extends DynamicObject
{
	private double length, angle;
	private double x2, y2;
	private DynLineConnector parent;

	/**
	 * @param color
	 * @param x
	 * @param y
	 */
	public DynLineConnector(Color color, double x1, double y1, double length, double angle, DynLineConnector parent)
	{
		super(color, x1, y1);
		this.length = length;
		this.angle = angle;
		this.parent = parent;
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

	public void setTail(double xTail, double yTail)
	{
		moveTo(xTail, yTail);
		invalidate();
	}

	/**
	 * Calls {@link #update(boolean)} with {@code true} passed as
	 * {@code updateParent} argument.
	 */
	public void update()
	{
		update(true);
	}

	/**
	 * Updates the line object, to be exact start and end Point. To do so, if
	 * specified, the parent line will be updated to reassign the tail position.
	 * 
	 * @param updateParent
	 *            If true, the parent line will also be updated
	 */
	public void update(boolean updateParent)
	{
		if (parent != null && updateParent)
		{
			// no extra update needed; #getTip() calls #update()
			Point2D origin = parent.getTip();
			x = origin.getX();
			y = origin.getY();
		}
		x2 = (Math.cos(Math.toRadians(-angle)) * length) + x;
		y2 = (Math.sin(Math.toRadians(-angle)) * length) + y;
	}

	/**
	 * Combines the update and the draw routine into one method.
	 */
	public void updateAndRedraw(Graphics2D g2d)
	{
		update();
		_draw(g2d);
	}

	public Point2D getTail()
	{
		if (parent == null)
		{
			update();
			return new Point2D.Double(x, y);
		} else
		{
			return parent.getTip();
		}
	}

	public Point2D getTip()
	{
		update();
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

	/**
	 * <h1>Caution!</h1> Does NOT update tail or tip of the line. Only draws the
	 * last calculated state. To update and draw, use instead
	 * {@link #updateAndRedraw(Graphics2D)}.<br>
	 * <br>
	 */
	@Override
	protected void _draw(Graphics2D g2d)
	{
		g2d.setColor(color);
		g2d.draw(new Line2D.Double(x, y, x2, y2));
	}

}
