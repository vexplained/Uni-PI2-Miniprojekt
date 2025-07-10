package de.vexplained.libraries.cvs_graphics_library.stdGraphics.dynObjects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.GeneralPath;

import de.vexplained.libraries.cvs_graphics_library.stdGraphics.DynamicObject;

/**
 * @author vExplained
 *
 */
public class DynArrow extends DynamicObject
{
	protected double x2, y2;

	/**
	 * @param color
	 * @param x
	 * @param y
	 */
	public DynArrow(Color color, double x, double y, double x2, double y2)
	{
		super(color, x, y);
		this.x2 = x2;
		this.y2 = y2;
	}

	public void setTip(double x2, double y2)
	{
		this.x2 = x2;
		this.y2 = y2;
		invalidate();
	}

	@Override
	public boolean isInShape(double x, double y)
	{
		return false;
	}

	@Override
	protected void _draw(Graphics2D g2d)
	{
		// TODO: Refactor to not use hardcoded tip size
		drawArrow(g2d, x, y, x2, y2, getStroke(), getStroke(), 15);
	}

	/**
	 * Source: https://gist.github.com/raydac/df97493f58b0521fb20a
	 * <br>
	 * Refactored to use double instead of Point2D instances
	 */
	public static void drawArrow(final Graphics2D gfx, double startX, double startY, double endX, double endY,
			final Stroke lineStroke,
			final Stroke arrowStroke, final float arrowSize)
	{
		gfx.setStroke(arrowStroke);
		final double deltax = startX - endX;
		final double result;
		if (deltax == 0.0d)
		{
			result = Math.PI / 2;
		} else
		{
			result = Math.atan((startY - endY) / deltax) + (startX < endX ? Math.PI : 0);
		}

		final double angle = result;

		final double arrowAngle = Math.PI / 12.0d;

		final double x1 = arrowSize * Math.cos(angle - arrowAngle);
		final double y1 = arrowSize * Math.sin(angle - arrowAngle);
		final double x2 = arrowSize * Math.cos(angle + arrowAngle);
		final double y2 = arrowSize * Math.sin(angle + arrowAngle);

		final double cx = (arrowSize / 2.0f) * Math.cos(angle);
		final double cy = (arrowSize / 2.0f) * Math.sin(angle);

		final GeneralPath polygon = new GeneralPath();
		polygon.moveTo(endX, endY);
		polygon.lineTo(endX + x1, endY + y1);
		polygon.lineTo(endX + x2, endY + y2);
		polygon.closePath();
		gfx.fill(polygon);

		gfx.setStroke(lineStroke);
		gfx.drawLine((int) startX, (int) startY, (int) (endX + cx), (int) (endY + cy));
	}

}
