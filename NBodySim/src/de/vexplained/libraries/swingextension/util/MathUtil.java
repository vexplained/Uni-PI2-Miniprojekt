package de.vexplained.libraries.swingextension.util;

/**
 * @author vExplained
 *
 */
public class MathUtil
{
	/**
	 * @param x
	 *            the number
	 * @param n
	 *            the log base
	 */
	public static double logN(double x, double n)
	{
		// log b (x) = log ₑ (x) / log ₑ (b)
		return Math.log(x) / Math.log(n);
	}

	public static int clamp(int value, int lower, int upper)
	{
		return Math.max(Math.min(value, upper), lower);
	}

	public static double clamp(double value, double lower, double upper)
	{
		return Math.max(Math.min(value, upper), lower);
	}
}
