package de.grotefober.nbodysim.sim;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import de.grotefober.nbodysim.ui.graphics.PhysicsUniverse2D;
import de.grotefober.nbodysim.ui.graphics.dynObjects.DynamicPhysicsObject;
import de.vexplained.libraries.cvs_graphics_library.stdGraphics.ITickable;
import de.vexplained.libraries.cvs_graphics_library.stdGraphics.ObjectManager;

public class PhysicsManager extends ObjectManager
{

	/**
	 * Contains all {@link DynamicPhysicsObject}s.
	 */
	protected Set<DynamicPhysicsObject> universe;

	private Set<PhysicsObject> physicsShadows;

	/**
	 * Time delta between two physics calculations, in seconds.
	 */
	private double simulationTimeStep;

	/**
	 * Factor to multiply pixel distances by to get "real" distances.
	 * 
	 * For instance, a value of <code>1E9</code> results in each pixel representing <b>one million kilometers</b>.
	 */
	public final double DISTANCE_FACTOR = 1E9;

	/**
	 * Acceleration limit (absolute value) to prevent objects flying outside the rendered region when approaching each
	 * other closely.
	 * TODO: Fix grammar in this doc ;)
	 */
	public final double ACCEL_CAP = 1E9;

	/**
	 * Velocity limit (absolute value) to prevent objects flying outside the rendered region when approaching each other
	 * closely.
	 * TODO: Fix grammar in this doc ;)
	 */
	public final double VELOCITY_CAP = DISTANCE_FACTOR * 1E2;

	/**
	 * Instantiates a new {@link PhysicsManager} using the given universe and a simulation time step of 1/60 s.
	 */
	public PhysicsManager(PhysicsUniverse2D physUniverse)
	{
		super(physUniverse);
		// Create copy of physUniverse set
		this.universe = Collections.synchronizedSet(physUniverse.getDynPhysicsObjects());
		this.physicsShadows = Collections.synchronizedSet(new HashSet<>(this.universe.size()));
		this.simulationTimeStep = 50_000d; // simulate 50_000 second per frame
	}

	public double getSimulationTimeStep()
	{
		return simulationTimeStep;
	}

	public void setSimulationTimeStep(double simulationTimeStep)
	{
		this.simulationTimeStep = simulationTimeStep;
	}

	public Set<DynamicPhysicsObject> getUniverse()
	{
		return universe;
	}

	public Set<PhysicsObject> getPhysicsShadows()
	{
		return physicsShadows;
	}

	@Override
	public void enableTickScheduler()
	{
		// super.enableTickScheduler((int) (this.simulationTimeStep * 1000), OPTIMIZE_TIME_PRECISION);
		// FIXME change in prod
		super.enableTickScheduler(16, OPTIMIZE_TIME_PRECISION);
	}

	@Override
	public void addToTickScheduler(ITickable toAdd)
	{
		if (toAdd instanceof DynamicPhysicsObject)
		{
			synchronized (universe)
			{
				universe.add((DynamicPhysicsObject) toAdd);
			}
			synchronized (physicsShadows)
			{
				physicsShadows.add(((DynamicPhysicsObject) toAdd).getPhysicsObject());
			}
		} else
		{
			super.addToTickScheduler(toAdd);
		}
	}

	@Override
	public void removeFromTickScheduler(ITickable toRemove)
	{
		if (toRemove instanceof DynamicPhysicsObject)
		{
			synchronized (universe)
			{
				universe.remove(toRemove);
			}
			synchronized (physicsShadows)
			{
				physicsShadows.remove(((DynamicPhysicsObject) toRemove).getPhysicsObject());
			}
		} else
		{
			super.removeFromTickScheduler(toRemove);
		}
	}

	@Override
	public void removeAll()
	{
		super.removeAll();
		synchronized (universe)
		{
			universe.clear();
		}
		synchronized (physicsShadows)
		{
			physicsShadows.clear();
		}
	}

	@Override
	public void runTick()
	{
		super.runTick();

		synchronized (universe)
		{
			// TODO optimize somehow?
			for (IPhysicsTickable obj : universe)
			{
				obj.tickAcceleration(this);
			}
			for (IPhysicsTickable obj : universe)
			{
				obj.tickVelocity(this);
			}
			for (IPhysicsTickable obj : universe)
			{
				obj.tickPosition(this);

				if (obj instanceof DynamicPhysicsObject)
				{
					// TODO: better approach for syncing? Define #syncDynamicObj in IPhysicsTickable? (-> no)
					// How to avoid typechecking & casting in each loop iteration of #runTick?
					((DynamicPhysicsObject) obj).syncDynamicObject();
				}
			}
		}
		// Repaint canvas
		// TODO perhaps delegate to separate worker
		// => make simulation & display independent
		// When doing so: also move obj#syncDynamicObject in loop above
		this.canvas.invalidate();
	}

}
