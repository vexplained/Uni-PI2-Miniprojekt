package de.vexplained.libraries.cvs_graphics_library.stdGraphics.dynObjects;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;

import de.vexplained.libraries.cvs_graphics_library.stdGraphics.DynamicObject;

/**
 * @author vExplained
 *
 */
public class DynArrow extends DynamicObject
{
	private double length, angle;

	/**
	 * @param color
	 * @param x
	 * @param y
	 */
	public DynArrow(Color color, double x, double y, double length, double angle)
	{
		super(color, x, y);
		this.length = length;
		this.angle = angle;
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

		double arrowRatio = 0.5f;
		double arrowLength = length;

		BasicStroke stroke = (BasicStroke) g2d.getStroke();

		double endX = 350.0f;

		double veeX = endX - stroke.getLineWidth() * 0.5f / arrowRatio;

		// vee
		Path2D.Float path = new Path2D.Float();

		double waisting = 0.7f;

		double waistX = endX - arrowLength * 0.5f;
		double waistY = arrowRatio * arrowLength * 0.5f * waisting;
		double arrowWidth = arrowRatio * arrowLength;

		path.moveTo(veeX - arrowLength, -arrowWidth);
		path.quadTo(waistX, -waistY, endX, 0.0f);
		path.quadTo(waistX, waistY, veeX - arrowLength, arrowWidth);

		// end of arrow is pinched in
		path.lineTo(veeX - arrowLength * 0.75f, 0.0f);
		path.lineTo(veeX - arrowLength, -arrowWidth);

		g2d.rotate(Math.toRadians(angle));
		g2d.fill(path);

		g2d.draw(new Line2D.Double(x, y, veeX - arrowLength * 0.5f, y));

		g2d.rotate(Math.toRadians(-angle));
	}

}
