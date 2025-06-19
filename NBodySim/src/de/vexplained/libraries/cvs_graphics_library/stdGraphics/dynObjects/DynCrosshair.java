package de.vexplained.libraries.cvs_graphics_library.stdGraphics.dynObjects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;

import de.vexplained.libraries.cvs_graphics_library.stdGraphics.DynamicObject;

/**
 * @author vExplained
 *
 */
public class DynCrosshair extends DynamicObject
{
	private double cutoutRadius;
	private double crosshairLength;

	/**
	 * @param cutoutRadius
	 *            radius of an imaginary circle cutout centered on the crosshair where no crosshair is drawn.
	 * @param crosshairLength
	 *            length of each crosshair line, beginning at the end of the center cutout.
	 */
	public DynCrosshair(Color color, double xCenter, double yCenter, double cutoutRadius, double crosshairLength,
			Stroke stroke)
	{
		super(color, xCenter, yCenter, stroke);
		this.cutoutRadius = cutoutRadius;
		this.crosshairLength = crosshairLength;
	}

	/**
	 * @return the cutoutRadius
	 */
	public double getCenterCircleRadius()
	{
		return cutoutRadius;
	}

	/**
	 * @param cutoutRadius
	 *            the cutoutRadius to set
	 */
	public void setCenterCircleRadius(double centerCircleRadius)
	{
		this.cutoutRadius = centerCircleRadius;
		this.invalidate();
	}

	/**
	 * @return the crosshairLength
	 */
	public double getCrosshairLength()
	{
		return crosshairLength;
	}

	/**
	 * @param crosshairLength
	 *            the crosshairLength to set
	 */
	public void setCrosshairLength(double crosshairLength)
	{
		this.crosshairLength = crosshairLength;
		this.invalidate();
	}

	@Override
	public boolean isInShape(double x, double y)
	{
		return false;
	}

	@Override
	protected void _draw(Graphics2D g2d)
	{
		double outerRadius = cutoutRadius + crosshairLength;
		// left horizontal line
		g2d.draw(new Line2D.Double(x - outerRadius, y, x - cutoutRadius, y));
		// right horizontal line
		g2d.draw(new Line2D.Double(x + cutoutRadius, y, x + outerRadius, y));
		// top vertical line
		g2d.draw(new Line2D.Double(x, y - outerRadius, x, y - cutoutRadius));
		// bottom vertical line
		g2d.draw(new Line2D.Double(x, y + cutoutRadius, x, y + outerRadius));

		// drawn circle
		double circleRadius = cutoutRadius + crosshairLength / 2D;
		g2d.draw(new Ellipse2D.Double(x - circleRadius, y - circleRadius, circleRadius * 2, circleRadius * 2));
	}

}
