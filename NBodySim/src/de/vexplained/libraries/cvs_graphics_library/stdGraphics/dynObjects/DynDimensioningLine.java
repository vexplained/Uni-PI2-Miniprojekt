package de.vexplained.libraries.cvs_graphics_library.stdGraphics.dynObjects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;

/**
 * @author vExplained
 * @deprecated currently not working
 */
@Deprecated
public class DynDimensioningLine extends DynLine
{
	private double dimensioningDelimiterLengthHalfs;

	public DynDimensioningLine(Color color, double x1, double y1, double x2, double y2,
			double dimensioningDelimiterLength)
	{
		super(color, x1, y1, x2, y2);
		this.dimensioningDelimiterLengthHalfs = dimensioningDelimiterLength / 2D;
	}

	public DynDimensioningLine(Color color, Point2D tail, Point2D tip, double dimensioningDelimiterLength)
	{
		super(color, tail, tip);
		this.dimensioningDelimiterLengthHalfs = dimensioningDelimiterLength / 2D;
	}

	@Override
	protected void _draw(Graphics2D g2d)
	{
		super._draw(g2d);

		// FIXME not working

		// double angle = Math.atan2(y2 - y, x2 - x);
		//
		// Graphics2D g2Rotated = (Graphics2D) g2d.create();
		// g2Rotated.translate(x, y);
		// g2Rotated.rotate(-angle + Math.PI / 2D);
		// g2Rotated.draw(new Line2D.Double(-dimensioningDelimiterLengthHalfs, 0, dimensioningDelimiterLengthHalfs, 0));
		// g2Rotated.translate(x - x2, y - y2);
		// g2Rotated.draw(new Line2D.Double(-dimensioningDelimiterLengthHalfs, 0, dimensioningDelimiterLengthHalfs, 0));
	}

}
