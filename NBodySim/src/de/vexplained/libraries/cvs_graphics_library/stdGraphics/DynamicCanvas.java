package de.vexplained.libraries.cvs_graphics_library.stdGraphics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JPanel;

/**
 * A dynamic canvas is used to display objects (like rectangles, circles,
 * lines). These objects are capable of changing position, size etc. without
 * explicitly telling the canvas to repaint.
 * 
 */
public class DynamicCanvas extends JPanel implements IDynamicContainer<DynamicObject>
{
	private static final long serialVersionUID = -5218505755523100049L;

	private int width, height;
	protected Color bgColor;
	private ArrayList<BufferedImage> images = new ArrayList<>();

	private List<DynamicObject> objects;

	/**
	 * The angle this {@link DynamicCanvas} is rotated in CCW direction, in degrees.
	 */
	private float rotationAngle = 0;

	/**
	 * Creates a new canvas object which may paint dynamic objects attached to it.
	 */
	public DynamicCanvas()
	{
		this(new Color(238, 238, 238));
	}

	public DynamicCanvas(Color bgColor)
	{
		super();

		this.objects = Collections.synchronizedList(new ArrayList<DynamicObject>());

		// Using exact matches instead of #equalsIgnoreCase(...) as #getProperty(...)
		// can return null

		// if (System.getProperty("sun.java2d.opengl") != "true" && System.getProperty("sun.java2d.opengl") != "True")
		// {
		// System.out.println("Warning! Java2D OpenGL Hardware Acceleration is disabled. "
		// + "Enable with \tSystem.setProperty(\"sun.java2d.opengl\", \"true\");"
		// + "\n(Affects every OS-platform; Use \'True\' to receive verbose console output about whether"
		// + "the OpenGL-based pipeline is initialized successfully)\n\n");
		// }
		// if (System.getProperty("sun.java2d.noddraw") != "true")
		// {
		// System.out.println("Warning! The Java GDI pipeline instead of the DirectDraw pipeline is used. "
		// + "Enable with \tSystem.setProperty(\"sun.java2d.noddraw\", \"true\");\n"
		// + "(Only affects applications running on Windows; Improves resize behaviour)\n\n");
		// }

		setBackground(bgColor);
	}

	/**
	 * Rotates this {@link DynamicCanvas}.
	 *
	 * @param rotationAngle
	 *            The angle this {@link DynamicCanvas} is rotated in CCW direction, in degrees.
	 */
	public void setCanvasRotation(float rotationAngle)
	{
		this.rotationAngle = rotationAngle;
		invalidate();
	}

	/**
	 * Returns the angle this {@link DynamicCanvas} is rotated in CCW direction, in degrees.
	 * 
	 * @return The angle this {@link DynamicCanvas} is rotated in CCW direction, in degrees.
	 */
	public float getCanvasRotation()
	{
		return rotationAngle;
	}

	public void addImage(BufferedImage image)
	{
		images.add(image);
		invalidate();
	}

	public BufferedImage getImage(int index)
	{
		return images.get(index);
	}

	@Override
	public void removeAllObjects()
	{
		// not calling #removeObject multiple times for performance reasons
		// (only one synchronized block instead of many syncs)
		synchronized (objects)
		{
			objects.clear();
		}
		invalidate();
	}

	@Override
	public void removeAllObjects(List<DynamicObject> listToRemove)
	{
		synchronized (objects)
		{
			objects.removeAll(listToRemove);
		}
		invalidate();
	}

	@Override
	public void removeObject(DynamicObject dynObj)
	{
		synchronized (objects)
		{
			if (dynObj.parentCanvas == this)
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

	@Override
	public List<DynamicObject> getObjects()
	{
		return objects;
	}

	@Override
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

		if (images != null && images.size() > 0)
		{
			for (BufferedImage img : images)
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
