package de.vexplained.libraries.cvs_graphics_library.util;

import java.awt.Color;
import java.awt.geom.Point2D;

import de.vexplained.libraries.cvs_graphics_library.stdGraphics.dynObjects.DynLine;

/**
 * @author vExplained
 *
 */
public class GraphicsCalcUtils
{
	public static final double EPSILON = 0.000001;

	/**
	 * Calculate the cross product of two points.
	 * 
	 * @param a
	 *            first point
	 * @param b
	 *            second point
	 * @return the value of the cross product
	 */
	public static double crossProduct(Point2D a, Point2D b)
	{
		return a.getX() * b.getY() - b.getX() * a.getY();
	}

	/**
	 * Check if bounding boxes do intersect. If one bounding box touches the other,
	 * they do intersect.
	 * 
	 * @param a
	 *            first bounding box
	 * @param b
	 *            second bounding box
	 * @return <code>true</code> if they intersect, <code>false</code> otherwise.
	 */
	public static boolean doBoundingBoxesIntersect(Point2D[] a, Point2D[] b)
	{
		return a[0].getX() <= b[1].getX() && a[1].getX() >= b[0].getX() && a[0].getY() <= b[1].getY()
				&& a[1].getY() >= b[0].getY();
	}

	/**
	 * Checks if a Point is on a line
	 * 
	 * @param line
	 *            Line (interpreted as line, although given as line segment)
	 * @param point
	 *            Point
	 * @return <code>true</code> if point is on line, otherwise <code>false</code>
	 */
	public static boolean isPointOnLine(DynLine line, Point2D point)
	{
		// Move the image, so that a.first is on (0|0)
		DynLine lnTmp = new DynLine(Color.CYAN, 0, 0, line.getTip().getX() - line.getTail().getX(),
				line.getTip().getY() - line.getTail().getY());

		// LineSegment aTmp = new LineSegment(new Point(0, 0),
		// new Point(line.second.x - line.first.x, line.second.y - line.first.y));
		Point2D pTmp = new Point2D.Double(point.getX() - line.getTail().getX(), point.getY() - line.getTail().getY());
		// Point bTmp = new Point(point.x - line.first.x, point.y - line.first.y);
		double r = crossProduct(lnTmp.getTip(), pTmp);
		return Math.abs(r) < EPSILON;
	}

	/**
	 * Checks if a point is to the right of a line. If the point is on the line, it is not to the
	 * right of the line.
	 * 
	 * @param line
	 *            line segment interpreted as a line
	 * @param point
	 *            the point
	 * @return <code>true</code> if the point is right of the line,
	 *         <code>false</code> otherwise
	 */
	public static boolean isPointRightOfLine(DynLine line, Point2D point)
	{
		// Move the image, so that a.first is on (0|0)
		DynLine lnTmp = new DynLine(Color.CYAN, 0, 0, line.getTip().getX() - line.getTail().getX(),
				line.getTip().getY() - line.getTail().getY());

		Point2D pTmp = new Point2D.Double(point.getX() - line.getTail().getX(), point.getY() - line.getTail().getY());
		return crossProduct(lnTmp.getTip(), pTmp) < 0;
	}

	/**
	 * Check if line segment first touches or crosses the line that is defined by
	 * line segment second.
	 *
	 * @param first
	 *            line segment interpreted as line
	 * @param second
	 *            line segment
	 * @return <code>true</code> if line segment first touches or crosses line
	 *         second, <code>false</code> otherwise.
	 */
	public static boolean lineSegmentTouchesOrCrossesLine(DynLine a, DynLine b)
	{
		return isPointOnLine(a, b.getTail()) || isPointOnLine(a, b.getTip())
				|| (isPointRightOfLine(a, b.getTail()) ^ isPointRightOfLine(a, b.getTip()));
	}

	/**
	 * Check if line segments intersect
	 * 
	 * @param a
	 *            first line segment
	 * @param b
	 *            second line segment
	 * @return <code>true</code> if lines do intersect, <code>false</code> otherwise
	 */
	public static boolean doLinesIntersect(DynLine a, DynLine b)
	{
		Point2D[] box1 = a.getBoundingBox();
		Point2D[] box2 = b.getBoundingBox();
		return doBoundingBoxesIntersect(box1, box2) && lineSegmentTouchesOrCrossesLine(a, b)
				&& lineSegmentTouchesOrCrossesLine(b, a);
	}
}
