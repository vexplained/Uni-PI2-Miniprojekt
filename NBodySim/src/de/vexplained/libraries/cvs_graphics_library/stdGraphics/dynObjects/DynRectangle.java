package de.vexplained.libraries.cvs_graphics_library.stdGraphics.dynObjects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D;

import de.vexplained.libraries.cvs_graphics_library.stdGraphics.DynamicShape;

/**
 * @author vExplained
 *
 */
public class DynRectangle extends DynamicShape
{
	private double w, h;
	private Stroke stroke;

	/**
	 * @param color
	 * @param x
	 * @param y
	 */
	// public DynRectangle(Color color, int x, int y, int w, int h)
	// {
	// this(color, x, y, w, h, new BasicStroke(5, BasicStroke.CAP_ROUND,
	// BasicStroke.JOIN_ROUND));
	// }

	public DynRectangle(Color color, double x, double y, double w, double h)
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
	public boolean isInShape(double mx, double my)
	{
		return (mx >= x && mx <= x + w && my >= y && my <= y + h);
	}

	@Override
	protected void _draw(Graphics2D g2d)
	{
		g2d.draw(new Rectangle2D.Double(x, y, w, h));
	}

	@Override
	protected void _drawFill(Graphics2D g2d)
	{
		g2d.fill(new Rectangle2D.Double(x, y, w, h));
	}
}
