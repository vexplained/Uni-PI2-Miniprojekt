package de.vexplained.libraries.cvs_graphics_library.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author vExplained
 *
 */
public class MathUtil
{
	/**
	 * Rounds a double to the specified number of decimal places.
	 * 
	 * @see https://stackoverflow.com/a/2808648/19474335
	 */
	public static double round(double value, int places)
	{
		if (places < 0)
		{
			throw new IllegalArgumentException();
		}

		BigDecimal bd = BigDecimal.valueOf(value);
		bd = bd.setScale(places, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}

}
