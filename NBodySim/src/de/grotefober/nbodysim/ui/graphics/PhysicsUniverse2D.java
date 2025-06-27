package de.grotefober.nbodysim.ui.graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JPanel;

import de.grotefober.nbodysim.ui.graphics.dynObjects.DynamicPhysicsObject;
import de.vexplained.libraries.cvs_graphics_library.stdGraphics.DynamicObject;
import de.vexplained.libraries.cvs_graphics_library.stdGraphics.ObjectManager;

public class PhysicsUniverse2D extends JPanel
{
	private static final long serialVersionUID = 7569088216700449776L;

	private int width, height;
	protected Color bgColor;
	private List<ImageLayer> imageLayers;

	private List<DynamicPhysicsObject> dynPhysObjects;

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

		this.dynPhysObjects = Collections.synchronizedList(new ArrayList<DynamicPhysicsObject>());
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

	public void removeAllObjects()
	{
		// not calling #removeObject multiple times for performance reasons
		// (only one synchronized block instead of many syncs)
		synchronized (dynPhysObjects)
		{
			dynPhysObjects.clear();
		}
		invalidate();
	}

	public void removeAllObjects(List<DynamicPhysicsObject> listToRemove)
	{
		synchronized (dynPhysObjects)
		{
			dynPhysObjects.removeAll(listToRemove);
		}
		invalidate();
	}

	public void removeObject(DynamicPhysicsObject dynObj)
	{
		synchronized (dynPhysObjects)
		{
			if (dynObj.getDynamicObject().getParentCanvas() == this)
			{
				int index = objects.indexOf(dynObj);
				if (index > 0)
				{
					objects.remove(index);
				}
			}
		}
		invalidate();
	}

	public List<DynamicObject> getObjects()
	{
		return objects;
	}

	public void addObject(DynamicObject object)
	{
		object.setParentCanvas(this);
		objects.add(object);
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

		if (rotationAngle != 0)
		{
			g2d.rotate(-Math.toRadians(rotationAngle), getWidth() / 2D, getHeight() / 2D);
		}

		synchronized (objects)
		{
			for (DynamicObject obj : objects)
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
			for (BufferedImage img : imageLayers)
			{
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
	 * @return A newly created {@link ObjectManager} using this canvas.
	 */
	public ObjectManager createObjectManager()
	{
		return new ObjectManager(this);
	}
}
