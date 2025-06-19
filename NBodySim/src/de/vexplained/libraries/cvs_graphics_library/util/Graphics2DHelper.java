package de.vexplained.libraries.cvs_graphics_library.util;

import java.awt.BasicStroke;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;

/**
 * @author vExplained
 *
 */
public class Graphics2DHelper
{
	/**
	 * Draw a String centered in the middle of a Rectangle.
	 *
	 * @param g
	 *            The {@link Graphics2D} instance.
	 * @param text
	 *            The {@link String} to draw.
	 * @param boundingRect
	 *            The {@link Rectangle} to center the text in.
	 * @param font
	 *            The {@link Font} to use for drawing. May be <code>null</code> if the set font of the
	 *            {@link Graphics2D} object should be used.
	 * 
	 * @see https://stackoverflow.com/a/27740330/19474335
	 */
	public static void drawCenteredString(Graphics2D g, String text, Rectangle boundingRect, Font font)
	{
		FontMetrics metrics = font != null ? g.getFontMetrics(font) : g.getFontMetrics();
		int x = boundingRect.x + (boundingRect.width - metrics.stringWidth(text)) / 2;
		int y = calcYCenterOfString(g, boundingRect, font);
		if (font != null)
		{
			g.setFont(font);
		}
		g.drawString(text, x, y);
	}

	public static int calcYCenterOfString(Graphics2D g, int yCenter, Font font)
	{
		return calcYCenterOfString(g, new Rectangle(0, yCenter, 0, 0), font);
	}

	public static int calcYCenterOfString(Graphics2D g, Rectangle boundingRect, Font font)
	{
		FontMetrics metrics = font != null ? g.getFontMetrics(font) : g.getFontMetrics();
		// ascent: Screen(0|0) is top left; FontChar(0|0) ist bottom left => add font
		// height
		int y = boundingRect.y + ((boundingRect.height - metrics.getHeight()) / 2) + metrics.getAscent();
		return y;
	}

	/**
	 * Draw a {@link Shape} centered in the middle of a Rectangle.
	 *
	 * @param g
	 *            The {@link Graphics2D} instance.
	 * @param text
	 *            The {@link String} to draw.
	 * @param boundingRect
	 *            The {@link Rectangle} to center the text in.
	 * @param font
	 *            The {@link Font} to use for drawing
	 */
	public static void fillCenteredShape(Graphics2D g, Shape shape, Rectangle boundingRect, Font font)
	{
		Rectangle shapeBounds = shape.getBounds();

		int x = (int) (boundingRect.x + (boundingRect.width - shapeBounds.getWidth()) / 2);
		int y = (int) (boundingRect.y + (boundingRect.height - shapeBounds.getHeight()) / 2);
		if (font != null)
		{
			g.setFont(font);
		}
		AffineTransform oldTransform = g.getTransform();
		g.translate(x, y);
		g.fill(shape);
		g.transform(oldTransform); // reset translation
	}

	/**
	 * Draw a String centered in the middle of a Rectangle.
	 *
	 * @param g
	 *            The {@link Graphics2D} instance.
	 * @param text
	 *            The {@link String} to draw.
	 *
	 * @param font
	 *            The {@link Font} to use for drawing
	 * 
	 * @return A {@link Shape} containing the string. Can be used to draw outlined
	 *         text.
	 * 
	 * @see https://stackoverflow.com/a/51329942
	 */
	public static Shape createFontShape(Graphics2D g, String text, Font font)
	{
		g = (Graphics2D) g.create();
		FontRenderContext frc = g.getFontRenderContext();
		if (text == null || text.length() == 0)
		{
			text = " "; // TextLayout does not support strings of length 0 //$NON-NLS-1$
		}
		TextLayout tl = new TextLayout(text, font, frc);
		return tl.getOutline(null);
	}

	/*
	 * scheme: mathematical - starting bottom left, continuing CCW
	 */
	public static final byte R_BOTTOM_LEFT = 0b0001;
	public static final byte R_BOTTOM_RIGHT = 0b0010;
	public static final byte R_TOP_RIGHT = 0b0100;
	public static final byte R_TOP_LEFT = 0b1000;

	/**
	 * Creates a rectangle with 0 to 4 corners rounded. This creates a real semicircular path at the corners, instead of
	 * the usual squircle approximation.
	 * 
	 * @param width
	 *            the width of the rectangle
	 * @param height
	 *            the height of the rectangle
	 * @param radius
	 *            the radius of all rounded corners
	 * @param roundCorners
	 *            all corners that should be rounded
	 * 
	 * @return a rectangle with all corners rounded as specified in
	 *         {@code roundedCorners}
	 */
	public static Path2D.Float createPartiallyRoundRect(float width, float height, float radius, byte roundCorners)
	{
		// bezier curve circular transition: https://stackoverflow.com/a/27863181
		double radiusControlPointModifier = (4 / 3D * Math.tan(Math.PI / (2 * 4))) * radius;
		// double radiusControlPointModifier = radius;

		Path2D.Float roundRect = new Path2D.Float();

		// top left corner
		if ((roundCorners & R_TOP_LEFT) != 0)
		{
			roundRect.moveTo(radius, 0);
		} else
		{
			roundRect.moveTo(0, 0);
		}

		// top right corner
		if ((roundCorners & R_TOP_RIGHT) != 0)
		{
			roundRect.lineTo(width - radius, 0);
			roundRect.curveTo((width - radius) + radiusControlPointModifier, 0, width,
					radius - radiusControlPointModifier, width, radius);
		} else
		{
			roundRect.lineTo(width, 0);
		}

		// bottom right corner
		if ((roundCorners & R_BOTTOM_RIGHT) != 0)
		{
			roundRect.lineTo(width, height - radius);
			roundRect.curveTo(width, (height - radius) + radiusControlPointModifier,
					(width - radius) + radiusControlPointModifier, height, width - radius, height);
		} else
		{
			roundRect.lineTo(width, height);
		}

		// bottom left corner
		if ((roundCorners & R_BOTTOM_LEFT) != 0)
		{
			roundRect.lineTo(radius, height);
			roundRect.curveTo(radius - radiusControlPointModifier, height, 0,
					(height - radius) + radiusControlPointModifier, 0, height - radius);
		} else
		{
			roundRect.lineTo(0, height);
		}

		// top left corner (again)
		if ((roundCorners & R_TOP_LEFT) != 0)
		{
			roundRect.lineTo(0, radius);
			roundRect.curveTo(0, radius - radiusControlPointModifier, radius - radiusControlPointModifier, 0, radius,
					0);
		} else
		{
			roundRect.lineTo(0, 0);
		}
		roundRect.closePath();
		return roundRect;
	}

	/**
	 * Tries to extract the stroke-width from the given stroke object.
	 * 
	 * @param s
	 *            the stroke.
	 * @return the stroke's width.
	 * 
	 * @see http://www.java2s.com/Code/Java/2D-Graphics-GUI/Triestoextractthestrokewidthfromthegivenstrokeobject.htm
	 */
	public static float getStrokeWidth(final Stroke s)
	{
		if (s instanceof BasicStroke)
		{
			final BasicStroke bs = (BasicStroke) s;
			return bs.getLineWidth();
		}
		return 1;
	}
}
