package de.grotefober.nbodysim.ui.graphics;

import java.awt.image.BufferedImage;

import de.vexplained.libraries.cvs_graphics_library.stdGraphics.DynamicCanvas;

/**
 * Record of an image this record represents as well as its z-index.
 * A <code>z-index</code> less than zero indicates this layer shall be drawn below the canvas surface.
 * 
 * @see DynamicCanvas
 * 
 * @author Levin
 *
 */
public record ImageLayer(BufferedImage img, int zIndex) implements Comparable<ImageLayer>
{

	@Override
	public int compareTo(ImageLayer o)
	{
		if (o == null)
		{
			return 1;
		}
		return this.zIndex == o.zIndex ? 0 : (this.zIndex > o.zIndex ? 1 : -1);
	}

}
