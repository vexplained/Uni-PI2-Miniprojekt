package de.grotefober.nbodysim.ui.graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JPanel;

import de.grotefober.nbodysim.sim.PhysicsManager;
import de.grotefober.nbodysim.ui.graphics.dynObjects.DynamicPhysicsObject;
import de.vexplained.libraries.cvs_graphics_library.stdGraphics.IDynamicComponent;
import de.vexplained.libraries.cvs_graphics_library.stdGraphics.IDynamicContainer;

public class PhysicsUniverse2D extends JPanel implements IDynamicContainer<IDynamicComponent>
{
	private static final long serialVersionUID = 7569088216700449776L;

	private int width, height;
	protected Color bgColor;
	private List<ImageLayer> imageLayers;

	/**
	 * Holds all <b>non-physics</b> objects.
	 */
	private Set<IDynamicComponent> dynObjects;
	private Set<DynamicPhysicsObject> dynPhysObjects;

	/**
	 * Creates a new canvas object.
	 */
	public PhysicsUniverse2D()
	{
		this(Color.BLACK);
	}

	public PhysicsUniverse2D(Color bgColor)
	{
		super();

		// Use LinkedHashSet for drawable objects to draw them in the order they were added
		this.dynObjects = Collections.synchronizedSet(new LinkedHashSet<IDynamicComponent>());
		this.dynPhysObjects = Collections.synchronizedSet(new HashSet<DynamicPhysicsObject>());
		this.imageLayers = Collections.synchronizedList(new ArrayList<ImageLayer>());

		setBackground(bgColor);
	}

	public void addImageLayer(ImageLayer layer)
	{
		imageLayers.add(layer);
		Collections.sort(imageLayers);
		invalidate();
	}

	/**
	 * @deprecated TODO: Not implemented.
	 */
	@Deprecated
	public BufferedImage getImage(int zIndex)
	{
		return null;
	}

	// TODO: rename methods from "...Objects" to "...Component" to reflect generalisation from "DynamicObject" to
	// "DynamicComponent"?
	@Override
	public void removeAllObjects()
	{
		// not calling #removeObject multiple times for performance reasons
		// (only one synchronized block instead of many syncs)
		synchronized (dynObjects)
		{
			dynObjects.clear();
		}
		synchronized (dynPhysObjects)
		{
			dynPhysObjects.clear();
		}
		invalidate();
	}

	/**
	 * @deprecated Untested.
	 */
	@Deprecated
	@Override
	public void removeAllObjects(List<IDynamicComponent> listToRemove)
	{
		synchronized (dynObjects)
		{
			dynObjects.removeAll(listToRemove);
		}
		synchronized (dynPhysObjects)
		{
			dynPhysObjects.removeAll(listToRemove);
		}
		invalidate();
	}

	@Override
	public boolean removeObject(IDynamicComponent dynObj)
	{
		boolean result = false;
		if (dynObj instanceof DynamicPhysicsObject)
		{
			synchronized (dynPhysObjects)
			{
				if (dynObj.getParentCanvas() == this)
				{
					result = dynPhysObjects.remove(dynObj);
				}
			}
		} else
		{
			synchronized (dynObjects)
			{
				if (dynObj.getParentCanvas() == this)
				{
					result = dynObjects.remove(dynObj);
				}
			}
		}

		invalidate();
		return result;
	}

	@Override
	public List<IDynamicComponent> getObjects()
	{
		List<IDynamicComponent> allObjects = new ArrayList<>(dynObjects);
		allObjects.addAll(dynPhysObjects);
		return allObjects;
	}

	public Set<DynamicPhysicsObject> getDynPhysicsObjects()
	{
		return dynPhysObjects;
	}

	@Override
	public void addObject(IDynamicComponent object)
	{
		object.setParentCanvas(this);
		if (object instanceof DynamicPhysicsObject)
		{
			dynPhysObjects.add((DynamicPhysicsObject) object);
		} else
		{
			dynObjects.add(object);
		}
		invalidate();
	}

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

		synchronized (dynObjects)
		{
			for (IDynamicComponent obj : dynObjects)
			{
				if (obj != null)
				{
					obj.draw(g2d);
				} else
				{
					throw new NullPointerException("Registered object to draw is null!");
				}
			}
		}

		synchronized (dynPhysObjects)
		{
			for (IDynamicComponent obj : dynPhysObjects)
			{
				if (obj != null)
				{
					obj.draw(g2d);
				} else
				{
					throw new NullPointerException("Registered object to draw is null!");
				}
			}
		}
	}

	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g.create();

		drawCanvas(g2d);

		if (imageLayers != null && imageLayers.size() > 0)
		{
			for (ImageLayer imgLayer : imageLayers)
			{
				BufferedImage img = imgLayer.img();
				g2d.drawImage(img, 0, 0, this);
			}
		}
	}

	@Override
	public void invalidate()
	{
		this.repaint();
	}

	/**
	 * @return A newly created {@link PhysicsManager} using this canvas.
	 */
	public PhysicsManager createPhysicsManager()
	{
		return new PhysicsManager(this);
	}
}
