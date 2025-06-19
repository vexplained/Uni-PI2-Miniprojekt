package de.grotefober.nbodysim.util;

import de.grotefober.nbodysim.sim.Vector2D;

public class Vector2DUtils
{
	public static double distCubed(Vector2D p1, Vector2D p2)
	{
		// square root operation or exponentiation by 3/2 necessary
		// => both expensive
		double dist = p1.distance(p2);
		return dist * dist * dist;
	}
}
