package de.vexplained.libraries.cvs_graphics_library.stdGraphics;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Point2D;

/**
 * {@link DynamicObject} which covers an area, like a rectangle or an ellipse, are {@link DynamicShape}s. They may have
 * a fill color.
 * 
 * @author vExplained
 *
 */
public abstract class DynamicShape extends DynamicObject
{

	/**
	 * The fill color of this Dynamic Shape. By default, the fill is set to transparent.
	 */
	protected Color fill;

	/**
	 * Creates a new Dynamic Shape instance. When altering the properties of a Dynamic Shape, its parent canvas is
	 * automatically being updated to show the altered version.
	 * 
	 * @param color
	 *            the color of the object.
	 * @param x
	 *            the x coordinate of the object.
	 * @param y
	 *            the y coordinate of the object.
	 */
	public DynamicShape(Color color, double x, double y)
	{
		this(color, x, y, new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
	}

	/**
	 * Creates a new Dynamic Shape instance. When altering the properties of a Dynamic Shape, its parent canvas is
	 * automatically being updated to show the altered version.
	 * 
	 * @param color
	 *            the color of the object.
	 * @param origin
	 *            the origin of the object.
	 * @param stroke
	 *            the stroke of the object, if the object is drawn as outline.
	 */
	public DynamicShape(Color color, Point2D origin, Stroke stroke)
	{
		this(color, origin.getX(), origin.getY(), stroke);
	}

	/**
	 * Creates a new Dynamic Shape instance. When altering the properties of a Dynamic Shape, its parent canvas is
	 * automatically being updated to show the altered version.
	 * 
	 * @param color
	 *            the color of the object.
	 * @param x
	 *            the x coordinate of the object.
	 * @param y
	 *            the y coordinate of the object.
	 * @param stroke
	 *            the stroke of the object, if the object is drawn as outline.
	 */
	public DynamicShape(Color color, double x, double y, Stroke stroke)
	{
		super(color, x, y, stroke);
		this.fill = new Color(0x00000000, true);
	}

	/**
	 * Returns the fill {@link Color} of this {@link DynamicShape} instance.
	 * 
	 * @return the fill {@link Color} of this {@link DynamicShape} instance.
	 */
	public Color getFill()
	{
		return fill;
	}

	/**
	 * Sets the fill {@link Color} of this {@link DynamicShape} instance.
	 */
	public void setFill(Color fill)
	{
		this.fill = fill;
		invalidate();
	}

	/**
	 * Draws this {@link DynamicShape} instance onto the given {@link Graphics2D} <code>g2d</code> instance, if this
	 * component is set to be visible.
	 * <br>
	 * <i>Note that the drawing is <b>not</b> being executed at a clone of the {@link Graphics2D} instance</i>.
	 */
	@Override
	public void draw(Graphics2D g2d)
	{
		if (visible)
		{
			g2d = (Graphics2D) g2d.create();
			g2d.setStroke(getStroke());
			g2d.setColor(getFill());
			_drawFill(g2d);
			g2d.setColor(getColor());
			_draw(g2d);
		}
	}

	/**
	 * Defines the implementation-dependent drawing routine to fill the shape.
	 * <br>
	 * <i>The implementation need not set the fill color of this object as this has already been done when
	 * calling this method.</i>
	 */
	protected abstract void _drawFill(Graphics2D g2d);
}
