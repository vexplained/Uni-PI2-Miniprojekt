package de.vexplained.libraries.cvs_graphics_library.util;

/**
 * @author vExplained
 *
 */
public class StringUtil
{
	/**
	 * Formats the input double value in scientific notation, <b>only if</b> <code>abs(d) >= 1E+4</code>. Then, it is
	 * formatted with <code>sigFigs</code> significant figures.
	 * <code>decimalPlaces - 1</code> decimals if noted in scientific notation.
	 */
	public static String formatDoubleAsScientific(double d, int sigFigs)
	{
		sigFigs = Math.abs(sigFigs);

		if (Math.abs(Math.log(Math.abs(d))) < 4)
		{
			if ((long) d == d)
			{
				return (long) d + "";
			} else
			{
				return d + "";
			}
		} else
		{
			return String.format("%" + sigFigs + "." + (sigFigs - 1) + "E", d);
		}
	}
}
