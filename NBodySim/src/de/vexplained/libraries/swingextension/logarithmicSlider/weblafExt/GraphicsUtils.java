package de.vexplained.libraries.swingextension.logarithmicSlider.weblafExt;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Area;
import java.util.HashMap;
import java.util.Map;

/**
 * This class provides a set of utilities to work with Graphics2D.
 *
 * @author Mikle Garin
 * 
 * @see WebLaF (http://weblookandfeel.com/)
 */
public class GraphicsUtils
{
	/**
	 * Setting clip Shape by taking old clip Shape into account
	 */

	public static Shape intersectClip(final Graphics2D g2d, final Shape clip)
	{
		return intersectClip(g2d, clip, true);
	}

	public static Shape intersectClip(final Graphics2D g2d, final Shape clip, final boolean shouldSetup)
	{
		if (shouldSetup && clip != null)
		{
			final Shape oldClip = g2d.getClip();

			// Optimized by Graphics2D clip intersection
			g2d.clip(clip);

			return oldClip;
		} else
		{
			return null;
		}
	}

	public static Shape subtractClip(final Graphics g, final Shape clip)
	{
		return subtractClip(g, clip, true);
	}

	public static Shape subtractClip(final Graphics g, final Shape clip, final boolean shouldSetup)
	{
		if (shouldSetup && clip != null)
		{
			final Shape oldClip = g.getClip();
			if (oldClip != null)
			{
				// Area-based substraction
				final Area finalClip = new Area(oldClip);
				finalClip.subtract(new Area(clip));
				g.setClip(finalClip);
			}
			return oldClip;
		} else
		{
			return null;
		}
	}

	/**
	 * Draws web styled shade using specified shape
	 */

	public static void drawShade(final Graphics2D g2d, final Shape shape, final Color shadeColor, final int width)
	{
		drawShade(g2d, shape, ShadeType.simple, shadeColor, width, null, true);
	}

	public static void drawShade(final Graphics2D g2d, final Shape shape, final ShadeType shadeType,
			final Color shadeColor, final int width)
	{
		drawShade(g2d, shape, shadeType, shadeColor, width, null, true);
	}

	public static void drawShade(final Graphics2D g2d, final Shape shape, final ShadeType shadeType,
			final Color shadeColor, int width, final Shape clip, final boolean round)
	{
		// Ignoring shade with width less than 2
		if (width <= 1)
		{
			return;
		}

		// Applying clip
		final Shape oldClip = clip != null ? intersectClip(g2d, clip) : subtractClip(g2d, shape);

		// Saving composite
		final Composite oldComposite = g2d.getComposite();
		float currentComposite = 1f;
		if (oldComposite instanceof AlphaComposite
				&& ((AlphaComposite) oldComposite).getRule() == AlphaComposite.SRC_OVER)
		{
			currentComposite = ((AlphaComposite) oldComposite).getAlpha();
		}

		// Saving stroke
		final Stroke oldStroke = g2d.getStroke();

		// Drawing shade
		if (shadeColor != null)
		{
			g2d.setPaint(shadeColor);
		}
		if (shadeType.equals(ShadeType.simple))
		{
			// Drawing simple shade
			final float simpleShadeOpacity = 0.7f;
			if (simpleShadeOpacity < 1f)
			{
				g2d.setComposite(
						AlphaComposite.getInstance(AlphaComposite.SRC_OVER, simpleShadeOpacity * currentComposite));
			}
			g2d.setStroke(getStroke(width * 2, round ? BasicStroke.CAP_ROUND : BasicStroke.CAP_BUTT));
			g2d.draw(shape);
		} else
		{
			// Drawing complex gradient shade
			width = width * 2;
			for (int i = width; i >= 2; i -= 2)
			{
				// float minTransparency = 0.2f;
				// float maxTransparency = 0.6f;
				// float opacity = minTransparency + ( maxTransparency - minTransparency ) * ( 1 - ( i - 2 ) / ( width -
				// 2 ) );
				final float opacity = (float) (width - i) / (width - 1);
				g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity * currentComposite));
				g2d.setStroke(getStroke(i, round ? BasicStroke.CAP_ROUND : BasicStroke.CAP_BUTT));
				g2d.draw(shape);
			}
		}

		// Restoring initial graphics settings
		g2d.setStroke(oldStroke);
		g2d.setComposite(oldComposite);
		g2d.setClip(oldClip);
	}

	/**
	 * Strokes caching
	 */

	private static final Map<String, Stroke> cachedStrokes = new HashMap<String, Stroke>();

	public static Stroke getStroke(final int width)
	{
		return getStroke(width, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
	}

	public static Stroke getStroke(final int width, final int cap)
	{
		return getStroke(width, cap, BasicStroke.JOIN_ROUND);
	}

	public static Stroke getStroke(final int width, final int cap, final int join)
	{
		final String key = width + "," + cap + "," + join;
		Stroke stroke = cachedStrokes.get(key);
		if (stroke == null)
		{
			stroke = new BasicStroke(width, cap, join);
			cachedStrokes.put(key, stroke);
		}
		return stroke;
	}
}
