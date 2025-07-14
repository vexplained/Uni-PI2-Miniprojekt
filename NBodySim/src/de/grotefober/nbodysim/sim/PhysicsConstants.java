package de.grotefober.nbodysim.sim;

// final: make it un-inheritable
public final class PhysicsConstants
{
	/**
	 * Gravitational constant G, in SI units [m^3 kg^-1 s^-2].
	 */
	// public static final double G = 6.67430e-11D;
	// FIXME correct in prod
	// public static final double G = 1D;
	public static final double G = 6.67430e-11D;

	/**
	 * Astronomical unit, in meters.
	 */
	public static final double AU = 149_597_870_700D;
}
