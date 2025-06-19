package de.grtfbr.ui.graphics;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JPanel;

import de.grtfbr.ui.graphics.dynObjects.DynamicObject;

/**
 * @author Levin
 *
 */
public class DynamicCanvas extends JPanel
{
	private static final long serialVersionUID = 7740498773966049273L;

	private static final Color DEFAULT_COLOR_BG = new Color(0x23224A);

	private Color bgColor;
	private Set<DynamicObject> objects;
	private Set<ImageLayer> imageLayers;  // TODO unimplemented

	public DynamicCanvas()
	{
		this(DEFAULT_COLOR_BG);
		this.objects = Collections.synchronizedSet(new HashSet<DynamicObject>()); // Using set instead of list to
																					 // prevent duplicates
	}

	public DynamicCanvas(Color bgColor)
	{
		this.bgColor = bgColor;
	}

	/**
	 * Set the rendering hints for the {@link Graphics2D} instance provided. The rendering hints set include
	 * <ul>
	 * <li>{@link RenderingHints#KEY_ANTIALIASING}: Enable anti aliasing to prevent jagged edges
	 * <li>{@link RenderingHints#KEY_FRACTIONALMETRICS}: Support fractional values for dimensions and positions of
	 * objects
	 * <li>{@link RenderingHints#KEY_TEXT_ANTIALIASING}
	 * <li>{@link RenderingHints#KEY_STROKE_CONTROL}
	 * </ul>
	 */
	protected void setupRenderingHints(Graphics2D g2d)
	{
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
		// KEEP TURNED OFF:
		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		// Rendering hint to keep sub-pixel accuracy and waive possible hardware optimizations
		g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
	}

	protected void drawCanvas(Graphics2D g2d)
	{
		setupRenderingHints(g2d);

		//
	}
}
