package de.vexplained.libraries.cvs_graphics_library.stdGraphics.dynObjects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.RoundRectangle2D;

import de.vexplained.libraries.cvs_graphics_library.stdGraphics.DynamicShape;

/**
 * @author vExplained
 *
 */
public class DynRoundRectangle extends DynamicShape
{
	private double w, h;
	private Stroke stroke;
	private double arcw, arch;

	/**
	 * Constructs and initializes a <code>DynRoundRectangle</code> from the specified double coordinates.
	 *
	 * @param x
	 *            the X coordinate of the newly constructed <code>DynRoundRectangle</code>
	 * @param y
	 *            the Y coordinate of the newly constructed <code>DynRoundRectangle</code>
	 * @param w
	 *            the width to which to set the newly constructed <code>DynRoundRectangle</code>
	 * @param h
	 *            the height to which to set the newly constructed <code>DynRoundRectangle</code>
	 * @param arcw
	 *            the width of the arc to use to round off the corners of the newly constructed
	 *            <code>DynRoundRectangle</code>
	 * @param arch
	 *            the height of the arc to use to round off the corners of the newly constructed
	 *            <code>DynRoundRectangle</code>
	 */
	public DynRoundRectangle(Color color, double x, double y, double w, double h, double arcw, double arch)
	{
		super(color, x, y);
		this.w = w;
		this.h = h;
		this.arcw = arcw;
		this.arch = arch;
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

	/**
	 * Sets the <code>arcw</code> and <code>arch</code> values used for the arcs rounding off the corners of this
	 * <code>DynRoundRectangle</code>.
	 * 
	 * @param arcw
	 *            the width of the arc used to round off the corners of this <code>DynRoundRectangle</code>
	 * @param arch
	 *            the height of the arc used to round off the corners of this <code>DynRoundRectangle</code>
	 */
	public void setRadius(double arcw, double arch)
	{
		this.arcw = arcw;
		this.arch = arch;
	}

	/**
	 * Returns the <code>arcw</code> value used for the arcs rounding off the corners of this
	 * <code>DynRoundRectangle</code>.
	 * 
	 * @return the width of the arc used to round off the corners of this <code>DynRoundRectangle</code>
	 */
	public double getRadiusW()
	{
		return this.arcw;
	}

	/**
	 * Returns the <code>arch</code> value used for the arcs rounding off the corners of this
	 * <code>DynRoundRectangle</code>.
	 * 
	 * @return the height of the arc used to round off the corners of this <code>DynRoundRectangle</code>
	 */
	public double getRadiusH()
	{
		return this.arch;
	}

	@Override
	public boolean isInShape(double mx, double my)
	{
		return (mx >= x && mx <= x + w && my >= y && my <= y + h);
	}

	@Override
	protected void _draw(Graphics2D g2d)
	{
		g2d.draw(new RoundRectangle2D.Double(x, y, w, h, arcw, arch));
	}

	@Override
	protected void _drawFill(Graphics2D g2d)
	{
		g2d.fill(new RoundRectangle2D.Double(x, y, w, h, arcw, arch));
	}
}
