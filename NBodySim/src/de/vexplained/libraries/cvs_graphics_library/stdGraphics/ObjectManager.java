package de.vexplained.libraries.cvs_graphics_library.stdGraphics;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author vExplained
 *
 */
public class ObjectManager
{
	/**
	 * Optimizes the scheduling routine for best time precision. Updates are scheduled with constant <b>period</b> using
	 * {@link ScheduledExecutorService#scheduleAtFixedRate(Runnable, long, long, TimeUnit)}.
	 * 
	 * This scheduling approach may be useful for simple physics simulations.
	 */
	public static final short OPTIMIZE_TIME_PRECISION = 0b0;
	/**
	 * Optimizes the scheduling routine for best appearance. Updates are scheduled with constant <b>delay</b> using
	 * {@link ScheduledExecutorService#scheduleWithFixedDelay(Runnable, long, long, TimeUnit)}.
	 * 
	 * This scheduling approach may be useful for animations to mitigate the visibility of stutters. However, the
	 * execution time of an update cycle directly influences frame rate.
	 */
	public static final short OPTIMIZE_ANIMATION_CONTINUITY = 0b1;

	private IDynamicContainer canvas;
	private ScheduledExecutorService tickScheduler;
	private Future<?> futureTask;
	private List<ITickable> objects;

	public ObjectManager(IDynamicContainer canvas)
	{
		this.canvas = canvas;
		objects = new ArrayList<ITickable>();
		tickScheduler = Executors.newSingleThreadScheduledExecutor();
	}

	/**
	 * Once called, the {@link #runTick()} method is ran as often as possible to
	 * achieve best precision.
	 * 
	 * Uses the default optimization approach {@link #OPTIMIZE_TIME_PRECISION}.
	 */
	public void enableTickScheduler()
	{
		enableTickScheduler(1);
	}

	/**
	 * Once called, the {@link #runTick()} method is ran as often as possible to
	 * achieve best precision.
	 * 
	 * Depending on the optimization approach specified, subsequent updates are either scheduled
	 * with constant period between updates ({@link #OPTIMIZE_TIME_PRECISION}) or by having a constant delay between the
	 * end of the current update and the start of the subsequent update ({@link #OPTIMIZE_ANIMATION_CONTINUITY}).
	 * 
	 * @param optimizationApproach
	 *            the optimization approach used, either {@link #OPTIMIZE_TIME_PRECISION} or
	 *            {@link #OPTIMIZE_ANIMATION_CONTINUITY}.
	 */
	public void enableTickScheduler(short optimizationApproach)
	{
		enableTickScheduler(0, optimizationApproach);
	}

	/**
	 * Once called, calls to the {@link #runTick()} method are scheduled with the specified <b>period</b>.
	 * 
	 * Uses the default optimization approach {@link #OPTIMIZE_TIME_PRECISION}.
	 * 
	 * @param period
	 *            time in milliseconds between successive task executions.
	 */
	public void enableTickScheduler(long period)
	{
		enableTickScheduler(period, OPTIMIZE_TIME_PRECISION);
	}

	/**
	 * Once called, calls to the {@link #runTick()} method are scheduled with the specified period <i>or</i> delay,
	 * depending on the optimization approach used.
	 * 
	 * Depending on the optimization approach specified, subsequent updates are either scheduled
	 * with constant period between updates ({@link #OPTIMIZE_TIME_PRECISION}) or by having a constant delay between the
	 * end of the current update and the start of the subsequent update ({@link #OPTIMIZE_ANIMATION_CONTINUITY}).
	 * 
	 * @param periodOrDelay
	 *            the period (when using {@link #OPTIMIZE_TIME_PRECISION}) or delay (when using
	 *            {@link #OPTIMIZE_ANIMATION_CONTINUITY}) between individual updates.
	 * @param optimizationApproach
	 *            the optimization approach used, either {@link #OPTIMIZE_TIME_PRECISION} or
	 *            {@link #OPTIMIZE_ANIMATION_CONTINUITY}.
	 */
	public void enableTickScheduler(long periodOrDelay, short optimizationApproach)
	{
		if (periodOrDelay <= 0)
		{
			periodOrDelay = 1;
		}

		if (optimizationApproach == 0)
		{
			this.futureTask = tickScheduler.scheduleAtFixedRate(new Runnable()
			{
				@Override
				public void run()
				{
					runTick();
				}
			}, 0, periodOrDelay, TimeUnit.MILLISECONDS);
		} else
		{
			this.futureTask = tickScheduler.scheduleWithFixedDelay(new Runnable()
			{
				@Override
				public void run()
				{
					runTick();
				}
			}, 0, periodOrDelay, TimeUnit.MILLISECONDS);
		}
	}

	public void addToTickScheduler(ITickable toAdd)
	{
		synchronized (objects)
		{
			if (!objects.contains(toAdd))
			{
				objects.add(toAdd);
			}
		}
	}

	/**
	 * Adds every tickable object ({@link DynamicTickableObject}) from the canvas
	 * specified in the constructor.<br>
	 * Calls {@link #addToTickScheduler(DynamicTickableObject)} for every tickable
	 * object.
	 */
	public void addAllFromCanvas()
	{
		for (Object obj : canvas.getObjects())
		{
			if (obj instanceof ITickable)
			{
				addToTickScheduler((ITickable) obj);
			}
		}
	}

	/**
	 * Removes an object from the tick scheduler so it will no longer receive automated calls to its
	 * {@link DynamicTickableObject#tick()} method.
	 */
	public void removeFromTickScheduler(ITickable toRemove)
	{
		synchronized (objects)
		{
			objects.remove(toRemove);
		}
	}

	/**
	 * Removes all objects from the tick scheduler.
	 */
	public void removeAll()
	{
		synchronized (objects)
		{
			objects.clear();
		}
	}

	/**
	 * Stops the automatic execution of {@link #runTick()}.
	 *
	 * @throws ExecutionException
	 * @throws InterruptedException
	 */
	public void disableTickScheduler() throws InterruptedException, ExecutionException
	{
		if (futureTask != null)
		{
			futureTask.cancel(false);

			try
			{
				futureTask.get(); // throw possible exceptions
			} catch (CancellationException e)
			{
				// Cull stack trace for CancellationException; stack traces of exceptions within #runTick are rethrown
			}
		}
		futureTask = null;
	}

	/**
	 * Calls the {@link DynamicTickableObject#tick()} method for every
	 * registered line.
	 */
	public void runTick()
	{
		// Synchronize access to objects to avoid concurrency issues
		synchronized (objects)
		{
			for (ITickable obj : objects)
			{
				obj.tick();
			}
		}
		canvas.invalidate();
	}
}
