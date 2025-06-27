package de.vexplained.libraries.cvs_graphics_library.stdGraphics;

import java.awt.Graphics2D;

public interface IDynamicComponent
{
	/**
	 * Sets the canvas this object is painted on. This is necessary to enable automatic repainting of the canvas upon
	 * altering the object's properties.
	 * 
	 * @param parent
	 *            the canvas this {@link IDynamicComponent} belongs to.
	 */
	public void setParentCanvas(IDynamicContainer parent);

	/**
	 * Returns the parent canvas this {@link IDynamicComponent} belongs to.
	 * 
	 * @return the canvas this {@link IDynamicComponent} is drawn onto.
	 */
	public IDynamicContainer getParentCanvas();

	/**
	 * Returns whether this {@link IDynamicComponent} is being rendered.
	 * 
	 * @return the visibility of this {@link IDynamicComponent}.
	 */
	public boolean isVisible();

	/**
	 * Sets the visibilty of this {@link IDynamicComponent}.
	 */
	public void setVisible(boolean visible);

	/**
	 * Draws this {@link IDynamicComponent} onto the given {@link Graphics2D} <code>g2d</code> instance, if this
	 * component is set to be visible.
	 * <br>
	 * <i>Note that the drawing is <b>not</b> being executed at a clone of the {@link Graphics2D} instance</i>.
	 */
	public void draw(Graphics2D g2d);

	/**
	 * Passes the invalidate call to the <code>parentCanvas</code> of this {@link IDynamicComponent}, if one is
	 * defined.
	 */
	public void invalidate();
}
