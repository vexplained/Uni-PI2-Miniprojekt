package de.vexplained.libraries.cvs_graphics_library.stdGraphics.dynObjects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

import de.vexplained.libraries.cvs_graphics_library.stdGraphics.DynamicShape;

/**
 * @author vExplained
 *
 */
public class DynEllipse extends DynamicShape
{
	private double w, h;

	/**
	 * @param color
	 * @param x
	 * @param y
	 */
	public DynEllipse(Color color, double x, double y, double w, double h)
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
		// equation is for ellipse centered at (this.x, this.y)
		x -= w / 2;
		y -= h / 2;

		// if > 1: outside
		// if == 1: on outline
		// if < 1: inside
		// @see
		// https://www.geeksforgeeks.org/check-if-a-point-is-inside-outside-or-on-the-ellipse/
		return (((int) Math.pow((x - this.x), 2) / (int) Math.pow(w / 2, 2))
				+ ((int) Math.pow((y - this.y), 2) / (int) Math.pow(h / 2, 2))) < 1;
	}

	@Override
	protected void _draw(Graphics2D g2d)
	{
		g2d.draw(new Ellipse2D.Double(x, y, w, h));
	}

	@Override
	protected void _drawFill(Graphics2D g2d)
	{
		g2d.fill(new Ellipse2D.Double(x, y, w, h));
	}

}
