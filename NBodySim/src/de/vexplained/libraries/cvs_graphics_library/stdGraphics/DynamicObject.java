package de.vexplained.libraries.cvs_graphics_library.stdGraphics;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Point2D;

/**
 * @author vExplained
 *
 */
public abstract class DynamicObject
{
	protected DynamicCanvas parentCanvas;

	protected double x, y;
	protected Color color;
	protected Stroke stroke;
	protected boolean visible = true;

	/**
	 * Creates a new Dynamic Object instance. When altering the properties of a Dynamic Object, its parent canvas is
	 * automatically being updated to show the altered version.
	 * 
	 * @param color
	 *            the color of the object.
	 * @param x
	 *            the x coordinate of the object.
	 * @param y
	 *            the y coordinate of the object.
	 */
	public DynamicObject(Color color, double x, double y)
	{
		this(color, x, y, new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
	}

	/**
	 * Creates a new Dynamic Object instance. When altering the properties of a Dynamic Object, its parent canvas is
	 * automatically being updated to show the altered version.
	 * 
	 * @param color
	 *            the color of the object.
	 * @param origin
	 *            the origin of the object.
	 * @param stroke
	 *            the stroke of the object, if the object is drawn as outline.
	 */
	public DynamicObject(Color color, Point2D origin, Stroke stroke)
	{
		this(color, origin.getX(), origin.getY(), stroke);
	}

	/**
	 * Creates a new Dynamic Object instance. When altering the properties of a Dynamic Object, its parent canvas is
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
	public DynamicObject(Color color, double x, double y, Stroke stroke)
	{
		this.x = x;
		this.y = y;
		this.color = color;
		this.stroke = stroke;
	}

	/**
	 * Sets the canvas this object is painted on. This is necessary to enable automatic repainting of the canvas upon
	 * altering the object's properties.
	 */
	public void setParentCanvas(DynamicCanvas parent)
	{
		this.parentCanvas = parent;
	}

	/**
	 * Returns the {@link Color} of this {@link DynamicObject} instance.
	 * 
	 * @return the {@link Color} of this {@link DynamicObject} instance.
	 */
	public Color getColor()
	{
		return color;
	}

	/**
	 * Sets the {@link Color} of this {@link DynamicObject} instance.
	 */
	public void setColor(Color color)
	{
		this.color = color;
		invalidate();
	}

	/**
	 * Returns the {@link Stroke} of this {@link DynamicObject} instance.
	 * 
	 * @return the {@link Stroke} of this {@link DynamicObject} instance.
	 */
	public Stroke getStroke()
	{
		return stroke;
	}

	/**
	 * Sets the {@link Stroke} of this {@link DynamicObject} instance.
	 */
	public void setStroke(Stroke stroke)
	{
		this.stroke = stroke;
		invalidate();
	}

	/**
	 * Returns the x-coordinate of this {@link DynamicObject} instance on the canvas.
	 * 
	 * @return the x-coordinate of this {@link DynamicObject} instance.
	 */
	public double getX()
	{
		return x;
	}

	/**
	 * Sets the x-coordinate of this {@link DynamicObject} instance on the canvas.
	 */
	public void setX(double x)
	{
		this.x = x;
		invalidate();
	}

	/**
	 * Returns the y-coordinate of this {@link DynamicObject} instance on the canvas.
	 * 
	 * @return the y-coordinate of this {@link DynamicObject} instance.
	 */
	public double getY()
	{
		return y;
	}

	/**
	 * Sets the y-coordinate of this {@link DynamicObject} instance on the canvas.
	 */
	public void setY(double y)
	{
		this.y = y;
		invalidate();
	}

	/**
	 * Returns whether this {@link DynamicObject} instance is being rendered.
	 * 
	 * @return the visibility of this {@link DynamicObject} instance.
	 */
	public boolean isVisible()
	{
		return visible;
	}

	/**
	 * Sets the visibilty of this {@link DynamicObject} instance.
	 */
	public void setVisible(boolean visible)
	{
		this.visible = visible;
		invalidate();
	}

	/**
	 * Changes the position of this {@link DynamicObject} instance to new absolute values.
	 * <br>
	 * The result of this method is equivalent to calling {@link #setX(double)} and {@link #setY(double)} successively.
	 * 
	 * @param x
	 *            the new x-coordinate
	 * @param y
	 *            the new y-coordinate
	 */
	public void moveTo(double x, double y)
	{
		setX(x);
		setY(y);
		invalidate();
	}

	/**
	 * Changes the position of this {@link DynamicObject} instance using x and y offsets.
	 *
	 * @param deltaX
	 *            the amount to move this object in x-direction.
	 * @param deltaY
	 *            the amount to move this object in y-direction.
	 */
	public void moveBy(double deltaX, double deltaY)
	{
		setX(getX() + deltaX);
		setY(getY() + deltaY);
		invalidate();
	}

	/**
	 * Checks whether the given point is part of this {@link DynamicObject} instance's bounding shape.
	 *
	 * @param x
	 *            the x-coordinate of the point in question.
	 * @param y
	 *            the y-coordinate of the point in question.
	 * 
	 * @return true if the given point is part of this {@link DynamicObject} instance's bounding shape, false otherwise.
	 */
	public abstract boolean isInShape(double x, double y);

	/**
	 * Draws this {@link DynamicObject} instance onto the given {@link Graphics2D} <code>g2d</code> instance, if this
	 * component is set to be visible.
	 * <br>
	 * <i>Note that the drawing is <b>not</b> being executed at a clone of the {@link Graphics2D} instance</i>.
	 */
	public void draw(Graphics2D g2d)
	{
		if (visible)
		{
			g2d = (Graphics2D) g2d.create();
			g2d.setStroke(getStroke());
			g2d.setColor(getColor());
			_draw(g2d);
		}
	}

	/**
	 * Defines the implementation-dependent drawing routine.
	 * <br>
	 * <i>The implementation need not set the stroke or the color of this object as this has already been done when
	 * calling this method.</i>
	 */
	protected abstract void _draw(Graphics2D g2d);

	/**
	 * Passes the invalidate call to the <code>parentCanvas</code> of this {@link DynamicObject} instance, if one is
	 * defined. In this case, {@link DynamicCanvas#invalidate()} is called.
	 */
	public void invalidate()
	{
		if (parentCanvas != null)
		{
			parentCanvas.invalidate();
		}
	}
}
