package de.grotefober.nbodysim.ui.graphics.dynObjects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

import de.vexplained.libraries.cvs_graphics_library.stdGraphics.DynamicShape;
import de.vexplained.libraries.cvs_graphics_library.stdGraphics.dynObjects.DynEllipse;

/**
 * Ellipse with x, y being at the center instead of the top left corner, like in {@link DynEllipse}.
 *
 */
public class DynCenteredEllipse extends DynamicShape
{
	private double w, h;

	/**
	 * @param color
	 * @param x
	 * @param y
	 */
	public DynCenteredEllipse(Color color, double x, double y, double w, double h)
	{
		super(color, x, y);
		this.w = w;
		this.h = h;
	}

	public void setSize(double w, double h)
	{
		this.w = w;
		this.h = h;
		invalidate();
	}

	public double getWidth()
	{
		return w;
	}

	public double getHeight()
	{
		return h;
	}

	@Override
	public boolean isInShape(double x, double y)
	{
		// if > 1: outside
		// if == 1: on outline
		// if < 1: inside
		// @see
		// https://www.geeksforgeeks.org/check-if-a-point-is-inside-outside-or-on-the-ellipse/
		return (Math.pow((x - this.x), 2) / Math.pow(w / 2, 2))
				+ (Math.pow((y - this.y), 2) / Math.pow(h / 2, 2)) <= 1;
	}

	@Override
	protected void _draw(Graphics2D g2d)
	{
		g2d.draw(new Ellipse2D.Double(x - w / 2d, y - h / 2d, w, h));
	}

	@Override
	protected void _drawFill(Graphics2D g2d)
	{
		g2d.fill(new Ellipse2D.Double(x - w / 2d, y - h / 2d, w, h));
	}

}
